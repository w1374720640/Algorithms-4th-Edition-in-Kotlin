package chapter4.section3

import edu.princeton.cs.algs4.Bag
import edu.princeton.cs.algs4.In
import edu.princeton.cs.algs4.Queue

/**
 * 加权无向图的API
 */
open class EdgeWeightedGraph {
    val V: Int
    var E: Int = 0
    protected val adj: Array<Bag<Edge>>

    constructor(V: Int) {
        this.V = V
        adj = Array(V) { Bag<Edge>() }
    }

    constructor(input: In) : this(input.readInt()) {
        val E = input.readInt()
        repeat(E) {
            addEdge(Edge(input.readInt(), input.readInt(), input.readDouble()))
        }
    }

    fun addEdge(edge: Edge) {
        adj[edge.v].add(edge)
        adj[edge.w].add(edge)
        E++
    }

    fun adj(v: Int): Iterable<Edge> {
        return adj[v]
    }

    fun edges(): Iterable<Edge> {
        val queue = Queue<Edge>()
        for (v in adj.indices) {
            val bag = adj[v]
            bag.forEach {
                if (it.other(v) > v) {
                    queue.enqueue(it)
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

fun getTinyWeightedGraph(): EdgeWeightedGraph {
    return EdgeWeightedGraph(In("./data/tinyEWG.txt"))
}

fun main() {
    println(getTinyWeightedGraph())
}