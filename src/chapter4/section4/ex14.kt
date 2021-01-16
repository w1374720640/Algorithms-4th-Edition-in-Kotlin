package chapter4.section4

import kotlin.math.abs

/**
 * 给出使用4.4.6.1节和4.4.6.2节的两种尝试处理图4.4.19的tinyEWDn.txt所得到的路径
 *
 * 尝试一：找到权重最小的负权重边，将所有的边的权重都加上这个负值的绝对值，这样原有向图就变为了一幅不含有负权重边的有向图。
 * 对于两条路径，一条路径含有两个边，另一个含有三个边，都加上一个值之后，第二条路径增加的比第一条多，会改变路径间的大小关系。
 *
 * 尝试二：直接使用DijkstraSP算法处理
 */
class AddValueSP(digraph: EdgeWeightedDigraph, s: Int) : SP(digraph, s) {
    private var sp: DijkstraSP

    init {
        var minEdge: DirectedEdge? = null
        digraph.edges().forEach { edge ->
            if (minEdge == null || edge < minEdge!!) {
                minEdge = edge
            }
        }
        if (minEdge == null || minEdge!!.weight > 0.0) {
            sp = DijkstraSP(digraph, s)
        } else {
            val newDigraph = EdgeWeightedDigraph(digraph.V)
            val diff = abs(minEdge!!.weight) + 0.1
            digraph.edges().forEach { edge ->
                newDigraph.addEdge(DirectedEdge(edge.from(), edge.to(), edge.weight + diff))
            }
            sp = DijkstraSP(newDigraph, s)
        }
    }

    override fun distTo(v: Int): Double {
        return sp.distTo(v)
    }

    override fun hasPathTo(v: Int): Boolean {
        return sp.hasPathTo(v)
    }

    override fun pathTo(v: Int): Iterable<DirectedEdge>? {
        return sp.pathTo(v)
    }

    override fun toString(): String {
        return sp.toString()
    }
}

fun main() {
    val digraph = getTinyEWDAGn()
    println(BellmanFordSP(digraph, 0))
    println(AddValueSP(digraph, 0))
    println(DijkstraSP(digraph, 0))
}