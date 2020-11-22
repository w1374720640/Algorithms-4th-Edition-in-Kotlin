package chapter4.section1

import edu.princeton.cs.algs4.Stack

/**
 * 使用栈（而不是递归）实现的深度优先路径搜索
 */
class StackDepthFirstPaths(graph: Graph, val s: Int) : Paths(graph, s) {
    init {
        require(s in 0 until graph.V)
    }

    private val marked = BooleanArray(graph.V)
    private val edgeTo = IntArray(graph.V)
    private val distTo = IntArray(graph.V) { -1 }

    init {
        val stack = Stack<Int>()
        stack.push(s)
        marked[s] = true
        edgeTo[s] = s
        distTo[s] = 0
        while (!stack.isEmpty) {
            val v = stack.pop()
            graph.adj(v).forEach { w ->
                if (!marked[w]) {
                    marked[w] = true
                    edgeTo[w] = v
                    distTo[w] = distTo[v] + 1
                    stack.push(w)
                }
            }
        }
    }

    override fun hasPathTo(v: Int): Boolean {
        return marked[v]
    }

    override fun pathTo(v: Int): Iterable<Int>? {
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