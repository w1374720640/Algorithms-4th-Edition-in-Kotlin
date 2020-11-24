package chapter4.section1

/**
 * 边的连通性
 * 在一副连通图中，如果一条边被删除后图会被分为两个独立的连通分量，这条边就被成为桥。
 * 没有桥的图称为边连通图。
 * 开发一种基于深度优先搜索算法的数据类型，判断一个图是否时边连通图。
 *
 * 解：这题和4.1.34都可以参考练习4.1.10中的代码实现
 */
fun Graph.isConnectedGraph(): Boolean {
    for (s in 0 until V) {
        val iterable = adj(s)
        if (iterable.count() <= 1) continue

        val marked = BooleanArray(V)
        marked[s] = true
        var i = 1

        // 任意选择一个相邻顶点开始深度优先遍历
        val v = iterable.first()
        i += dfs(this, marked, v)
        val search = DepthFirstSearch(this, s)
        if (i != search.count()) return false
    }
    return true
}

private fun dfs(graph: Graph, marked: BooleanArray, v: Int): Int {
    if (!marked[v]) {
        marked[v] = true
        var i = 1
        graph.adj(v).forEach {
            i += dfs(graph, marked, it)
        }
        return i
    }
    return 0
}

fun main() {
    val graph = Graph(6)
    graph.addEdge(0, 1)
    graph.addEdge(1, 2)
    graph.addEdge(0, 2)
    graph.addEdge(2, 3)
    graph.addEdge(3, 4)
    graph.addEdge(4, 5)
    graph.addEdge(5, 3)
    println(graph.isConnectedGraph())
    graph.addEdge(0, 4)
    println(graph.isConnectedGraph())
    drawGraph(graph, showIndex = true, pointRadius = GraphGraphics.DEFAULT_POINT_RADIUS * 2)
}
