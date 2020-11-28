package chapter4.section2

/**
 * 拓扑排序的API
 *
 * 命题E：当且仅当一幅有向图是无环图时它才能进行拓扑排序
 * 命题F：一幅有向无环图的拓扑排序即为所有顶点的逆后序排列
 */
class Topological(digraph: Digraph) {
    private var order: Iterable<Int>? = null

    init {
        val cycle = DirectedCycle(digraph)
        if (!cycle.hasCycle()) {
            val depthFirstOrder = DepthFirstOrder(digraph)
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
    val fileName = "./data/jobs.txt"
    val delim = "/"
    val symbolDigraph = SymbolDigraph(fileName, delim)
    val digraph = symbolDigraph.G()
    val topological = Topological(digraph)
    if (topological.isDAG()) {
        topological.order()!!.forEach {
            println(symbolDigraph.name(it))
        }
    }
}