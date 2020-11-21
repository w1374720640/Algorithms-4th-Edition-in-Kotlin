package chapter4.section1

import edu.princeton.cs.algs4.In

/**
 * 判断图是否有环（假设不存在自环或平行边）
 */
class Cycle(graph: Graph) {
    private val marked = BooleanArray(graph.V)
    var hasCycle = false
        private set

    init {
        for (s in 0 until graph.V) {
            if (!marked[s]) {
                dfs(graph, s, s)
            }
        }
    }

    private fun dfs(graph: Graph, v: Int, parent: Int) {
        if (hasCycle) return
        marked[v] = true
        graph.adj(v).forEach { w ->
            if (!marked[w]) {
                dfs(graph, w, v)
            } else if (w != parent) {
                hasCycle = true
                return
            }
        }
    }
}

fun main() {
    val graph = Graph(In("./data/tinyG.txt"))
    println(Cycle(graph).hasCycle)

    val graph2 = Graph(5)
    graph2.addEdge(0, 1)
    graph2.addEdge(1, 2)
    graph2.addEdge(2, 3)
    graph2.addEdge(3, 4)
    println(Cycle(graph2).hasCycle)
}