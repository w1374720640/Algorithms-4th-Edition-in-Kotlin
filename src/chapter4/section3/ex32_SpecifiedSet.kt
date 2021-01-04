package chapter4.section3

import chapter1.section5.CompressionWeightedQuickUnionUF
import chapter3.section5.LinearProbingHashSET
import chapter3.section5.SET

/**
 * 指定的集合
 * 给定一幅连通的加权图G和一个边的集合S（不含环），给出一种算法得到含有S中所有边的最小加权生成树
 *
 * 解：使用Kruskal算法计算最小生成树时，先将集合S内的所有边都加入到最小生成树中，然后再执行正常逻辑
 */
class SpecifiedSetMST(graph: EWG, S: SET<Edge>) : MST() {
    private val edges = ArrayList<Edge>()
    private val uf = CompressionWeightedQuickUnionUF(graph.V)

    init {
        // 先将集合S中的所有边都加入最小生成树中
        S.forEach { edge ->
            val v = edge.either()
            val w = edge.other(v)
            queue.enqueue(edge)
            weight += edge.weight
            uf.union(v, w)
        }

        // 下面是正常逻辑
        graph.edges().forEach {
            edges.add(it)
        }
        edges.sort()
        var i = 0
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
}

fun main() {
    val graph = getTinyWeightedGraph()
    val S = LinearProbingHashSET<Edge>()
    S.add(Edge(4, 7, 0.37))
    S.add(Edge(0, 4, 0.38))
    S.add(Edge(3, 6, 0.52))
    val mst = SpecifiedSetMST(graph, S)
    println(mst.toString())
}
