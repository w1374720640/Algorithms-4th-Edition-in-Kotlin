package chapter4.section1

import chapter1.section5.CompressionWeightedQuickUnionUF
import edu.princeton.cs.algs4.In
import extensions.readInt

/**
 * 按照正文中的要求，用union-find算法实现4.1.2.3中搜索的API
 */
class UnionFindSearch(val graph: Graph, val s: Int) : Search(graph, s) {
    // 压缩路径、带权重的QuickUnion
    private val uf = CompressionWeightedQuickUnionUF(graph.V)
    private var count = 0

    init {
        for (v in 0 until graph.V) {
            graph.adj(v).forEach { w ->
                uf.union(v, w)
            }
        }

        // 初始化时统计count值，防止count()方法时间复杂度退化为O(N)
        val sId = uf.find(s)
        for (v in 0 until graph.V) {
            if (uf.find(v) == sId) {
                count++
            }
        }
    }

    override fun marked(v: Int): Boolean {
        return uf.connected(s, v)
    }

    override fun count(): Int {
        return uf.treeSize[uf.find(s)]
    }

}

fun main() {
    val path = "./data/tinyG.txt"
    val graph = Graph(In(path))
    val s = readInt("search with: ")
    val search = UnionFindSearch(graph, s)
    for (i in 0 until graph.V) {
        if (search.marked(i)) {
            print("$i ")
        }
    }
    println()
    println("count=${search.count()}")
    println("${if (search.count() == graph.V) "" else "NOT"} connected")
}