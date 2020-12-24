package chapter4.section3

import edu.princeton.cs.algs4.Stack

/**
 * 找到一幅加权无向图中的一个环
 * 参考有向环[chapter4.section2.DirectedCycle]实现
 */
class EdgeWeightedGraphCycle(graph: EWG) {
    private val marked = BooleanArray(graph.V) // 无向图遍历不需要onStack参数记录顶点是否在当前栈中
    private val edgeTo = Array<Edge?>(graph.V) { null }
    private var cycle: Stack<Edge>? = null

    init {
        for (v in 0 until graph.V) {
            if (hasCycle()) break
            if (!marked[v]) {
                dfs(graph, v, v)
            }
        }
    }

    private fun dfs(graph: EWG, v: Int, s: Int) {
        marked[v] = true
        graph.adj(v).forEach { edge ->
            if (hasCycle()) return
            val w = edge.other(v)
            if (!marked[w]) {
                edgeTo[w] = edge
                dfs(graph, w, v)
            } else if (w != s) {
                cycle = Stack<Edge>()
                var lastVertex = v
                while (lastVertex != w) {
                    val lastEdge = edgeTo[lastVertex]!!
                    cycle!!.push(lastEdge)
                    lastVertex = lastEdge.other(lastVertex)
                }
                cycle!!.push(edge)
            }
        }
    }

    fun hasCycle(): Boolean {
        return cycle != null
    }

    fun cycle(): Iterable<Edge>? {
        return cycle
    }
}

fun main() {
    val graph = getTinyWeightedGraph()
    val cycle = EdgeWeightedGraphCycle(graph)
    if (cycle.hasCycle()) {
        println("has cycle")
        println(cycle.cycle()!!.joinToString())
    } else {
        println("no cycle")
    }
}