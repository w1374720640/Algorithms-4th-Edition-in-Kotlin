package chapter4.section3

import extensions.formatDouble

/**
 * 逆向删除算法
 * 实现以下计算最小生成树的算法：
 * 开始时图含有原图的所有的边，然后按照权重大小的降序排列遍历所有的边。
 * 对于每条边，如果删除它图仍然是连通的，那就删掉它。
 * 证明这种方法可以得到图的最小生成树。
 * 实现中加权边的比较次数增长的数量级是多少？
 *
 * 解：将所有边按权重排序，加入[EdgeWeightedGraphWithDelete]类中，
 * 按权重由高到低遍历，依次从图中删除，删除后用[EdgeWeightedGraphCC]类判断图的连通性，
 * 如果不连通，将删除的边重新加入后删除下一个，如果连通，直接删除下一个。
 * 对边排序时间复杂度为O(ElgE)，由高到低遍历时间复杂度为O(E)，判断图的连通性时间复杂度接近O(E)
 * 总时间复杂度为O(E^2)
 */
class ReverseDeleteMST(graph: EWG) : MST {
    private val deleteEWG = EdgeWeightedGraphWithDelete(graph)
    private var weight = 0.0

    init {
        val list = ArrayList<Edge>()
        graph.edges().forEach {
            list.add(it)
        }
        list.sortDescending()
        for (i in list.indices) {
            val edge = list[i]
            deleteEWG.delete(edge)
            val cc = EdgeWeightedGraphCC(deleteEWG)
            if (cc.count() != 1) {
                deleteEWG.addEdge(edge)
            }
        }
        check(deleteEWG.E == deleteEWG.V - 1)
        deleteEWG.edges().forEach { weight += it.weight }
    }

    override fun edges(): Iterable<Edge> {
        return deleteEWG.edges()
    }

    override fun weight(): Double {
        return weight
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
                .append("weight=")
                .append(formatDouble(weight, 2))
                .append("\n")
        edges().forEach {
            stringBuilder.append(it.toString())
                    .append("\n")
        }
        return stringBuilder.toString()
    }
}

fun main() {
    val graph = getTinyWeightedGraph()
    val mst = ReverseDeleteMST(graph)
    println(mst)
}