package chapter4.section2

import edu.princeton.cs.algs4.Bag
import edu.princeton.cs.algs4.Queue

/**
 * 强连通分量
 * 设计一种线性时间的算法来计算给定顶点v所在的强连通分量。
 * 在这个算法的基础上设计一种平方时间的算法来计算有向图的所有强连通分量。
 *
 * 解：遍历所有顶点，标记可以从顶点v到达的所有顶点
 * 获取有向图的反向图GR，遍历所有顶点，标记可以从顶点v到达的所有顶点
 * 在两张图中同时被标记为可达的顶点在顶点v的强连通分量中
 */
class StrongComponentSingleVertex(digraph: Digraph, v: Int) {
    private val connected = BooleanArray(digraph.V)
    private var count = 0

    init {
        val paths = BreadthFirstDirectedPaths(digraph, v)
        val reversePaths = BreadthFirstDirectedPaths(digraph.reverse(), v)
        for (i in 0 until digraph.V) {
            if (paths.hasPathTo(i) && reversePaths.hasPathTo(i)) {
                connected[i] = true
                count++
            }
        }
    }


    fun connected(w: Int): Boolean {
        return connected[w]
    }

    fun count(): Int {
        return count
    }

    fun connectedVertex(): Iterable<Int> {
        val queue = Queue<Int>()
        for (i in connected.indices) {
            if (connected[i]) {
                queue.enqueue(i)
            }
        }
        return queue
    }
}

class StrongComponentAllVertex(digraph: Digraph) {
    private val ids = IntArray(digraph.V) { -1 }
    private var count = 0

    init {
        for (i in 0 until digraph.V) {
            if (ids[i] == -1) {
                val singleVertex = StrongComponentSingleVertex(digraph, i)
                singleVertex.connectedVertex().forEach {
                    ids[it] = count
                }
                count++
            }
        }
    }

    fun connected(v: Int, w: Int): Boolean {
        return ids[v] == ids[w]
    }

    fun count(): Int {
        return count
    }

    fun id(v: Int): Int {
        return ids[v]
    }
}

fun main() {
    val digraph = getTinyDG()
    val scc = StrongComponentAllVertex(digraph)
    val count = scc.count()
    println("$count components")
    val bagArray = Array(count) { Bag<Int>() }
    for (v in 0 until digraph.V) {
        bagArray[scc.id(v)].add(v)
    }
    bagArray.forEach {
        println(it.joinToString())
    }
}