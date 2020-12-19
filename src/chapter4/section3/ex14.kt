package chapter4.section3

import chapter1.section5.CompressionWeightedQuickUnionUF
import edu.princeton.cs.algs4.Queue
import extensions.formatDouble

/**
 * 给定一幅加权图G以及它的最小生成树。
 * 从G中删去一条边且G仍然是连通的，如何在与E成正比的时间内找到新图的最小生成树。
 *
 * 解：因为是从图G中删去一条边，若边不在最小生成树中，删除该边对最小生成树无影响，
 * 若删除的边在最小生成树中，删去一条边后，会得到两颗独立的树，
 * 在连接两棵树的边中找到权重最小的边，即可组成新图的最小生成树。
 * 遍历树的剩余边，使用[chapter1.section5.CompressionWeightedQuickUnionUF]获取每个顶点的id，时间复杂度接近O(V)
 * 遍历图G的所有边，找到同时连接两个连通分量、权重最小的边，组成新的最小生成树，时间复杂度接近O(E)
 */
fun ex14(graph: EdgeWeightedGraph, mst: MST, deleteEdge: Edge): Iterable<Edge> {
    require(graph.V > 1)

    val uf = CompressionWeightedQuickUnionUF(graph.V)
    var containEdge = false
    val edges = mst.edges()
    edges.forEach { edge ->
        if (edge == deleteEdge) {
            containEdge = true
        } else {
            val v = edge.either()
            val w = edge.other(v)
            uf.union(v, w)
        }
    }
    if (!containEdge) return edges

    var minWeight = Double.MAX_VALUE
    var minEdge: Edge? = null
    graph.edges().forEach {
        val v = it.either()
        val w = it.other(v)
        if (uf.find(v) != uf.find(w) && it != deleteEdge) {
            if (it.weight < minWeight) {
                minEdge = it
                minWeight = it.weight
            }
        }
    }
    val result = Queue<Edge>()
    edges.forEach { if (it != deleteEdge) result.enqueue(it) }
    result.enqueue(minEdge)
    return result
}

fun main() {
    val graph = getTinyWeightedGraph()
    val mst = PrimMST(graph)
    println(mst)

    // 删除边0-7，添加边1-3，树的权重增加
    var deleteEdge: Edge? = null
    graph.adj(0).forEach {
        if (it.other(0) == 7) {
            deleteEdge = it
        }
    }
    val result = ex14(graph, mst, deleteEdge!!)
    var weight = 0.0
    result.forEach {
        weight += it.weight
    }
    println("weight=${formatDouble(weight, 2)}")
    println(result.joinToString(separator = "\n") { it.toString() })
}