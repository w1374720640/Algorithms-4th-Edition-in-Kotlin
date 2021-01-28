package chapter4.section4

import chapter2.section4.HeapIndexMinPriorityQueue
import edu.princeton.cs.algs4.Stack

/**
 * 给定两点的最短路径
 * 设计并实现一份API，使用Dijkstra算法的改进版本解决加权有向图中给定两点的最短路径问题
 *
 * 解：只遍历图的一部分，不会放松所有顶点
 */
class SourceSinkShortestPaths(digraph: EdgeWeightedDigraph, s: Int, private val t: Int) {
    private val distTo = Array(digraph.V) { Double.POSITIVE_INFINITY }
    private val edgeTo = arrayOfNulls<DirectedEdge>(digraph.V)
    private val pq = HeapIndexMinPriorityQueue<Double>(digraph.V)

    init {
        distTo[s] = 0.0
        pq[s] = 0.0
        while (!pq.isEmpty()) {
            val v = pq.delMin().second
            if (v == t) break
            relax(digraph, v)
        }
    }

    private fun relax(digraph: EdgeWeightedDigraph, v: Int) {
        digraph.adj(v).forEach { edge ->
            val w = edge.to()
            if (distTo[w] > distTo[v] + edge.weight) {
                distTo[w] = distTo[v] + edge.weight
                edgeTo[w] = edge
                pq[w] = distTo[w]
            }
        }
    }

    fun hasPath(): Boolean {
        return distTo[t] != Double.POSITIVE_INFINITY
    }

    fun path(): Iterable<DirectedEdge>? {
        if (!hasPath()) return null
        val stack = Stack<DirectedEdge>()
        var edge = edgeTo[t]
        while (edge != null) {
            stack.push(edge)
            edge = edgeTo[edge.from()]
        }
        return stack
    }
}

fun main() {
    val digraph = getTinyEWD()
    val paths = SourceSinkShortestPaths(digraph, 0, 5)
    println(paths.path()?.joinToString())
}