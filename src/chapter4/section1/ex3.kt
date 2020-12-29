package chapter4.section1

import chapter3.section5.LinearProbingHashSET
import chapter3.section5.SET
import edu.princeton.cs.algs4.In

/**
 * 为Graph添加一个赋值构造函数，它接受一副图G然后创建并初始化这幅图的一个副本
 * G的用例对它作出的任何改动都不应该影响它的副本
 *
 * 解：因为通过扩展函数实现，不能直接访问adj变量
 * 遍历图所有顶点对应的边，再将边依次加入新创建的图中
 * 因为一条边会被遍历两次，所以用一个新的数据结构对边去重
 */
fun Graph.copy(): Graph {
    val set = getEdgeSet()
    val graph = Graph(V)
    set.forEach {
        graph.addEdge(it.small, it.large)
    }
    return graph
}

fun Graph.getEdgeSet(): SET<Edge> {
    val set = LinearProbingHashSET<Edge>()
    for (v in 0 until V) {
        adj(v).forEach {
            set.add(Edge(v, it))
        }
    }
    return set
}

/**
 * 无向图的一条边，0-1和1-0相等，无法描述平行边
 */
class Edge(v: Int, w: Int) {

    val small = if (v > w) w else v
    val large = if (v > w) v else w

    override fun hashCode(): Int {
        return small * 31 + large
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other === this) return true
        if (other !is Edge) return false
        return other.small == small && other.large == large
    }
}

fun main() {
    val path = "./data/tinyG.txt"
    println("before change:")
    val graph = Graph(In(path))
    println("graph: $graph")
    val copyGraph = graph.copy()
    println("copy: $copyGraph")

    graph.addEdge(0, 7)
    graph.addEdge(8, 11)
    println("after change:")
    println("graph: $graph")
    println("copy: $copyGraph")
}