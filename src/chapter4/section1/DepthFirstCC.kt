package chapter4.section1

import edu.princeton.cs.algs4.Bag
import edu.princeton.cs.algs4.In

class DepthFirstCC(graph: Graph) : CC(graph) {
    private var count = 0
    private val ids = IntArray(graph.V)
    private val marked = BooleanArray(graph.V)

    init {
        for (v in 0 until graph.V) {
            if (!marked[v]) {
                dfs(graph, v)
                count++
            }
        }
    }

    private fun dfs(graph: Graph, v: Int) {
        marked[v] = true
        ids[v] = count
        graph.adj(v).forEach {
            if (!marked[it]) {
                dfs(graph, it)
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
    val cc = DepthFirstCC(graph)
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