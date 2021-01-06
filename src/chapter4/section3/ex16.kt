package chapter4.section3

import extensions.formatDouble

/**
 * 给定一幅加权图G以及它的最小生成树。
 * 向G中添加一条边e，编写一段程序找到e的权重在什么范围内才会被加入最小生成树。
 *
 * 由练习4.3.15可知，e的顶点不同产生的环不同，当e的权重不是环内的最大值时才会被加入最小生成树。
 * 将e加入最小生成树中，再找到新树中的环，e的权重需要小于环中权重最大的边（假设权重为非负数）
 */
fun ex16(graph: EWG, mst: MST, e: Edge): Pair<Double, Double> {
    val tree = EdgeWeightedGraph(graph.V)
    mst.edges().forEach {
        tree.addEdge(it)
    }
    tree.addEdge(e)

    val cycle = EdgeWeightedGraphCycle(tree).cycle()?.iterator()
    check(cycle != null && cycle.hasNext())
    var maxEdge = cycle.next()
    while (cycle.hasNext()) {
        val edge = cycle.next()
        if (edge > maxEdge) {
            maxEdge = edge
        }
    }
    return 0.0 to maxEdge.weight
}

fun main() {
    val graph = getTinyWeightedGraph()
    val mst = PrimMST(graph)
    println(mst)

    val result = ex16(graph, mst, Edge(0, 6, 0.0))
    println("[${formatDouble(result.first, 2)}, ${formatDouble(result.second, 2)}]")
}