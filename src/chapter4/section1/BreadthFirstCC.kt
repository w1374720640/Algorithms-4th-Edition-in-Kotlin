package chapter4.section1

import edu.princeton.cs.algs4.Bag
import edu.princeton.cs.algs4.In
import edu.princeton.cs.algs4.Queue

class BreadthFirstCC(graph: Graph) : CC(graph) {
    private var count = 0
    private val ids = IntArray(graph.V)
    private val marked = BooleanArray(graph.V)

    init {
        val queue = Queue<Int>()
        for (i in 0 until graph.V) {
            if (!marked[i]) {
                marked[i] = true
                ids[i] = count
                queue.enqueue(i)
                while (!queue.isEmpty) {
                    val v = queue.dequeue()
                    graph.adj(v).forEach { w ->
                        if (!marked[w]) {
                            marked[w] = true
                            ids[w] = count
                            queue.enqueue(w)
                        }
                    }
                }
                count++
            }
        }
    }

    override fun connected(v: Int, w: Int): Boolean {
        return ids[v] == ids[w]
    }

    override fun count(): Int {
        return count
    }

    override fun id(v: Int): Int {
        return ids[v]
    }
}

fun main() {
    val path = "./data/tinyG.txt"
    val graph = Graph(In(path))
    val cc = BreadthFirstCC(graph)
    val count = cc.count()
    println("$count components")
    val bagArray = Array(count) { Bag<Int>() }
    for (v in 0 until graph.V) {
        bagArray[cc.id(v)].add(v)
    }
    bagArray.forEach {
        println(it.joinToString())
    }
}