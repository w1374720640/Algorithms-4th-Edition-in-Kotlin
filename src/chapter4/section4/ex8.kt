package chapter4.section4

import extensions.formatDouble

/**
 * 一幅有向图的直径指的是连接任意两个顶点的所有最短路径中的最大长度。
 * 编写一个DijkstraSP的用例，找出边的权重非负的给定EdgeWeightedDigraph图的直径。
 */
fun ex8(digraph: EdgeWeightedDigraph): Pair<Double, Iterable<DirectedEdge>?> {
    var diameter = Double.NEGATIVE_INFINITY
    var path: Iterable<DirectedEdge>? = null
    for (s in 0 until digraph.V) {
        val sp = DijkstraSP(digraph, s)
        for (v in 0 until digraph.V) {
            val dist = sp.distTo(v)
            if (dist != Double.POSITIVE_INFINITY && dist > diameter) {
                diameter = dist
                path = sp.pathTo(v)
            }
        }
    }
    return diameter to path
}

fun main() {
    val digraph = getTinyEWD()
    val result = ex8(digraph)
    if (result.first != Double.NEGATIVE_INFINITY) {
        println("diameter=${formatDouble(result.first, 2)}")
        println("path=${result.second!!.joinToString(separator = " ")}")
    }
}