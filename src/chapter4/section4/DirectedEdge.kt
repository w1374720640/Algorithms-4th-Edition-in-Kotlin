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

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other === this) return true
        if (other !is DirectedEdge) return false
        return v == other.v && w == other.w && weight == other.weight
    }

    override fun hashCode(): Int {
        var hash = v
        hash = hash * 31 + w
        hash = hash * 31 + weight.hashCode()
        return hash
    }
}