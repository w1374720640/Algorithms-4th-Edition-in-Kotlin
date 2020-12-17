package chapter4.section3

import chapter1.section5.CompressionWeightedQuickUnionUF
import edu.princeton.cs.algs4.Bag

/**
 * 找出一幅加权无向图的所有连通分量
 */
class EdgeWeightedGraphCC(graph: EWG) {
    private val uf = CompressionWeightedQuickUnionUF(graph.V)

    init {
        graph.edges().forEach {
            val v = it.either()
            val w = it.other(v)
            uf.union(v, w)
        }
    }

    fun connected(v: Int, w: Int): Boolean {
        return uf.connected(v, w)
    }

    fun count(): Int {
        return uf.count()
    }

    fun id(v: Int): Int {
        return uf.find(v)
    }
}

fun main() {
    val graph = getTinyWeightedGraph()
    val cc = EdgeWeightedGraphCC(graph)
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