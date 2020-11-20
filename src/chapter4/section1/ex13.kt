package chapter4.section1

import edu.princeton.cs.algs4.In

/**
 * 为BreadthFirstPaths的API添加并实现一个方法distTo()，返回从起点到给定的顶点的最短路径的长度
 * 它所需的时间应该为常数
 *
 * 解：这里通过扩展函数实现，时间复杂度与最短路径的长度成正比
 * 如果需要常数的时间，需要额外添加一个数组，代码略
 */
fun BreadthFirstPaths.distTo(v: Int): Int {
    val iterable = pathTo(v) ?: return -1
    return iterable.count() - 1
}

fun main() {
    val graph = Graph(In("./data/tinyG.txt"))
    val paths = BreadthFirstPaths(graph, 0)
    for (i in 0 until graph.V) {
        println("$i : ${paths.distTo(i)}")
    }
}