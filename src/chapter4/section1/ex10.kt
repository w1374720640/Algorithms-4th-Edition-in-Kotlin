package chapter4.section1

/**
 * 证明在任意一副连通图中都存在一个顶点，删去它（以及和它相连的所有边）不会影响到图的连通性
 * 编写一个深度优先搜索的方法找出这样一个顶点
 * 提示：留心那些相邻顶点全部都被标记过的顶点
 *
 * 解：如果顶点只有一条边，那么，删除这个顶点以及和它相连的边不会影响图的连通性
 * 如果一个顶点[s]有多条边，选择任意一条边开始深度优先搜索，搜索结束时：
 * 若找到的顶点数量和连通图总顶点数相等，说明与[s]相邻的其他顶点可以不通过顶点[s]连通，删除顶点[s]不会影响连通性
 * 若找到的顶点数量和连通图总顶点数不等，说明至少有一个相邻点与其他相邻点只能通过顶点[s]连通，删除顶点[s]会导致连通图分裂
 */
fun ex10(graph: Graph, s: Int): Boolean {
    val iterable = graph.adj(s)
    var i = 0
    var iterator = iterable.iterator()
    while (iterator.hasNext()) {
        i++
        iterator.next()
    }
    if (i <= 1) return true

    val marked = BooleanArray(graph.V)
    marked[s] = true
    i = 1

    iterator = iterable.iterator()
    // 任意选择一个相邻顶点开始深度优先遍历
    val v = iterator.next()
    i += dfs(graph, marked, v)
    val search = DepthFirstSearch(graph, s)
    return i == search.count()
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
    val graph = Graph(7)
    graph.addEdge(0,1)
    graph.addEdge(1,2)
    graph.addEdge(0,2)
    graph.addEdge(2,3)
    graph.addEdge(3,4)
    graph.addEdge(4,5)
    graph.addEdge(5,3)
    graph.addEdge(5,6)
    for (i in 0 until graph.V) {
        println("$i : ${ex10(graph, i)}")
    }
    drawGraph(graph, showIndex = true, pointRadius = GraphGraphics.DEFAULT_POINT_RADIUS * 2)
}