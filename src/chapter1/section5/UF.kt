package chapter1.section5

/**
 * union-find算法的API
 */
interface UF {
    fun union(p: Int, q: Int)
    fun find(p: Int): Int
    fun connected(p: Int, q: Int): Boolean
    fun count(): Int
}