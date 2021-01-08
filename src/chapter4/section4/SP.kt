package chapter4.section4

import edu.princeton.cs.algs4.Stack
import extensions.formatDouble

/**
 * 加权有向图最短路径的API
 */
abstract class SP(protected val digraph: EdgeWeightedDigraph, protected val s: Int) {
    protected val edgeTo = arrayOfNulls<DirectedEdge>(digraph.V)
    protected val distTo = Array(digraph.V) { Double.POSITIVE_INFINITY }

    /**
     * 从顶点s到v的距离，如果不存在则路径为无穷大
     */
    open fun distTo(v: Int): Double {
        return distTo[v]
    }

    /**
     * 是否存在从顶点s到v的路径
     */
    open fun hasPathTo(v: Int): Boolean {
        return distTo[v] != Double.POSITIVE_INFINITY
    }

    /**
     * 从顶点s到v的路径，如果不存在则为null
     */
    open fun pathTo(v: Int): Iterable<DirectedEdge>? {
        if (!hasPathTo(v)) return null
        val stack = Stack<DirectedEdge>()
        var edge = edgeTo[v]
        while (edge != null) {
            stack.push(edge)
            edge = edgeTo[edge.from()]
        }
        return stack
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        for (v in 0 until digraph.V) {
            stringBuilder.append(s)
                    .append(" to ")
                    .append(v)
            if (hasPathTo(v)) {
                stringBuilder.append(" (")
                        .append(formatDouble(distTo(v), 2))
                        .append("):")
                pathTo(v)!!.forEach {
                    stringBuilder.append(" ")
                            .append(it.toString())
                }
                stringBuilder.append("\n")
            } else {
                stringBuilder.append(" Unreachable\n")
            }
        }
        return stringBuilder.toString()
    }
}