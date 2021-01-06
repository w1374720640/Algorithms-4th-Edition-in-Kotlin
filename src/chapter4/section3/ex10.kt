package chapter4.section3

import edu.princeton.cs.algs4.In
import edu.princeton.cs.algs4.Queue

/**
 * 为稠密图实现EdgeWeightedGraph，使用邻接矩阵（存储权重的二维数组），不允许存在平行边。
 */
class AdjacencyMatrixEWG(V: Int) : EWG(V) {
    private val matrix = Array(V) { Array(V) { Double.POSITIVE_INFINITY } }

    constructor(input: In) : this(input.readInt()) {
        val E = input.readInt()
        repeat(E) {
            val edge = Edge(input.readInt(), input.readInt(), input.readDouble())
            addEdge(edge)
        }
    }

    private fun hasEdge(v: Int, w: Int): Boolean {
        return matrix[v][w] != Double.POSITIVE_INFINITY
    }

    override fun addEdge(edge: Edge) {
        val v = edge.either()
        val w = edge.other(v)
        // 不允许存在平行边
        if (hasEdge(v, w)) return

        matrix[v][w] = edge.weight
        matrix[w][v] = edge.weight
        E++
    }

    override fun adj(v: Int): Iterable<Edge> {
        val queue = Queue<Edge>()
        for (w in matrix[v].indices) {
            if (hasEdge(v, w)) {
                queue.enqueue(Edge(v, w, matrix[v][w]))
            }
        }
        return queue
    }

    override fun edges(): Iterable<Edge> {
        val queue = Queue<Edge>()
        for (v in 0 until V) {
            for (w in v + 1 until V) {
                if (hasEdge(v, w)) {
                    queue.enqueue(Edge(v, w, matrix[v][w]))
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
    val graph = AdjacencyMatrixEWG(In("./data/tinyEWG.txt"))
    println(graph)
}