package chapter4.section4

import edu.princeton.cs.algs4.Bag
import edu.princeton.cs.algs4.In
import edu.princeton.cs.algs4.Queue

/**
 * 加权有向图的API
 */
open class EdgeWeightedDigraph(val V: Int) {
    protected val adj = Array(V) { Bag<DirectedEdge>() }
    var E: Int = 0
        protected set

    constructor(input: In) : this(input.readInt()) {
        val E = input.readInt()
        repeat(E) {
            addEdge(DirectedEdge(input.readInt(), input.readInt(), input.readDouble()))
        }
    }

    /**
     * 将e添加到该有向图中
     */
    open fun addEdge(edge: DirectedEdge) {
        adj[edge.from()].add(edge)
        E++
    }

    /**
     * 从v指出的边
     */
    open fun adj(v: Int): Iterable<DirectedEdge> {
        return adj[v]
    }

    /**
     * 该有向图的所有边
     */
    open fun edges(): Iterable<DirectedEdge> {
        val queue = Queue<DirectedEdge>()
        adj.forEach { bag ->
            bag.forEach { edge ->
                queue.enqueue(edge)
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

/**
 * 获取一个小规模的加权有向图，图形见课本P412
 */
fun getTinyEWD() = EdgeWeightedDigraph(In("./data/tinyEWD.txt"))

/**
 * 获取一个小规模的无环加权有向图，图形见课本P426
 */
fun getTinyEWDAG() = EdgeWeightedDigraph(In("./data/tinyEWDAG.txt"))

/**
 * 获取一个小规模、含负权重环的加权有向图，图形见课本P435
 */
fun getTinyEWDAGnc() = EdgeWeightedDigraph(In("./data/tinyEWDnc.txt"))

/**
 * 获取一个小规模、含负权重边、有环、但不含负权重环的加权有向图，图形见课本P441
 */
fun getTinyEWDAGn() = EdgeWeightedDigraph(In("./data/tinyEWDn.txt"))

fun main() {
    val digraph = getTinyEWD()
    println(digraph)
}