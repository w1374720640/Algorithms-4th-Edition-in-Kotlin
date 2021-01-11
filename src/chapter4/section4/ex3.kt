package chapter4.section4

import edu.princeton.cs.algs4.In
import edu.princeton.cs.algs4.Queue

/**
 * 为稠密图实现一种使用邻接矩阵表示法（用二维数组保存边的权重，请参考练习4.3.10）的EdgeWeightedDigraph类。
 * 忽略平行边。
 */
class AdjacencyMatrixEWD(val V: Int) {
    private val adj = Array(V) { Array(V) { Double.POSITIVE_INFINITY } }
    var E = 0
        private set

    constructor(input: In) : this(input.readInt()) {
        val E = input.readInt()
        repeat(E) {
            addEdge(DirectedEdge(input.readInt(), input.readInt(), input.readDouble()))
        }
    }

    /**
     * 将e添加到该有向图中
     */
    fun addEdge(edge: DirectedEdge) {
        val v = edge.from()
        val w = edge.to()
        if (adj[v][w] != Double.POSITIVE_INFINITY) return
        adj[v][w] = edge.weight
        E++
    }

    /**
     * 从v指出的边
     */
    fun adj(v: Int): Iterable<DirectedEdge> {
        val queue = Queue<DirectedEdge>()
        adj[v].forEachIndexed { w, weight ->
            if (weight != Double.POSITIVE_INFINITY) {
                queue.enqueue(DirectedEdge(v, w, weight))
            }
        }
        return queue
    }

    /**
     * 该有向图的所有边
     */
    fun edges(): Iterable<DirectedEdge> {
        val queue = Queue<DirectedEdge>()
        for (v in 0 until V) {
            adj[v].forEachIndexed { w, weight ->
                if (weight != Double.POSITIVE_INFINITY) {
                    queue.enqueue(DirectedEdge(v, w, weight))
                }
            }
        }
        return queue
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
                .append(V)
                .append(" vertices, ")
                .append(E)
                .append(" edges\n")
        edges().forEach {
            stringBuilder.append(it.toString())
                    .append("\n")
        }
        return stringBuilder.toString()
    }
}

fun main() {
    val digraph = AdjacencyMatrixEWD(In("./data/tinyEWD.txt"))
    println(digraph)
}