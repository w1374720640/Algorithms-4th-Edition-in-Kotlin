package chapter4.section3

import edu.princeton.cs.algs4.In

/**
 * 从tinyEWG.txt中（请见图4.3.1）删去顶点7并给出加权图的最小生成树
 */
fun ex6(): EdgeWeightedGraph {
    val input = In("./data/tinyEWG.txt")
    val graph = EdgeWeightedGraph(7)
    input.readInt()
    repeat(input.readInt()) {
        val v = input.readInt()
        val w = input.readInt()
        val weight = input.readDouble()
        if (v != 7 && w != 7) {
            graph.addEdge(Edge(v, w, weight))
        }
    }
    return graph
}

fun main() {
    val mst = PrimMST(ex6())
    println(mst)
}