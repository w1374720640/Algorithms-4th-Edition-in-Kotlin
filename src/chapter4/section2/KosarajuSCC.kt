package chapter4.section2

import edu.princeton.cs.algs4.Bag

/**
 * 强连通分量的API
 *
 * 命题H：使用深度优先搜索查找给定有向图G的反向图GR，
 * 根据由此得到的所有顶点的逆后序再次用深度优先搜索处理有向图G（Kosaraju算法），
 * 其构造函数中的每一次递归调用所标记的顶点都在同一个强连通分量中。
 */
class KosarajuSCC(digraph: Digraph) {
    private val marked = BooleanArray(digraph.V)
    private val ids = IntArray(digraph.V)
    private var count = 0

    init {
        val order = DepthFirstOrder(digraph.reverse()).reversePost()
        order.forEach { s ->
            if (!marked[s]) {
                dfs(digraph, s)
                count++
            }
        }
    }

    private fun dfs(digraph: Digraph, v: Int) {
        marked[v] = true
        ids[v] = count
        digraph.adj(v).forEach { w ->
            if (!marked[w]) {
                dfs(digraph, w)
            }
        }
    }

    /**
     * v和w是强连通的吗
     */
    fun stronglyConnected(v: Int, w: Int): Boolean {
        return ids[v] == ids[w]
    }

    /**
     * 图中的强连通分量的总数
     */
    fun count(): Int {
        return count
    }

    /**
     * v所在的强连通分量的标识符（在0至count()-1之间）
     */
    fun id(v: Int): Int {
        return ids[v]
    }
}

fun main() {
    val digraph = getTinyDG()
    val scc = KosarajuSCC(digraph)
    val count = scc.count()
    println("$count components")
    val bagArray = Array(count) { Bag<Int>() }
    for (v in 0 until digraph.V) {
        bagArray[scc.id(v)].add(v)
    }
    bagArray.forEach {
        println(it.joinToString())
    }
}