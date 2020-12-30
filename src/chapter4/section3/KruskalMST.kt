package chapter4.section3

import chapter1.section5.CompressionWeightedQuickUnionUF
import chapter2.section4.HeapMinPriorityQueue
import edu.princeton.cs.algs4.Queue
import extensions.formatDouble

/**
 * 最小生成树的Kruskal算法
 */
class KruskalMST(graph: EWG) : MST {
    private val queue = Queue<Edge>()
    private var weight = 0.0

    init {
        val uf = CompressionWeightedQuickUnionUF(graph.V)
        val pq = HeapMinPriorityQueue<Edge>()
        graph.edges().forEach {
            pq.insert(it)
        }
        while (!pq.isEmpty() && queue.size() < graph.V - 1) {
            val edge = pq.delMin()
            val v = edge.either()
            val w = edge.other(v)
            if (uf.connected(v, w)) continue
            queue.enqueue(edge)
            weight += edge.weight
            uf.union(v, w)
        }
        check(queue.size() == graph.V - 1) { "All vertices should be connected." }
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
    val kruskalMST = KruskalMST(graph)
    println(kruskalMST.toString())

    drawRandomEWG { KruskalMST(it) }
}