package chapter4.section3

import chapter3.section5.LinearProbingHashSET
import edu.princeton.cs.algs4.Queue

/**
 * 一个特殊的图，提供了从图中删除一条边的方法
 */
class EdgeWeightedGraphWithDelete(V: Int) : EWG(V) {
    private val adj = Array(V) { LinearProbingHashSET<Edge>() }

    constructor(ewg: EWG) : this(ewg.V) {
        ewg.edges().forEach {
            addEdge(it)
        }
    }

    override fun addEdge(edge: Edge) {
        val v = edge.either()
        val w = edge.other(v)
        adj[v].add(edge)
        adj[w].add(edge)
        E++
    }

    override fun adj(v: Int): Iterable<Edge> {
        return adj[v]
    }

    override fun edges(): Iterable<Edge> {
        val queue = Queue<Edge>()
        for (v in 0 until V) {
            adj[v].forEach { edge ->
                val w = edge.other(v)
                if (v > w) {
                    queue.enqueue(edge)
                }
            }
        }
        return queue
    }

    fun delete(edge: Edge) {
        val v = edge.either()
        val w = edge.other(v)
        adj[v].delete(edge)
        adj[w].delete(edge)
        E--
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
    val graph = getTinyWeightedGraph()
    val deleteEWG = EdgeWeightedGraphWithDelete(graph)
    println(deleteEWG)
}