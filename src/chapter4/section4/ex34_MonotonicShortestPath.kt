package chapter4.section4

import chapter2.section4.HeapIndexMinPriorityQueue
import chapter3.section5.LinearProbingHashSET
import chapter3.section5.SET

/**
 * 单调最短路径
 * 给定一幅加权有向图，找出从s到其他每个顶点的单调最短路径。
 * 如果一条路径上的所有边的权重是严格单调递增或递减的，那么这条路径就是单调的。
 * 这样的路径应该是简单的（不包含重复顶点）。
 * 提示：按照权重的升序放松所有边并找到一条最佳路径；然后按照权重的降序放松所有边再找到另一条最佳路径。
 *
 * 解：这里假设所有边都为非负权重，使用Dijkstra算法的变形
 * 单调递增的最短路径：修改relax方法中的判断条件，原先只判断大小，现在要求边要递增且取最小的边
 * 单调递减的最短路径同理
 */
class MonotonicSP(digraph: EdgeWeightedDigraph,
                  s: Int,
                  val increase: Boolean,
                  private val excludeVertex: SET<Int> = LinearProbingHashSET(), // excludeVertex和maxDist参数在练习4.4.35中使用
                  private val maxDist: Double = Double.POSITIVE_INFINITY,
                  private val minDist: Double = Double.NEGATIVE_INFINITY) : SP(digraph, s) {
    private val pq = HeapIndexMinPriorityQueue<Double>(digraph.V)

    init {
        distTo[s] = 0.0
        pq[s] = 0.0
        while (!pq.isEmpty()) {
            relax(pq.delMin().second)
        }
    }

    private fun relax(v: Int) {
        val iterator = digraph.adj(v).iterator()
        while (iterator.hasNext()) {
            val edge = iterator.next()
            val w = edge.to()
            // 在练习4.4.35中限制路径应该是简单的，所以需要排除某些顶点
            if (excludeVertex.contains(w)) continue
            if (edge.weight >= maxDist || edge.weight <= minDist) continue
            if (distTo[w] <= distTo[v] + edge.weight) continue
            val lastEdge = edgeTo[v]
            if (lastEdge == null
                    || (increase && lastEdge < edge)
                    || (!increase && lastEdge > edge)) {
                distTo[w] = distTo[v] + edge.weight
                edgeTo[w] = edge
                pq[w] = distTo[w]
            }
        }
    }
}

fun main() {
    val s = 0
    val t = 3
    println("s=$s t=$t")
    val digraph = getTinyEWD()
    val increaseSP = MonotonicSP(digraph, s, true)
    println("increase: ${increaseSP.pathTo(t)?.joinToString()}")
    val decreaseSP = MonotonicSP(digraph, s, false)
    println("decrease: ${decreaseSP.pathTo(t)?.joinToString()}")
}