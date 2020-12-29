package chapter3.section5

import chapter3.section3.RedBlackBST
import chapter3.section4.LinearProbingHashST
import edu.princeton.cs.algs4.In

/**
 * 分别使用RedBlackBST和LinearProbingHashST来实现OrderedSET和SET（为键关联虚拟值并忽略它们）
 */

/**
 * 基于RedBlackBST实现的有序集合
 */
class RedBlackOrderedSET<K : Comparable<K>> : OrderedSET<K> {
    val st = RedBlackBST<K, Any>()

    companion object {
        private const val DEFAULT_VALUE = ""
    }

    override fun min(): K {
        return st.min()
    }

    override fun max(): K {
        return st.max()
    }

    override fun floor(key: K): K {
        return st.floor(key)
    }

    override fun ceiling(key: K): K {
        return st.ceiling(key)
    }

    override fun rank(key: K): Int {
        return st.rank(key)
    }

    override fun select(k: Int): K {
        return st.select(k)
    }

    override fun deleteMin() {
        st.deleteMin()
    }

    override fun deleteMax() {
        st.deleteMax()
    }

    override fun size(low: K, high: K): Int {
        return st.size(low, high)
    }

    override fun add(key: K) {
        st.put(key, DEFAULT_VALUE)
    }

    override fun delete(key: K) {
        st.delete(key)
    }

    override fun contains(key: K): Boolean {
        return st.contains(key)
    }

    override fun size(): Int {
        return st.size()
    }

    override fun keys(low: K, high: K): Iterable<K> {
        return st.keys(low, high)
    }

    override fun isEmpty(): Boolean {
        return st.isEmpty()
    }

    override fun iterator(): Iterator<K> {
        return st.keys().iterator()
    }

}

/**
 * 基于LinearProbingHashST实现的集合
 */
class LinearProbingHashSET<K : Any> : SET<K> {
    val st = LinearProbingHashST<K, Any>()

    companion object {
        private const val DEFAULT_VALUE = ""
    }

    override fun add(key: K) {
        st.put(key, DEFAULT_VALUE)
    }

    override fun delete(key: K) {
        st.delete(key)
    }

    override fun contains(key: K): Boolean {
        return st.contains(key)
    }

    override fun isEmpty(): Boolean {
        return st.isEmpty()
    }

    override fun size(): Int {
        return st.size()
    }

    override fun iterator(): Iterator<K> {
        return st.keys().iterator()
    }
}

fun main() {
    testSET(LinearProbingHashSET())
    testOrderedSET(RedBlackOrderedSET())

    val set = LinearProbingHashSET<String>()
    val orderedSET = RedBlackOrderedSET<String>()
    val input = In("./data/tinyTale.txt")
    while (!input.isEmpty) {
        val key = input.readString()
        set.add(key)
        orderedSET.add(key)
    }
    println("set       : ${set.joinToString()}")
    println("orderedSET: ${orderedSET.joinToString()}")
}