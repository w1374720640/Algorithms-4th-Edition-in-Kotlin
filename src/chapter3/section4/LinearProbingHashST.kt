package chapter3.section4

import chapter3.section1.ST
import chapter3.section1.testST
import edu.princeton.cs.algs4.Queue

/**
 * 基于线性探测的符号表
 * 保持使用率在1/8到1/2之间
 */
class LinearProbingHashST<K : Any, V : Any>(var m: Int = 4) : ST<K, V> {
    private var n = 0
    private var keys = arrayOfNulls<Any>(m)
    private var values = arrayOfNulls<Any>(m)

    init {
        require(m > 0)
    }

    private fun hash(key: K): Int {
        return (key.hashCode() and 0x7fffffff) % m
    }

    private fun resize(size: Int) {
        val newST = LinearProbingHashST<K, V>(size)
        for (i in keys.indices) {
            if (keys[i] != null) {
                @Suppress("UNCHECKED_CAST")
                newST.put(keys[i] as K, values[i] as V)
            }
        }
        n = newST.n
        m = newST.m
        keys = newST.keys
        values = newST.values
    }

    override fun put(key: K, value: V) {
        if (n >= m / 2) resize(m * 2)
        var i = hash(key)
        while (true) {
            @Suppress("UNCHECKED_CAST")
            when (keys[i] as? K) {
                null -> {
                    keys[i] = key
                    values[i] = value
                    n++
                    return
                }
                key -> {
                    values[i] = value
                    return
                }
                else -> {
                    i = (i + 1) % m
                }
            }
        }
    }

    override fun get(key: K): V? {
        var i = hash(key)
        while (true) {
            @Suppress("UNCHECKED_CAST")
            when (keys[i] as? K) {
                null -> return null
                key -> return values[i] as? V
                else -> i = (i + 1) % m
            }
        }
    }

    override fun delete(key: K) {
        var i = hash(key)
        while (true) {
            @Suppress("UNCHECKED_CAST")
            when (keys[i] as? K) {
                null -> throw NoSuchElementException()
                key -> {
                    //如果后面不为空的数据hash值和索引不同，则向前挪动一位
                    while (true) {
                        val nextIndex = (i + 1) % m
                        val nextKey = keys[nextIndex] as? K
                        if (nextKey != null && hash(nextKey) != nextIndex) {
                            keys[i] = keys[nextIndex]
                            values[i] = values[nextIndex]
                            i = nextIndex
                        } else break
                    }
                    //删除最后一条数据
                    keys[i] = null
                    values[i] = null
                    n--
                    //检查是否需要重新调整大小
                    if (m >= 8 && n <= m / 8) resize(m / 2)
                    return
                }
                else -> i = (i + 1) % m
            }
        }
    }

    override fun contains(key: K): Boolean {
        return get(key) != null
    }

    override fun isEmpty(): Boolean {
        return size() == 0
    }

    override fun size(): Int {
        return n
    }

    override fun keys(): Iterable<K> {
        val queue = Queue<K>()
        for (i in keys.indices) {
            if (keys[i] != null) {
                @Suppress("UNCHECKED_CAST")
                queue.enqueue(keys[i] as K)
            }
        }
        return queue
    }
}

fun main() {
    testST(LinearProbingHashST())
}