package chapter4.section3

import edu.princeton.cs.algs4.Bag
import edu.princeton.cs.algs4.Queue
import extensions.formatDouble

/**
 * 给定一幅加权图G以及它的最小生成树。
 * 向G中添加一条边e，编写一段程序找到e的权重在什么范围内才会被加入最小生成树。
 *
 * 由练习4.3.15可知，e的顶点不同产生的环不同，当e的权重不是环内的最大值时才会被加入最小生成树。
 * 通过广度优先搜索找到e的两个顶点间的最短路径，e的权重需要小于权重最大的边（假设权重为非负数）
 */
fun ex16(graph: EWG, mst: MST, eV: Int, eW: Int): Pair<Double, Double> {
    val adj = Array(graph.V) { Bag<Edge>() }
    mst.edges().forEach {
        val v = it.either()
        val w = it.other(v)
        adj[v].add(it)
        adj[w].add(it)
    }

    val marked = BooleanArray(graph.V)
    val pathTo = Array<Edge?>(graph.V) { null }
    val queue = Queue<Edge>()
    // 假设权重为非负数
    val e = Edge(eV, eW, 0.0)
    queue.enqueue(e)
    marked[eV] = true
    while (!queue.isEmpty) {
        val edge = queue.dequeue()
        val v = edge.either()
        val w = edge.other(v)
        if (marked[v] && marked[w]) {
            pathTo[eV] = edge
            break
        }
        val notMarked = if (marked[v]) w else v
        marked[notMarked] = true
        pathTo[notMarked] = edge
        adj[notMarked].forEach {
            if (it != edge) queue.enqueue(it)
        }
    }

    var maxEdge = e
    var lastVertex = eV
    while (true) {
        val edge = pathTo[lastVertex]
        check(edge != null)
        if (edge.weight > maxEdge.weight) {
            maxEdge = edge
        }
        lastVertex = edge.other(lastVertex)
        if (lastVertex == eV) break
    }
    return 0.0 to maxEdge.weight
}

fun main() {
    val graph = getTinyWeightedGraph()
    val mst = PrimMST(graph)
    println(mst)

    val result = ex16(graph, mst, 0, 6)
    println("[${formatDouble(result.first, 2)}, ${formatDouble(result.second, 2)}]")
}