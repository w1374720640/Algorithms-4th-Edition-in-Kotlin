package chapter4.section1

import edu.princeton.cs.algs4.Bag
import edu.princeton.cs.algs4.In

/**
 * 找出一幅图中的所有连通分量（深度优先）
 */
class CC(graph: Graph) {
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

    /**
     * v和w是否相连
     */
    fun connected(v: Int, w: Int): Boolean {
        return ids[v] == ids[w]
    }

    /**
     * 连通分量的数量
     */
    fun count(): Int {
        return count
    }

    /**
     * v所在连通分量的标识符(0 ~ count() - 1)
     */
    fun id(v: Int): Int {
        return ids[v]
    }
}

fun main() {
    val path = "./data/tinyG.txt"
    val graph = Graph(In(path))
    val cc = CC(graph)
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