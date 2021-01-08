package chapter4.section4

import extensions.formatDouble

/**
 * 加权有向边的API
 */
class DirectedEdge(private val v: Int, private val w: Int, val weight: Double) : Comparable<DirectedEdge> {

    /**
     * 指出这条边的顶点
     */
    fun from() = v

    /**
     * 这条边指向的顶点
     */
    fun to() = w

    override fun compareTo(other: DirectedEdge): Int {
        return weight.compareTo(other.weight)
    }

    override fun toString(): String {
        return "$v->$w ${formatDouble(weight, 2)}"
    }
}