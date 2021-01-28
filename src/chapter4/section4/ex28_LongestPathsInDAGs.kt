package chapter4.section4

import chapter2.section4.HeapIndexMinPriorityQueue
import edu.princeton.cs.algs4.Stack

/**
 * 有向无环图中的最长路径
 * 重新实现AcyclicLP类，根据命题T解决加权有向无环图中的最长路径问题
 *
 * 解：命题T的证明：
 * 给定一个最长路径，复制原始无环加权有向图得到一个副本并将副本中的所有边的权重取相反数。
 * 这样，副本中的最短路径即为原图中的最长路径。
 * 要将最短路径问题的答案转换为最长路径问题的答案，只需将方案中的权重变为正值即可。
 */
class AcyclicLP2(digraph: EdgeWeightedDigraph, s: Int) : SP(digraph, s) {
    private val pq = HeapIndexMinPriorityQueue<Double>(digraph.V)

    init {
        val newDigraph = EdgeWeightedDigraph(digraph.V)
        digraph.edges().forEach {
            newDigraph.addEdge(DirectedEdge(it.from(), it.to(), -1 * it.weight))
        }
        // 所有的权重都乘以-1，包括起点到它自身的距离
        distTo[s] = -0.0
        pq[s] = -0.0
        while (!pq.isEmpty()) {
            relax(newDigraph, pq.delMin().second)
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

    override fun distTo(v: Int): Double {
        return if (distTo[v] == Double.POSITIVE_INFINITY) {
            Double.POSITIVE_INFINITY
        } else {
            distTo[v] * -1
        }
    }

    override fun hasPathTo(v: Int): Boolean {
        return distTo[v] != Double.POSITIVE_INFINITY
    }

    override fun pathTo(v: Int): Iterable<DirectedEdge>? {
        if (!hasPathTo(v)) return null
        val stack = Stack<DirectedEdge>()
        var edge = edgeTo[v]
        while (edge != null) {
            stack.push(DirectedEdge(edge.from(), edge.to(), edge.weight * -1))
            edge = edgeTo[edge.from()]
        }
        return stack
    }
}

fun main() {
    val digraph = getTinyEWDAG()
    println(AcyclicLP(digraph, 5))
    println(AcyclicLP2(digraph, 5))
}