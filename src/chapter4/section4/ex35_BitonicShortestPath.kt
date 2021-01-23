package chapter4.section4

import chapter3.section5.LinearProbingHashSET
import edu.princeton.cs.algs4.Queue
import extensions.formatDouble

/**
 * 双调最短路径
 * 给定一幅有向图，找到从s到其他每个顶点的双调最短路径（如果存在）。
 * 如果从s到t的路径上存在一个中间顶点v使得从s到v中的所有边的权重均严格单调递增且从v到t中的所有边的权重均严格单调递减，
 * 那么这就是一条双调路径。
 * 这样的路径应该是简单的（不包含重复顶点）。
 *
 * 解：假设所有边的权重都为非负数，双调路径至少包含三条边、四个顶点才能组成先递增后递减的双调路径
 * 先找到起点s到其他所有顶点的单调递增路径，O(ElgV)
 * 再找到所有从起点s单调递增可达且不和起点s直接相连的其他顶点，O(V)
 * 分别以这些顶点为起点，找到和其他顶点的单调递减路径，O(VElgV)
 * 分别以除起点和终点外的其他顶点为中间顶点，找到s到其他每个顶点的最短的双调路径。O(V*V)
 * 总时间复杂度为O(VElgV)
 */
class BitonicSP(private val digraph: EdgeWeightedDigraph, private val s: Int) {
    private val distArray = Array(digraph.V) { Double.POSITIVE_INFINITY }
    private val pathArray = arrayOfNulls<Iterable<DirectedEdge>>(digraph.V)

    init {
        val increaseSP = MonotonicSP(digraph, s, true)
        val decreaseSPArray = arrayOfNulls<MonotonicSP>(digraph.V)

        val excludeVertex = LinearProbingHashSET<Int>().apply {
            add(s)
        }
        for (v in 0 until digraph.V) {
            if (v != s && increaseSP.hasPathTo(v)) {
                val path = increaseSP.pathTo(v)!!
                // 因为双调路径至少包含三条边，递增的部分必须至少有两条边
                if (path.count() < 2) continue
                // 递减路径上的所有边的权重必须小于递增路径最后一条边的权重
                val lastEdge = path.last()
                decreaseSPArray[v] = MonotonicSP(digraph, v, false, excludeVertex, maxDist = lastEdge.weight)
            }
        }

        // 简单路径不包含重复点
        val marked = BooleanArray(digraph.V)
        for (t in 0 until digraph.V) {
            // t表示终点
            if (t == s) continue
            var dist = Double.POSITIVE_INFINITY
            var path: Iterable<DirectedEdge>? = null
            loop@ for (v in 0 until digraph.V) {
                // v表示双调路径的中点
                if (v == s || v == t) continue
                if (!increaseSP.hasPathTo(v)) continue
                val decreaseSP = decreaseSPArray[v]
                if (decreaseSP == null || !decreaseSP.hasPathTo(t)) continue
                if (dist <= increaseSP.distTo(v) + decreaseSP.distTo(t)) continue

                for (i in 0 until digraph.V) {
                    marked[i] = false
                }
                marked[s] = true
                val queue = Queue<DirectedEdge>()
                increaseSP.pathTo(v)!!.forEach {
                    queue.enqueue(it)
                    marked[it.to()] = true
                }
                val iterator = decreaseSP.pathTo(t)!!.iterator()
                while (iterator.hasNext()) {
                    val edge = iterator.next()
                    if (marked[edge.to()]) continue@loop
                    queue.enqueue(edge)
                }
                dist = increaseSP.distTo(v) + decreaseSP.distTo(t)
                path = queue
            }
            if (dist != Double.POSITIVE_INFINITY) {
                distArray[t] = dist
                pathArray[t] = path
            }
        }
    }

    fun distTo(v: Int): Double {
        return distArray[v]
    }

    fun hasPathTo(v: Int): Boolean {
        return distTo(v) != Double.POSITIVE_INFINITY
    }

    fun pathTo(v: Int): Iterable<DirectedEdge>? {
        if (!hasPathTo(v)) return null
        return pathArray[v]
    }
}

fun main() {
    val digraph = getTinyEWD()
    for (s in 0 until digraph.V) {
        val sp = BitonicSP(digraph, s)
        for (t in 0 until digraph.V) {
            if (sp.hasPathTo(t)) {
                println("s=$s t=$t dist=${formatDouble(sp.distTo(t), 2)} path=${sp.pathTo(t)?.joinToString()}")
            }
        }
    }
}