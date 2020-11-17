package chapter4.section1

import edu.princeton.cs.algs4.In
import edu.princeton.cs.algs4.Queue
import extensions.readInt

class BreadFirstSearch(graph: Graph, s: Int) : Search(graph, s) {
    init {
        require(s in 0 until graph.V)
    }

    private val marked = BooleanArray(graph.V)
    private var count = 0

    init {
        val queue = Queue<Int>()
        queue.enqueue(s)
        while (!queue.isEmpty) {
            val v = queue.dequeue()
            marked[v] = true
            count++
            graph.adj(v).forEach {
                if (!marked[it]) {
                    queue.enqueue(it)
                }
            }
        }
    }

    override fun marked(v: Int): Boolean {
        return marked[v]
    }

    override fun count(): Int {
        return count
    }
}

fun main() {
    val path = "./data/tinyG.txt"
    val graph = Graph(In(path))
    val s = readInt("search with: ")
    val search = BreadFirstSearch(graph, s)
    for (i in 0 until graph.V) {
        if (search.marked(i)) {
            print("$i ")
        }
    }
    println()
    println("${if (search.count() == graph.V) "" else "NOT"} connected")
}