package chapter3.section4

import chapter3.section1.ST
import edu.princeton.cs.algs4.Queue
import edu.princeton.cs.algs4.RedBlackBST
import extensions.random
import extensions.spendTimeMillis

/**
 * 混合使用
 * 用实验研究在SeparateChainingHashST中使用RedBlackBST代替SequentialSearchST来处理碰撞的性能
 * 这种方案的优点是即使散列函数很糟糕它仍然能够保证对数级别的性能，缺点是需要维护两种不同的符号表实现
 * 实际效果如何呢？
 */
class RedBlackHashST<K : Comparable<K>, V : Any>(var m: Int = 4) : ST<K, V> {
    init {
        require(m > 0)
    }

    var n = 0
    var stArray = Array(m) { RedBlackBST<K, V>() }

    fun hash(key: K): Int {
        return (key.hashCode() and 0x7fffffff) % m
    }

    fun resize(size: Int) {
        val newHashST = RedBlackHashST<K, V>(size)
        stArray.forEach { st ->
            st.keys().forEach {
                newHashST.put(it, st.get(it)!!)
            }
        }
        n = newHashST.n
        m = newHashST.m
        stArray = newHashST.stArray
    }

    override fun put(key: K, value: V) {
        if (n >= 8 * m) resize(2 * m)
        val st = stArray[hash(key)]
        if (!st.contains(key)) n++
        st.put(key, value)
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

    fun joinToString(limit: Int = 100): String {
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
    val size = 10_0000
    val times = 100
    println("size=$size  times=$times")
    val linkedListTime = spendTimeMillis {
        repeat(times) {
            val st = SeparateChainingHashST<Int, Int>()
            repeat(size) {
                st.put(random(Int.MAX_VALUE), 0)
            }
        }
    }
    println("linkedListTime=$linkedListTime ms")
    val redBlackTime = spendTimeMillis {
        repeat(times) {
            val st = RedBlackHashST<Int, Int>()
            repeat(size) {
                st.put(random(Int.MAX_VALUE), 0)
            }
        }
    }
    println("redBlackTime=$redBlackTime ms")
}