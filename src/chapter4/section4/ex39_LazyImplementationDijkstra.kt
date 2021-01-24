package chapter4.section4

import chapter2.section4.HeapMinPriorityQueue

/**
 * 延时Dijkstra算法的实现
 * 根据正文实现Dijkstra算法的延时版本
 *
 * 解：参考[chapter4.section3.LazyPrimMST]类和[BellmanFordSP]类
 */
class LazyDijkstraSP(digraph: EdgeWeightedDigraph, s: Int) : SP(digraph, s) {
    private val onQueue = BooleanArray(digraph.V)
    private val pq = HeapMinPriorityQueue<Int>()

    init {
        distTo[s] = 0.0
        pq.insert(s)
        onQueue[s] = true
        while (!pq.isEmpty()) {
            relax(pq.delMin())
        }
    }

    private fun relax(v: Int) {
        onQueue[v] = false
        digraph.adj(v).forEach { edge ->
            val w = edge.to()
            if (distTo[w] > distTo[v] + edge.weight) {
                distTo[w] = distTo[v] + edge.weight
                edgeTo[w] = edge
                if (!onQueue[w]) {
                    pq.insert(w)
                    onQueue[w] = true
                }
            }
        }
    }
}

fun main() {
    val digraph = getTinyEWD()
    val sp = LazyDijkstraSP(digraph, 0)
    println(sp.toString())
}