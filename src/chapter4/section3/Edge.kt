package chapter4.section3

import extensions.formatDouble

/**
 * 无向图加权边的API
 *
 * 和练习4.1.3中定义的边不同
 */
class Edge(val v: Int, val w: Int, val weight: Double): Comparable<Edge> {

    /**
     * 边两端点之一
     */
    fun either(): Int {
        return v
    }

    /**
     * 另一个顶点
     */
    fun other(v: Int): Int {
        require(v == this.v || v == this.w)
        return if (v == this.v) this.w else this.v
    }

    override fun compareTo(other: Edge): Int {
        return weight.compareTo(other.weight)
    }

    override fun toString(): String {
        return "$v $w ${formatDouble(weight, 2)}"
    }
}