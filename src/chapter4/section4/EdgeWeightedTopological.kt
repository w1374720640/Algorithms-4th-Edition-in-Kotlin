package chapter4.section4

/**
 * 加权有向无环图拓扑排序的API
 * 参考[chapter4.section2.Topological]
 */
class EdgeWeightedTopological(digraph: EdgeWeightedDigraph) {
    private var order: Iterable<Int>? = null

    init {
        val cycle = EdgeWeightedCycleFinder(digraph)
        if (!cycle.hasCycle()) {
            val depthFirstOrder = EWDDepthFirstOrder(digraph)
            order = depthFirstOrder.reversePost()
        }
    }

    /**
     * G是有向无环图吗
     */
    fun isDAG(): Boolean {
        return order != null
    }

    /**
     * 拓扑有序的所有顶点
     */
    fun order(): Iterable<Int>? {
        return order
    }
}


fun main() {
    val digraph = getTinyEWDAG()
    val topological = EdgeWeightedTopological(digraph)
    if (topological.isDAG()) {
        println(topological.order()!!.joinToString())
    }
}