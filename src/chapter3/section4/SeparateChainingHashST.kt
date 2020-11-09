package chapter3.section4

import chapter3.section1.SequentialSearchST
import chapter3.section1.ST
import chapter3.section1.testST
import edu.princeton.cs.algs4.Queue
import java.lang.StringBuilder

/**
 * 基于拉链法的散列表
 * 保持拉链中的每个链表平均长度在2到8之间
 */
open class SeparateChainingHashST<K : Any, V : Any>(m: Int = 4) : ST<K, V> {
    init {
        require(m > 0)
    }

    var m: Int = m
        protected set
    var n = 0
        protected set
    protected var stArray = Array(m) { SequentialSearchST<K, V>() }

    open fun hash(key: K): Int {
        return (key.hashCode() and 0x7fffffff) % m
    }

    protected open fun resize(size: Int) {
        val newHashST = SeparateChainingHashST<K, V>(size)
        stArray.forEach { st ->
            st.keys().forEach {
                newHashST.put(it, st.get(it)!!)
            }
        }
        m = newHashST.m
        stArray = newHashST.stArray
    }

    override fun put(key: K, value: V) {
        if (n >= 8 * m) resize(2 * m)
        val st = stArray[hash(key)]
        val beforeSize = st.size()
        st.put(key, value)
        if (st.size() > beforeSize) {
            n++
        }
    }

    override fun get(key: K): V? {
        return stArray[hash(key)].get(key)
    }

    override fun delete(key: K) {
        val st = stArray[hash(key)]
        if (st.contains(key)) {
            st.delete(key)
            n--
            if (m > 4 && n <= 2 * m) resize(m / 2)
        } else {
            throw NoSuchElementException()
        }
    }

    override fun contains(key: K): Boolean {
        return stArray[hash(key)].contains(key)
    }

    override fun isEmpty(): Boolean {
        return n == 0
    }

    override fun size(): Int {
        return n
    }

    override fun keys(): Iterable<K> {
        val queue = Queue<K>()
        stArray.forEach { st ->
            st.keys().forEach {
                queue.enqueue(it)
            }
        }
        return queue
    }

    open fun joinToString(limit: Int = 100): String {
        val stringBuilder = StringBuilder("m=${m} n=${n}")
        for (i in stArray.indices) {
            if (i >= limit) {
                stringBuilder.append("\n...")
                break
            }
            stringBuilder.append("\n")
                    .append("i=$i ")
                    .append(stArray[i].keys().joinToString())
        }
        return stringBuilder.toString()
    }
}

fun main() {
    testST(SeparateChainingHashST())
}