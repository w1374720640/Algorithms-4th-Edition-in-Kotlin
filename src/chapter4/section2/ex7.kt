package chapter4.section2

import edu.princeton.cs.algs4.Queue

/**
 * 顶点的入度为指向该顶点的边的总数。
 * 顶点的出度为由该顶点指出的边的总数。
 * 从出度为0的顶点是不可能达到任何顶点的，这种顶点叫做终点；
 * 入度为0的顶点是不可能从任何顶点到达的，所以叫做起点。
 * 一幅允许出现自环且每个顶点的出度均为1的有向图叫做映射（从0到V-1之间的整数到它们自身的函数）。
 * 编写一段程序Degrees.java，实现下面的API。
 */
class Degrees(digraph: Digraph) {
    private val inDegrees = IntArray(digraph.V)
    private val outDegrees = IntArray(digraph.V)

    init {
        for (v in 0 until digraph.V) {
            digraph.adj(v).forEach { w ->
                inDegrees[w]++
                outDegrees[v]++
            }
        }
    }

    /**
     * v的入度
     */
    fun inDegree(v: Int): Int {
        return inDegrees[v]
    }

    /**
     * v的出度
     */
    fun outDegree(v: Int): Int {
        return outDegrees[v]
    }

    /**
     * 所有起点的集合
     */
    fun sources(): Iterable<Int> {
        val queue = Queue<Int>()
        for (i in inDegrees.indices) {
            if (inDegrees[i] == 0) queue.enqueue(i)
        }
        return queue
    }

    /**
     * 所有终点的集合
     */
    fun sinks(): Iterable<Int> {
        val queue = Queue<Int>()
        for (i in outDegrees.indices) {
            if (outDegrees[i] == 0) queue.enqueue(i)
        }
        return queue
    }

    /**
     * G是一幅映射吗？
     */
    fun isMap(): Boolean {
        for (i in outDegrees.indices) {
            if (outDegrees[i] != 1) return false
        }
        return true
    }
}

fun main() {
    val digraph1 = getTinyDG()
    val degrees = Degrees(digraph1)
    println("inDegrees(0): ${degrees.inDegree(0)}")
    println("outDegrees(0): ${degrees.outDegree(0)}")
    println("sources: ${degrees.sources().joinToString()}")
    println("sinks: ${degrees.sinks().joinToString()}")
    println("isMap1: ${degrees.isMap()}")

    val digraph2 = Digraph(4)
    digraph2.addEdge(0, 1)
    digraph2.addEdge(1, 2)
    digraph2.addEdge(2, 3)
    digraph2.addEdge(3, 0)
    println("isMap2: ${Degrees(digraph2).isMap()}")
}