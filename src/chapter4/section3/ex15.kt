package chapter4.section3

import edu.princeton.cs.algs4.Queue
import extensions.formatDouble

/**
 * 给定一幅加权图G以及它的最小生成树。
 * 向G中添加一条边e，如何在与V成正比的时间内找到新图的最小生成树。
 *
 * 解：由树的性质可以得知，用一条边连接树中的任意两个顶点都会产生一个新的环
 * 使用广度优先搜索找到环的所有边，去除环中权重最大的边就是新的最小生成树
 * 遍历旧的最小生成树，构造一个邻接表，再将边e加入邻接表中，时间复杂度O(V)
 * 以e的一个顶点开始使用广度（或深度）优先搜索找到一个环，时间复杂度O(V)
 * 遍历环中所有的边，删除权重最大的边，剩余的边构造成新的最小生成树
 */
fun ex15(graph: EWG, mst: MST, e: Edge): Iterable<Edge> {
    val tree = EdgeWeightedGraph(graph.V)
    mst.edges().forEach {
        tree.addEdge(it)
    }
    tree.addEdge(e)

    val cycle = EdgeWeightedGraphCycle(tree).cycle()?.iterator()
    if (cycle == null || !cycle.hasNext()) return tree.edges()
    var maxEdge = cycle.next()
    while (cycle.hasNext()) {
        val edge = cycle.next()
        if (edge > maxEdge) {
            maxEdge = edge
        }
    }

    val result = Queue<Edge>()
    tree.edges().forEach {
        if (it != maxEdge) result.enqueue(it)
    }
    return result
}

fun main() {
    val graph = getTinyWeightedGraph()
    val mst = PrimMST(graph)
    println(mst)

    // 1-7-0-2-6-1 组成一个环，2-6权重最大被删除，最小生成树总权重减小
    val e1 = Edge(1, 6, 0.2)
    val result1 = ex15(graph, mst, e1)
    var weight1 = 0.0
    result1.forEach {
        weight1 += it.weight
    }
    println("weight1=${formatDouble(weight1, 2)}")
    println(result1.joinToString(separator = "\n") { it.toString() })
    println()

    // 1-7-0-2-6-1 组成一个环，新加入的1-6权重最大被删除，最小生成树不变
    val e2 = Edge(1, 6, 0.5)
    val result2 = ex15(graph, mst, e2)
    var weight2 = 0.0
    result2.forEach {
        weight2 += it.weight
    }
    println("weight2=${formatDouble(weight2, 2)}")
    println(result2.joinToString(separator = "\n") { it.toString() })
}