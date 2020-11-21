package chapter4.section1

import edu.princeton.cs.algs4.In
import edu.princeton.cs.algs4.Queue
import edu.princeton.cs.algs4.Stack

/**
 * 为BreadthFirstPaths的API添加并实现一个方法distTo()，返回从起点到给定的顶点的最短路径的长度
 * 它所需的时间应该为常数
 *
 * 解：如果需要常数的时间，需要额外添加一个数组
 */
class DistToBreadFirstPaths(graph: Graph, val s: Int) : Paths(graph, s) {
    init {
        require(s in 0 until graph.V)
    }

    private val marked = BooleanArray(graph.V)
    private val edgeTo = IntArray(graph.V)
    private val distTo = IntArray(graph.V) { -1 }

    init {
        val queue = Queue<Int>()
        marked[s] = true
        distTo[s] = 0
        queue.enqueue(s)
        while (!queue.isEmpty) {
            val v = queue.dequeue()
            graph.adj(v).forEach { w ->
                if (!marked[w]) {
                    marked[w] = true
                    edgeTo[w] = v
                    distTo[w] = distTo[v] + 1
                    queue.enqueue(w)
                }
            }
        }
    }

    fun distTo(v: Int): Int {
        return distTo[v]
    }

    override fun hasPathTo(v: Int): Boolean {
        return marked[v]
    }

    override fun pathTo(v: Int): Iterable<Int>? {
        if (!hasPathTo(v)) return null
        val stack = Stack<Int>()
        var w = v
        while (w != s) {
            stack.push(w)
            w = edgeTo[w]
        }
        stack.push(s)
        return stack
    }
}

fun main() {
    val graph = Graph(In("./data/tinyG.txt"))
    val paths = DistToBreadFirstPaths(graph, 0)
    for (i in 0 until graph.V) {
        println("$i : ${paths.distTo(i)}")
    }
}