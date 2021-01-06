package chapter4.section3

import chapter1.section5.CompressionWeightedQuickUnionUF
import chapter2.section4.HeapMaxPriorityQueue

/**
 * 如何得到一幅加权图的最大生成树？
 *
 * 解：LazyPrim算法将最小优先队列替换为最大优先队列
 * Prim算法将最小索引优先队列替换为最大索引优先队列
 * Kruskal算法将升序排序改为降序排序
 * 这里只演示Kruskal算法的最大生成树
 */
class MaxKruskalMST(graph: EWG) : MST() {

    init {
        val uf = CompressionWeightedQuickUnionUF(graph.V)
        val pq = HeapMaxPriorityQueue<Edge>()
        graph.edges().forEach {
            pq.insert(it)
        }
        while (!pq.isEmpty() && queue.size() < graph.V - 1) {
            val edge = pq.delMax()
            val v = edge.either()
            val w = edge.other(v)
            if (uf.connected(v, w)) continue
            queue.enqueue(edge)
            weight += edge.weight
            uf.union(v, w)
        }
        check(queue.size() == graph.V - 1) { "All vertices should be connected." }
    }
}

fun main() {
    val graph = getTinyWeightedGraph()
    val maxKruskalMST = MaxKruskalMST(graph)
    println(maxKruskalMST.toString())
    drawEWGGraph(graph) { MaxKruskalMST(it) }
}