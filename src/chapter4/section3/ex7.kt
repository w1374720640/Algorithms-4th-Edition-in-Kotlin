package chapter4.section3

import chapter1.section5.CompressionWeightedQuickUnionUF
import edu.princeton.cs.algs4.Queue
import extensions.formatDouble

/**
 * 如何得到一幅加权图的最大生成树？
 *
 * 解：LazyPrim算法将最小优先队列替换为最大优先队列
 * Prim算法将最小索引优先队列替换为最大索引优先队列
 * Kruskal算法将升序排序改为降序排序
 * 这里只演示Kruskal算法的最大生成树
 */
class MaxKruskalMST(graph: EWG) : MST {
    private val edges = ArrayList<Edge>()
    private val uf = CompressionWeightedQuickUnionUF(graph.V)
    private val queue = Queue<Edge>()
    private var weight = 0.0

    init {
        graph.edges().forEach {
            edges.add(it)
        }
        // 直接排序，而不是使用优先队列
        edges.sortDescending()
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
    val maxKruskalMST = MaxKruskalMST(graph)
    println(maxKruskalMST.toString())
    drawEWGGraph(graph) { MaxKruskalMST(it) }
}