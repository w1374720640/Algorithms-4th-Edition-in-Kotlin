package chapter4.section3

import chapter1.section5.CompressionWeightedQuickUnionUF
import edu.princeton.cs.algs4.Stack

/**
 * 关键边
 * 关键边指的是图的最小生成树中的某一条边，如果删除它，新图的最小生成树的总权重将会大于原最小生成树的总权重。
 * 找到在ElogE时间内找出图的关键边的算法。
 * 注意：这个问题中边的权重并不一定各不相同（否则最小生成树中的所有边都是关键边）
 *
 * 解：如果所有边的权重各不相同，则最小生成树中的所有边都是关键边，
 * 如果边的权重可以相同，则在使用Prim算法或Kruskal算法求最小生成树的过程中，
 * 如果从优先队列或排序后的数组中取出的最小边不止一个（有多个大小相同的横切边），
 * 则这个边不是关键边，否则这个边是关键边。
 * 这里以Kruskal算法为例，在计算最小生成树的过程中过滤大小相同的横切边
 */
fun ex26_CriticalEdges(graph: EWG): Iterable<Edge> {
    val edges = ArrayList<Edge>()
    val uf = CompressionWeightedQuickUnionUF(graph.V)
    val stack = Stack<Edge>()
    var count = 0

    graph.edges().forEach {
        edges.add(it)
    }
    edges.sort()
    var i = 0
    var lastEdge: Edge? = null
    while (count < graph.V - 1) {
        val edge = edges[i++]
        val v = edge.either()
        val w = edge.other(v)
        if (uf.find(v) != uf.find(w)) {
            uf.union(v, w)
            count++
            if (lastEdge == null) {
                stack.push(edge)
            } else if (edge.compareTo(lastEdge) == 0) {
                if (!stack.isEmpty && stack.peek().compareTo(lastEdge) == 0) {
                    stack.pop()
                }
            } else {
                stack.push(edge)
            }
            lastEdge = edge
        }
    }
    return stack
}

private fun getCriticalGrape(): EWG {
    val graph = EdgeWeightedGraph(8)
    graph.addEdge(Edge(4, 5, 0.35)) // 关键边
    graph.addEdge(Edge(4, 7, 0.37))
    graph.addEdge(Edge(5, 7, 0.28)) // A组
    graph.addEdge(Edge(0, 7, 0.28)) // A组
    graph.addEdge(Edge(1, 5, 0.32))
    graph.addEdge(Edge(0, 4, 0.38))
    graph.addEdge(Edge(2, 3, 0.26)) // B组
    graph.addEdge(Edge(1, 7, 0.28)) // A组
    graph.addEdge(Edge(0, 2, 0.26)) // B组
    graph.addEdge(Edge(1, 2, 0.36))
    graph.addEdge(Edge(1, 3, 0.29))
    graph.addEdge(Edge(2, 7, 0.34))
    graph.addEdge(Edge(6, 2, 0.40)) // 关键边
    graph.addEdge(Edge(3, 6, 0.52))
    graph.addEdge(Edge(6, 0, 0.58))
    graph.addEdge(Edge(6, 4, 0.93))
    return graph
}

fun main() {
    val graph1 = getTinyWeightedGraph()
    val iterable1 = ex26_CriticalEdges(graph1)
    println("size=${iterable1.count()}\n${iterable1.joinToString(separator = "\n")}")
    println()

    val graph2 = getCriticalGrape()
    val iterable2 = ex26_CriticalEdges(graph2)
    println("size=${iterable2.count()}\n${iterable2.joinToString(separator = "\n")}")
}