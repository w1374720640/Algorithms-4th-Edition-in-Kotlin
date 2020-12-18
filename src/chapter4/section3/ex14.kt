package chapter4.section3

import chapter1.section5.CompressionWeightedQuickUnionUF
import chapter2.sleep
import edu.princeton.cs.algs4.StdDraw
import extensions.random

/**
 * 给定一幅加权图G以及它的最小生成树。
 * 从G中删去一条边且G仍然是连通的，如何在与E成正比的时间内找到新图的最小生成树。
 *
 * 解：最小生成树包含所有顶点，且无环连通，删去一条边后，图G必定被分为两个不连通的分量，
 * 在连接两个连通分量的边中找到权重最小的边，即可组成新图的最小生成树。
 * 遍历树的剩余边，使用[chapter1.section5.CompressionWeightedQuickUnionUF]获取每个顶点的id，时间复杂度接近O(V)
 * 遍历图G的所有边，找到同时连接两个连通分量、权重最小的边，组成新的最小生成树，时间复杂度接近O(E)
 */
fun ex14(graph: EdgeWeightedGraph, tree: Iterable<Edge>): Pair<Edge, Edge?> {
    require(graph.V > 1)
    val deleteIndex = random(tree.count())
    var deleteEdge: Edge? = null
    val uf = CompressionWeightedQuickUnionUF(graph.V)
    tree.forEachIndexed { index, edge ->
        if (index == deleteIndex) {
            deleteEdge = edge
        } else {
            val v = edge.either()
            val w = edge.other(v)
            uf.union(v, w)
        }
    }
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
    // 返回删除的边和新的边，删除的边必定不为空，新的边可能为空
    return deleteEdge!! to minEdge
}

fun main() {
    val delay = 2000L
    // 随机生成一个边长和权重相等的随机图
    val randomGraph = getRandomEWG()
    var mst: MST? = null
    drawEWGGraph(randomGraph.first, points = randomGraph.second, showIndex = false, delay = 0) {
        mst = PrimMST(it)
        mst!!
    }
    sleep(delay)
    check(mst != null)
    val result = ex14(randomGraph.first, mst!!.edges())

    // 将删除的边绘制为红色
    StdDraw.setPenRadius(EWGGraphics.DEFAULT_EDGE_WIDTH * 2)
    StdDraw.setPenColor(StdDraw.RED)
    val deleteV = result.first.either()
    val deleteW = result.first.other(deleteV)
    val deletePointV = randomGraph.second[deleteV]
    val deletePointW = randomGraph.second[deleteW]
    StdDraw.line(deletePointV.x(), deletePointV.y(), deletePointW.x(), deletePointW.y())
    sleep(delay)

    // 将新添加的边绘制为绿色
    result.second?.let {
        StdDraw.setPenRadius(EWGGraphics.DEFAULT_EDGE_WIDTH * 2)
        StdDraw.setPenColor(StdDraw.GREEN)
        val newV = it.either()
        val newW = it.other(newV)
        val newPointV = randomGraph.second[newV]
        val newPointW = randomGraph.second[newW]
        StdDraw.line(newPointV.x(), newPointV.y(), newPointW.x(), newPointW.y())
    }
}