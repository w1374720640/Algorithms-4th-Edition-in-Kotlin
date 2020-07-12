package chapter1.exercise1_5

interface UF {
    fun union(p: Int, q: Int)
    fun find(p: Int): Int
    fun connected(p: Int, q: Int): Boolean
    fun count(): Int
}