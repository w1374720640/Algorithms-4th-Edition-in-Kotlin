package chapter4.section3

import chapter1.section5.CompressionWeightedQuickUnionUF
import edu.princeton.cs.algs4.Queue
import extensions.formatDouble

/**
 * 最小生成树的Kruskal算法
 */
class KruskalMST(graph: EdgeWeightedGraph) : MST(graph) {
    private val edges = ArrayList<Edge>()
    private val uf = CompressionWeightedQuickUnionUF(graph.V)
    private val queue = Queue<Edge>()
    private var weight = 0.0

    init {
        graph.edges().forEach {
            edges.add(it)
        }
        // 直接排序，而不是使用优先队列
        edges.sort()
        var i = 0
        // 最小生成树最多有V-1条边
        while (queue.size() < graph.V - 1) {
            val edge = edges[i++]
            val v = edge.either()
            val w = edge.other(v)
            if (uf.find(v) != uf.find(w)) {
                queue.enqueue(edge)
                weight += edge.weight
                uf.union(v, w)
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
    val kruskalMST = KruskalMST(graph)
    println(kruskalMST.toString())
}