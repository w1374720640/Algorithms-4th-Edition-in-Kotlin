package chapter4.section4

import edu.princeton.cs.algs4.Queue

/**
 * 基于队列的Bellman-Ford算法
 *
 * 命题W：当且仅当加权有向图中至少存在一条从s到v的有向路径，
 * 且所有从s到v的有向路径上的任意顶点都不存在于任何负权重环中时，s到v的最短路径才是存在的。
 * 命题X（Bellman-Ford算法）：
 * 在任意含有V个顶点的加权有向图中给定起点s，从s无法到达任何负权重环，以下算法能够解决其中的单点最短路径问题：
 * 将distTo[s]初始化为0，其他distTo[]元素初始化为无穷大。以任意顺序放松有向图的所有边，重复V轮。
 * 命题W（续）：Bellman-Ford算法所需的时间和EV成正比，空间和V成正比。
 */
class BellmanFordSP(digraph: EdgeWeightedDigraph, s: Int) : SP(digraph, s) {
    private val onQueue = BooleanArray(digraph.V) // 判断顶点是否在队列中
    private val queue = Queue<Int>() // 正在被放松的队列
    private var cost = 0 // relax被调用的次数
    private var cycle: Iterable<DirectedEdge>? = null // 负权重环

    init {
        distTo[s] = 0.0
        queue.enqueue(s)
        onQueue[s] = true
        while (!queue.isEmpty && !hasNegativeCycle()) {
            val v = queue.dequeue()
            onQueue[v] = false
            relax(v)
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
        // 最坏情况下会调用V*V次relax方法（重复V轮，每轮调用V次）
        // 当调用V次（一轮）之后，edgeTo[]中包含所有的可达顶点，任何时刻edgeTo[]中的边组成的新图都不应该包含环
        // 为了提高效率，每调用V次之后检查一次是否存在环（也可以是其他频率），存在环则表示存在负权重环
        // 原文中说cost代表relax()被调用的次数，但原书中代码不一致，需要移动到循环外层，并且先自增再取余
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

    override fun toString(): String {
        return if (hasNegativeCycle()) {
            "has negative cycle: ${negativeCycle()!!.joinToString()}"
        } else {
            super.toString()
        }
    }
}

fun main() {
    // 含有负权重边但无负权重环
    val negativeDigraph = getTinyEWDn()
    val negativeSP = BellmanFordSP(negativeDigraph, 5)
    println(negativeSP.toString())

    // 含负权重环
    val negativeCycleDigraph = getTinyEWDnc()
    val negativeCycleSP = BellmanFordSP(negativeCycleDigraph, 6)
    println(negativeCycleSP.toString())
}