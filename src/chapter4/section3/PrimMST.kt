package chapter4.section3

import chapter2.section4.HeapIndexMinPriorityQueue
import edu.princeton.cs.algs4.Queue
import extensions.formatDouble

/**
 * 最小生成树的Prim算法（即时版本）
 */
class PrimMST(graph: EWG) : MST(graph) {
    private val marked = BooleanArray(graph.V)
    private val queue = Queue<Edge>()
    private val indexMinPQ = HeapIndexMinPriorityQueue<Edge>(graph.V)
    private var weight = 0.0

    init {
        visit(graph, 0)
        while (!indexMinPQ.isEmpty()) {
            // indexMinPQ的大小永远小于graph.V
            val edge = indexMinPQ.min()
            queue.enqueue(edge)
            weight += edge.weight
            indexMinPQ.delMin()
            val v = edge.either()
            val w = edge.other(v)
            if (!marked[v]) visit(graph, v)
            if (!marked[w]) visit(graph, w)
        }
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

    override fun edges(): Iterable<Edge> {
        return queue
    }

    override fun weight(): Double {
        return weight
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
                .append("weight=")
                .append(formatDouble(weight, 2))
                .append("\n")
        queue.forEach {
            stringBuilder.append(it.toString())
                    .append("\n")
        }
        return stringBuilder.toString()
    }
}

fun main() {
    val graph = getTinyWeightedGraph()
    val primMST = PrimMST(graph)
    println(primMST.toString())

    drawRandomEWG { PrimMST(it) }
}