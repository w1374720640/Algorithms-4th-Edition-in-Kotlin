package chapter4.section1

/**
 * 判断图是否是二分图
 * （能够用两种颜色将图的所有顶点着色，使得任意一条边的两个端点的颜色都不相同）
 */
class TwoColor(graph: Graph) {
    private val marked = BooleanArray(graph.V)
    private val colors = BooleanArray(graph.V)
    var isTwoColorable = true
        private set

    init {
        for (s in 0 until graph.V) {
            if (!marked[s]) {
                dfs(graph, s)
            }
        }
    }

    private fun dfs(graph: Graph, v: Int) {
        if (!isTwoColorable) return
        marked[v] = true
        graph.adj(v).forEach { w ->
            if (!marked[w]) {
                colors[w] = !colors[v]
                dfs(graph, w)
            } else if (colors[w] == colors[v]) {
                isTwoColorable = false
                return
            }
        }
    }
}

fun main() {
    val graph1 = Graph(5)
    graph1.addEdge(0, 1)
    graph1.addEdge(1, 2)
    graph1.addEdge(2, 3)
    graph1.addEdge(3, 4)
    graph1.addEdge(4, 0)
    println(TwoColor(graph1).isTwoColorable)

    val graph2 = Graph(6)
    graph2.addEdge(0, 1)
    graph2.addEdge(1, 2)
    graph2.addEdge(2, 3)
    graph2.addEdge(3, 4)
    graph2.addEdge(4, 5)
    graph2.addEdge(5, 0)
    println(TwoColor(graph2).isTwoColorable)
}