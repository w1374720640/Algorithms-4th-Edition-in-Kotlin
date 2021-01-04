package chapter4.section3

/**
 * Vyssotsky算法
 * 开发一种不断使用环的性质（请见练习4.3.8）来计算最小生成树的算法：
 * 每次将一条边添加到假设的最小生成树中，如果形成了一个环则删去环中权重最大的边。
 * 注意：这个算法不如我们学过的几种方法引人注意，因为很难找到一种数据结构能够有效支持“删除环中权重最大的边”的操作。
 * 练习4.3.8：证明环的性质：任取一幅加权图中的一个环（边的权重各不相同），环中权重最大的边必然不属于图的最小生成树。
 *
 * 解：使用可以删除边的加权无向图[EdgeWeightedGraphWithDelete]替代原始加权无向图，
 * 将所有边一条一条加入无向图中，同时使用[EdgeWeightedGraphCycle]类查找当前图中的一个环，
 * 如果找到环，删除环中权重最大的边，继续添加，直到所有的边都添加到新的无向图中，
 * 因为边一条一条的加入，每次加入后会删除环中权重最大的边，所以，每次查找环时，最多需要遍历V条边，
 * 一共需要查找E次，所以总的时间复杂度为O(V*E)
 */
class VyssotskyMST(graph: EWG) : MST() {
    private val deleteEWG = EdgeWeightedGraphWithDelete(graph.V)

    init {
        graph.edges().forEach { edge ->
            deleteEWG.addEdge(edge)
            val cycle = EdgeWeightedGraphCycle(deleteEWG)
            if (cycle.hasCycle()) {
                val iterator = cycle.cycle()!!.iterator()
                var maxEdge = iterator.next()
                while (iterator.hasNext()) {
                    val nextEdge = iterator.next()
                    if (nextEdge.weight > maxEdge.weight) {
                        maxEdge = nextEdge
                    }
                }
                deleteEWG.delete(maxEdge)
            }
        }
        deleteEWG.edges().forEach {
            weight += it.weight
        }
    }

    override fun edges(): Iterable<Edge> {
        return deleteEWG.edges()
    }
}

fun main() {
    val graph = getTinyWeightedGraph()
    val mst = VyssotskyMST(graph)
    println(mst.toString())
}