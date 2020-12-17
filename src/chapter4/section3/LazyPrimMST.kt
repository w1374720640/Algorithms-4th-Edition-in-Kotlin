package chapter4.section3

import chapter2.section4.HeapMinPriorityQueue
import edu.princeton.cs.algs4.Queue
import extensions.formatDouble

/**
 * 最小生成树的Prim算法的延时实现
 */
class LazyPrimMST(graph: EdgeWeightedGraph) : MST(graph) {
    private val marked = BooleanArray(graph.V)
    private val queue = Queue<Edge>()
    private val minPQ = HeapMinPriorityQueue<Edge>()
    private var weight = 0.0

    init {
        visit(graph, 0)
        while (!minPQ.isEmpty()) {
            val edge = minPQ.delMin()
            val v = edge.either()
            val w = edge.other(v)
            if (marked[v] && marked[w]) continue
            queue.enqueue(edge)
            if (!marked[v]) visit(graph, v)
            if (!marked[w]) visit(graph, w)
            weight += edge.weight
        }
        check(queue.size() == graph.V - 1)
    }

    private fun visit(graph: EdgeWeightedGraph, v: Int) {
        marked[v] = true
        graph.adj(v).forEach {
            if (!marked[it.other(v)]) {
                minPQ.insert(it)
            }
        }
    }

    override fun edges(): Iterable<Edge> {
        return queue
    }

    override fun weight(): Double {
        return weight
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
                .append("weight=")
                .append(formatDouble(weight, 2))
                .append("\n")
        queue.forEach {
            stringBuilder.append(it.toString())
                    .append("\n")
        }
        return stringBuilder.toString()
    }
}

fun main() {
    val graph = getTinyWeightedGraph()
    val lazyPrimMST = LazyPrimMST(graph)
    println(lazyPrimMST.toString())
    drawRandomEWG { LazyPrimMST(it) }
}