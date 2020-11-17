package chapter4.section1

import edu.princeton.cs.algs4.In
import edu.princeton.cs.algs4.Queue
import edu.princeton.cs.algs4.Stack
import extensions.readInt
import java.lang.StringBuilder

class BreadFirstPaths(graph: Graph, val s: Int): Paths(graph, s) {
    init {
        require(s in 0 until graph.V)
    }

    private val marked = BooleanArray(graph.V)
    private val edgeTo = IntArray(graph.V)

    init {
        val queue = Queue<Int>()
        queue.enqueue(s)
        while (!queue.isEmpty) {
            val v = queue.dequeue()
            marked[v] = true
            graph.adj(v).forEach {
                if (!marked[it]) {
                    edgeTo[it] = v
                    queue.enqueue(it)
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
}

fun main() {
    val path = "./data/tinyG.txt"
    val graph = Graph(In(path))
    val s = readInt("path with: ")
    val search = BreadFirstPaths(graph, s)
    for (v in 0 until graph.V) {
        val stringBuilder = StringBuilder()
                .append(s)
                .append(" to ")
                .append(v)
                .append(": ")
        if (search.hasPathTo(v)) {
            search.pathTo(v)!!.forEach {
                stringBuilder.append(if (it == s) it.toString() else "-$it")
            }
        }
        println(stringBuilder.toString())
    }
}