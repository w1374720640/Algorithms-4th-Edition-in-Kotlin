package chapter4.section3

import edu.princeton.cs.algs4.Queue
import extensions.formatDouble

/**
 * 最小生成树的API
 */
abstract class MST {
    protected val queue = Queue<Edge>()
    protected var weight = 0.0

    /**
     * 最小生成树的所有边
     */
    open fun edges(): Iterable<Edge> {
        return queue
    }

    /**
     * 最小生成树的权重
     */
    open fun weight(): Double {
        return weight
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
                .append("weight=")
                .append(formatDouble(weight, 2))
                .append("\n")
        edges().forEach {
            stringBuilder.append(it.toString())
                    .append("\n")
        }
        return stringBuilder.toString()
    }
}