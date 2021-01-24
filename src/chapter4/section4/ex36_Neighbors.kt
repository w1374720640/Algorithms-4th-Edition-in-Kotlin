package chapter4.section4

import edu.princeton.cs.algs4.Queue

/**
 * 邻居顶点
 * 编写一个SP的用例，找出一幅给定加权有向图中和一个给定顶点的距离在d之内的所有顶点。
 * 你的算法所需的运行时间应该与由这些顶点和依附于它们的边组成的子图的大小以及V（用于初始化数据结构）中的较大者成正比。
 *
 * 解：接收一个以给定顶点为起点的SP对象，遍历所有顶点，找到sp中与起点距离小于d的所有顶点
 * 时间复杂度为O(V)，与子图的大小无关
 */
fun ex36_Neighbors(digraph: EdgeWeightedDigraph, sp: SP, d: Double): Iterable<Int> {
    val queue = Queue<Int>()
    for (v in 0 until digraph.V) {
        val dist = sp.distTo(v)
        if (dist < d) {
            queue.enqueue(v)
        }
    }
    return queue
}

fun main() {
    val s = 7
    val d = 0.8
    val digraph = getTinyEWD()
    val sp = DijkstraSP(digraph, s)
    val iterable = ex36_Neighbors(digraph, sp, d)
    println(iterable.joinToString())
}