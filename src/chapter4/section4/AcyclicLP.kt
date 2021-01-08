package chapter4.section4

/**
 * 基于拓扑排序的加权有向图的最长路径算法
 * 参考最短路径算法[AcyclicSP]
 */
class AcyclicLP(digraph: EdgeWeightedDigraph, s: Int) : SP(digraph, s) {
    init {
        // 最长路径算法默认值为负无穷
        repeat(digraph.V) {
            distTo[it] = Double.NEGATIVE_INFINITY
        }

        val topological = EWDTopological(digraph)
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
            // 和最短路径的区别是这里用小于号判断
            if (distTo[w] < distTo[v] + edge.weight) {
                distTo[w] = distTo[v] + edge.weight
                edgeTo[w] = edge
            }
        }
    }

    override fun hasPathTo(v: Int): Boolean {
        return distTo[v] != Double.NEGATIVE_INFINITY
    }
}

fun main() {
    val digraph = getTinyEWDAG()
    println(AcyclicLP(digraph, 5))
}