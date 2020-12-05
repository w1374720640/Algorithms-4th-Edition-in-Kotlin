package chapter4.section2

import edu.princeton.cs.algs4.Queue
import edu.princeton.cs.algs4.Stack

/**
 * 广度优先的单点最短有向路径
 */
class BreadthFirstDirectedPaths(digraph: Digraph, val s: Int) {
    init {
        require(s in 0 until digraph.V)
    }

    private val marked = BooleanArray(digraph.V)
    private val edgeTo = IntArray(digraph.V)
    private val distTo = IntArray(digraph.V) { -1 }

    init {
        val queue = Queue<Int>()
        marked[s] = true
        distTo[s] = 0
        queue.enqueue(s)
        while (!queue.isEmpty) {
            val v = queue.dequeue()
            digraph.adj(v).forEach { w ->
                if (!marked[w]) {
                    marked[w] = true
                    edgeTo[w] = v
                    distTo[w] = distTo[v] + 1
                    queue.enqueue(w)
                }
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