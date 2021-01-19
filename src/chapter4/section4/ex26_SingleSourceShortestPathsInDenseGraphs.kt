package chapter4.section4

import edu.princeton.cs.algs4.In
import edu.princeton.cs.algs4.Stack
import extensions.random

/**
 * 稠密图中的单点最短路径
 * 实现另一个版本的Dijkstra算法，使之能够在与V²成正比的时间内在一幅稠密的加权有向图中计算出给定顶点的最短路径树。
 * 请使用邻接矩阵法表示稠密图（请参考练习4.4.3和练习4.3.29）
 *
 * 解：创建一个大小为V的marked数组，用于记录已经放松完成的顶点，
 * 从起点开始遍历所有顶点，对每个顶点进行放松，
 * 一个顶点放松后，遍历所有顶点，找到未完成放松且distTo权重最小的顶点，该顶点就是下一个需要放松的顶点。
 * 遍历所有顶点时间复杂度O(V)，放松一个顶点O(V)，找到下一个要放松的顶点O(V)
 * 总时间复杂度为O(V)*(O(V)+O(V))=O(V²)
 */
class DenseGraphsSP(digraph: AdjacencyMatrixEWD, s: Int) {
    private val distTo = Array(digraph.V) { Double.POSITIVE_INFINITY }
    private val edgeTo = arrayOfNulls<DirectedEdge>(digraph.V)

    init {
        val marked = BooleanArray(digraph.V)
        distTo[s] = 0.0
        var v = s
        repeat(digraph.V) {
            check(v != -1)
            v = relax(digraph, v, marked)
        }
    }

    private fun relax(digraph: AdjacencyMatrixEWD, v: Int, marked: BooleanArray): Int {
        marked[v] = true
        digraph.adj(v).forEach { edge ->
            val w = edge.to()
            if (distTo[w] > distTo[v] + edge.weight) {
                distTo[w] = distTo[v] + edge.weight
                edgeTo[w] = edge
            }
        }
        var minVertex = -1
        var minWeight = Double.POSITIVE_INFINITY
        for (i in 0 until digraph.V) {
            if (!marked[i] && distTo[i] < minWeight) {
                minVertex = i
                minWeight = distTo[i]
            }
        }
        return minVertex
    }

    /**
     * 从顶点s到v的距离，如果不存在则路径为无穷大
     */
    fun distTo(v: Int): Double {
        return distTo[v]
    }

    /**
     * 是否存在从顶点s到v的路径
     */
    fun hasPathTo(v: Int): Boolean {
        return distTo[v] != Double.POSITIVE_INFINITY
    }

    /**
     * 从顶点s到v的最短路径，如果不存在则为null
     */
    fun pathTo(v: Int): Iterable<DirectedEdge>? {
        if (!hasPathTo(v)) return null
        val stack = Stack<DirectedEdge>()
        var edge = edgeTo[v]
        while (edge != null) {
            stack.push(edge)
            edge = edgeTo[edge.from()]
        }
        return stack
    }
}

fun main() {
    val tinyDigraph = AdjacencyMatrixEWD(In("./data/tinyEWD.txt"))
    val tinySP = DenseGraphsSP(tinyDigraph, 0)
    println(tinySP.pathTo(6)?.joinToString())

    val V = 100
    val denseDigraph = AdjacencyMatrixEWD(V)
    for (v in 0 until V) {
        for (w in v + 1 until V) {
            denseDigraph.addEdge(DirectedEdge(v, w, random()))
            denseDigraph.addEdge(DirectedEdge(w, v, random()))
        }
    }
    val denseSP = DenseGraphsSP(denseDigraph, 0)
    println(denseSP.pathTo(10)?.joinToString())
}