package chapter4.section4

import chapter2.section4.HeapIndexMinPriorityQueue

/**
 * 最短路径的Dijkstra算法
 * 命题R：Dijkstra算法能够解决边权重非负的加权有向图的单起点最短路径问题
 * 所需的空间与V成正比，时间与ElogV成正比（最坏情况下）
 */
class DijkstraSP(digraph: EdgeWeightedDigraph, s: Int) : SP(digraph, s) {
    private val pq = HeapIndexMinPriorityQueue<Double>(digraph.V)

    init {
        distTo[s] = 0.0
        pq[s] = 0.0
        while (!pq.isEmpty()) {
            relax(pq.delMin().second)
        }
    }

    private fun relax(v: Int) {
        digraph.adj(v).forEach { edge ->
            val w = edge.to()
            if (distTo[w] > distTo[v] + edge.weight) {
                distTo[w] = distTo[v] + edge.weight
                edgeTo[w] = edge
                pq[w] = distTo[w]
            }
        }
    }
}

fun main() {
    val digraph = getTinyEWD()
    val sp = DijkstraSP(digraph, 0)
    println(sp.toString())
}