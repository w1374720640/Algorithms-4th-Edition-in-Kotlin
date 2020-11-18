package chapter4.section1

import edu.princeton.cs.algs4.In

/**
 * 为Graph添加一个方法hasEdge()，它接受两个整型参数v和w
 * 如果图含有边v-w，方法返回true，否则返回false
 */
fun Graph.hasEdge(v: Int, w: Int): Boolean {
    val iterable = adj(v)
    iterable.forEach {
        if (it == w) return true
    }
    return false
}

fun main() {
    val path = "./data/tinyG.txt"
    val graph = Graph(In(path))
    println("3-5: ${graph.hasEdge(3, 5)}")
    println("3-7: ${graph.hasEdge(3, 7)}")
}