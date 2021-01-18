package chapter4.section4

import chapter3.section5.LinearProbingHashSET
import chapter3.section5.SET

/**
 * 两个顶点集合之间的最短路径
 * 给定一幅边的权重均为正的有向图和两个没有交集的顶点集S和T，
 * 找到从S中的任意顶点到达T中的任意顶点的最短路径。
 * 你的算法在最坏情况下所需的时间应该与ElogV成正比。
 *
 * 解：根据练习4.4.24中的算法，将集合S中的所有顶点作为起点，生成最短路径森林，
 * 遍历集合T，找到到达每个顶点的最短路径，最短的最短路径必定只含有一条边
 */
fun ex25_ShortestPathsBetweenTwoSubsets(digraph: EdgeWeightedDigraph, S: SET<Int>, T: SET<Int>): DirectedEdge? {
    val startingPoints = Array(S.size()) { 0 }
    S.forEachIndexed { i, v ->
        check(!T.contains(v))
        startingPoints[i] = v
    }
    T.forEach {
        check(!S.contains(it))
    }

    val sp = MultisourceShortestPaths(digraph, startingPoints)
    var minEdge: DirectedEdge? = null
    T.forEach { v ->
        if (sp.hasPathTo(v)) {
            if (minEdge == null || minEdge!!.weight > sp.distTo(v)) {
                val path = sp.pathTo(v)!!.iterator()
                val edge = path.next()
                // 连接两个集合顶点的最短路径必定只包含一条边
                if (!path.hasNext()) {
                    minEdge = edge
                }
            }
        }
    }
    return minEdge
}

fun main() {
    val digraph = getTinyEWD()
    val S = LinearProbingHashSET<Int>()
    val T = LinearProbingHashSET<Int>()
    S.add(0)
    S.add(1)
    S.add(2)
    S.add(3)
    T.add(4)
    T.add(5)
    T.add(6)
    T.add(7)
    println(ex25_ShortestPathsBetweenTwoSubsets(digraph, S, T))
    println(ex25_ShortestPathsBetweenTwoSubsets(digraph, T, S))
}