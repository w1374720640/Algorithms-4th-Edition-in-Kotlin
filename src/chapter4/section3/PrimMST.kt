package chapter4.section3

import chapter2.section4.HeapIndexMinPriorityQueue

/**
 * 最小生成树的Prim算法（即时版本）
 */
class PrimMST(graph: EWG) : MST() {
    private val marked = BooleanArray(graph.V)
    // 书上用distTo的值作为排序依据，这里使用边的权重作为排序依据，过程不同，但结果相同
    private val indexMinPQ = HeapIndexMinPriorityQueue<Edge>(graph.V)

    init {
        visit(graph, 0)
        while (!indexMinPQ.isEmpty()) {
            // indexMinPQ的大小永远小于graph.V
            val edge = indexMinPQ.delMin().first
            queue.enqueue(edge)
            weight += edge.weight
            val v = edge.either()
            val w = edge.other(v)
            if (!marked[v]) visit(graph, v)
            if (!marked[w]) visit(graph, w)
        }
        check(queue.size() == graph.V - 1) { "All vertices should be connected." }
    }

    private fun visit(graph: EWG, v: Int) {
        marked[v] = true
        graph.adj(v).forEach {
            val w = it.other(v)
            if (!marked[w]) {
                if (!indexMinPQ.contains(w) || indexMinPQ[w]!!.weight > it.weight) {
                    indexMinPQ[w] = it
                }
            }
        }
    }
}

fun main() {
    val graph = getTinyWeightedGraph()
    val primMST = PrimMST(graph)
    println(primMST.toString())

    drawRandomEWG { PrimMST(it) }
}