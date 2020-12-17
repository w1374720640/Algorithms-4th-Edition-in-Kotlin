package chapter4.section3

import edu.princeton.cs.algs4.In

/**
 * 从tinyEWG.txt中（请见图4.3.1）删去顶点7并给出加权图的最小生成树
 *
 * 解：继承[EdgeWeightedGraph]类，并添加一个过滤函数，只有当函数返回true时才把边加入图中
 */
class FilterEWG(V: Int, val filter: (Edge) -> Boolean) : EdgeWeightedGraph(V) {

    constructor(input: In, filter: (Edge) -> Boolean) : this(input.readInt(), filter) {
        val E = input.readInt()
        repeat(E) {
            addEdge(Edge(input.readInt(), input.readInt(), input.readDouble()))
        }
    }

    override fun addEdge(edge: Edge) {
        if (filter(edge)) {
            super.addEdge(edge)
        }
    }
}

fun main() {
    val filterEWG = FilterEWG(In("./data/tinyEWG.txt")) {
        it.v != 7 && it.w != 7
    }
    println(filterEWG)
}