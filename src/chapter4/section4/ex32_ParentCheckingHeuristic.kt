package chapter4.section4

import edu.princeton.cs.algs4.Queue

/**
 * 启发式的父结点检查
 * 修改Bellman-Ford算法，仅当顶点v在最短路径树中的父结点edgeTo[v]目前不在队列中时才访问v。
 * Cherkassky、Goldberg和Radzik在实践中发现这种启发式的做法十分有帮助。
 * 证明这种方法能够正确的计算出最短路径且在最坏情况下的运行时间和EV成正比。
 */
class BellmanFordParentCheckingSP(digraph: EdgeWeightedDigraph, s: Int) : SP(digraph, s) {
    private val onQueue = BooleanArray(digraph.V)
    private val queue = Queue<Int>()
    private var cost = 0
    private var cycle: Iterable<DirectedEdge>? = null

    init {
        distTo[s] = 0.0
        queue.enqueue(s)
        onQueue[s] = true
        while (!queue.isEmpty && !hasNegativeCycle()) {
            val v = queue.dequeue()
            onQueue[v] = false
            val edge = edgeTo[v]
            // 和BellmanFordSP算法的唯一区别在这里
            if (edge == null || !onQueue[edge.from()]) {
                relax(v)
            }
        }
    }

    private fun relax(v: Int) {
        digraph.adj(v).forEach { edge ->
            val w = edge.to()
            if (distTo[w] > distTo[v] + edge.weight) {
                distTo[w] = distTo[v] + edge.weight
                edgeTo[w] = edge
                if (!onQueue[w]) {
                    queue.enqueue(w)
                    onQueue[w] = true
                }
            }
        }
        if (++cost % digraph.V == 0) {
            findNegativeCycle()
        }
    }

    private fun findNegativeCycle() {
        val newDigraph = EdgeWeightedDigraph(digraph.V)
        edgeTo.forEach { edge ->
            if (edge != null) {
                newDigraph.addEdge(edge)
            }
        }
        // 最短路径组成的加权有向图有环时，说明这个环是负权重环
        val cycleFinder = EdgeWeightedCycleFinder(newDigraph)
        if (cycleFinder.hasCycle()) {
            cycle = cycleFinder.cycle()
        }
    }

    fun hasNegativeCycle() = cycle != null

    fun negativeCycle() = cycle

    override fun distTo(v: Int): Double {
        if (hasNegativeCycle()) return Double.POSITIVE_INFINITY
        return super.distTo(v)
    }

    override fun hasPathTo(v: Int): Boolean {
        if (hasNegativeCycle()) return false
        return super.hasPathTo(v)
    }

    override fun pathTo(v: Int): Iterable<DirectedEdge>? {
        if (hasNegativeCycle()) return null
        return super.pathTo(v)
    }

    override fun toString(): String {
        return if (hasNegativeCycle()) {
            "has negative cycle: ${negativeCycle()!!.joinToString()}"
        } else {
            super.toString()
        }
    }
}

fun main() {
    val digraph = getTinyEWDn()
    val sp1 = BellmanFordSP(digraph, 5)
    println(sp1.toString())
    val sp2 = BellmanFordParentCheckingSP(digraph, 5)
    println(sp2.toString())
}