package chapter4.section2

import edu.princeton.cs.algs4.Stack

/**
 * 深度优先的单点有向路径
 */
class DepthFirstDirectedPaths(digraph: Digraph, val s: Int) {
    init {
        require(s in 0 until digraph.V)
    }

    private val marked = BooleanArray(digraph.V)
    private val edgeTo = IntArray(digraph.V)
    private val distTo = IntArray(digraph.V) { -1 }

    init {
        distTo[s] = 0
        dfs(digraph, s)
    }

    private fun dfs(graph: Digraph, v: Int) {
        marked[v] = true
        graph.adj(v).forEach {
            if (!marked[it]) {
                edgeTo[it] = v
                distTo[it] = distTo[v] + 1
                dfs(graph, it)
            }
        }
    }

    fun hasPathTo(v: Int): Boolean {
        return marked[v]
    }

    fun pathTo(v: Int): Iterable<Int>? {
        if (!hasPathTo(v)) return null
        val stack = Stack<Int>()
        var w = v
        while (w != s) {
            stack.push(w)
            w = edgeTo[w]
        }
        stack.push(s)
        return stack
    }

    fun distTo(v: Int): Int {
        return distTo[v]
    }
}