package chapter4.section4

import chapter2.section4.HeapIndexMinPriorityQueue
import edu.princeton.cs.algs4.Stack

/**
 * 多起点最短路径
 * 设计并实现一份API，使用Dijkstra算法解决加权有向图中的多起点最短路径问题，其中边的权重均为正：
 * 给定一组起点，找到相应的最短路径森林并实现一个方法为用例返回从任意起点到达每个顶点的最短路径。
 * 提示：添加一个伪顶点和从该顶点指向每个起点的一条权重为零的边，或者在初始化时将所有起点加入优先队列
 * 并将它们在distTo[]中对应的值均设为0。
 *
 * 解：根据提示，使用第二种方法
 */
class MultisourceShortestPaths(digraph: EdgeWeightedDigraph, startingPoints: Array<Int>) {
    private val distTo = Array(digraph.V) { Double.POSITIVE_INFINITY }
    private val edgeTo = arrayOfNulls<DirectedEdge>(digraph.V)
    private val pq = HeapIndexMinPriorityQueue<Double>(digraph.V)

    init {
        startingPoints.forEach {
            distTo[it] = 0.0
            pq[it] = 0.0
        }
        while (!pq.isEmpty()) {
            relax(digraph, pq.delMin().second)
        }
    }

    private fun relax(digraph: EdgeWeightedDigraph, v: Int) {
        digraph.adj(v).forEach { edge ->
            val w = edge.to()
            if (distTo[w] > distTo[v] + edge.weight) {
                distTo[w] = distTo[v] + edge.weight
                edgeTo[w] = edge
                pq[w] = distTo[w]
            }
        }
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
     * 从任意起点到v的最短路径，如果不存在则为null
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
    val digraph = getTinyEWD()
    val sp = MultisourceShortestPaths(digraph, arrayOf(1, 2))
    println(sp.pathTo(4)?.joinToString())
    println(sp.pathTo(6)?.joinToString())
}