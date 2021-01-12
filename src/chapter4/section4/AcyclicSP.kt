package chapter4.section4

/**
 * 基于拓扑排序的加权有向图的最短路径算法
 * 要求加权有向图无环，可以在线性时间内解决单点最短路径问题，能够处理负权重的边
 */
class AcyclicSP(digraph: EdgeWeightedDigraph, s: Int) : SP(digraph, s) {
    init {
        val topological = EdgeWeightedTopological(digraph)
        require(topological.isDAG()) { "The directed graph should be acyclic" }
        val iterator = topological.order()!!.iterator()
        distTo[s] = 0.0
        while (iterator.hasNext()) {
            relax(iterator.next())
        }
    }

    private fun relax(v: Int) {
        digraph.adj(v).forEach { edge ->
            val w = edge.to()
            if (distTo[w] > distTo[v] + edge.weight) {
                distTo[w] = distTo[v] + edge.weight
                edgeTo[w] = edge
            }
        }
    }
}

fun main() {
    val digraph = getTinyEWDAG()
    println(AcyclicSP(digraph, 5))
    println(AcyclicSP(digraph, 7))
}