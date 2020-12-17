package chapter4.section3

import edu.princeton.cs.algs4.In
import edu.princeton.cs.algs4.Queue

/**
 * 为稠密图实现EdgeWeightedGraph，使用邻接矩阵（存储权重的二维数组），不允许存在平行边。
 */
class AdjacencyMatrixEWG(V: Int) : EWG(V) {
    private val matrix = Array(V) { Array<Edge?>(V) { null } }

    constructor(input: In) : this(input.readInt()) {
        val E = input.readInt()
        repeat(E) {
            val edge = Edge(input.readInt(), input.readInt(), input.readDouble())
            addEdge(edge)
        }
    }

    override fun addEdge(edge: Edge) {
        val v = edge.either()
        val w = edge.other(v)
        if (matrix[v][w] == null || matrix[w][v] == null) {
            matrix[v][w] = edge
            matrix[w][v] = edge
            E++
        } else { // 不允许存在平行边，只设置新值，不增加边的数量
            matrix[v][w] = edge
            matrix[w][v] = edge
        }
    }

    override fun adj(v: Int): Iterable<Edge> {
        val queue = Queue<Edge>()
        matrix[v].forEach {
            if (it != null) {
                queue.enqueue(it)
            }
        }
        return queue
    }

    override fun edges(): Iterable<Edge> {
        val queue = Queue<Edge>()
        for (v in 0 until V) {
            for (w in v + 1 until V) {
                if (matrix[v][w] != null) {
                    queue.enqueue(matrix[v][w])
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

    drawEWGGraph(graph) {
        LazyPrimMST(it)
    }
}