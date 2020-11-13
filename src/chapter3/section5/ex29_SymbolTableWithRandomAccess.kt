package chapter3.section5

import chapter2.section4.PriorityQueueRandomQueue
import chapter3.section1.ST
import chapter3.section4.LinearProbingHashST

/**
 * 支持随机访问的符号表
 * 创建一个数据结构，能够向其中插入键值对，查找一个键并返回相应的值以及删除并返回一个随机的值
 * 提示：将一个符号表和一个随机队列结合起来实现该数据结构
 *
 * 解：随机队列参考练习2.4.21 [chapter2.section4.PriorityQueueRandomQueue]
 */
class RandomAccessST<K : Any, V : Any> : ST<K, V> {
    private val st = LinearProbingHashST<K, V>()
    private val pq = PriorityQueueRandomQueue<K>()

    override fun put(key: K, value: V) {
        if (st.contains(key)) {
            st.put(key, value)
        } else {
            st.put(key, value)
            pq.enqueue(key)
        }
    }

    override fun get(key: K): V? {
        return st.get(key)
    }

    override fun delete(key: K) {
        throw UnsupportedOperationException("Does not support deleting the specified key.")
    }

    fun randomDelete(): K {
        if (isEmpty()) throw NoSuchElementException()
        val key = pq.dequeue()
        st.delete(key)
        return key
    }

    override fun contains(key: K): Boolean {
        return st.contains(key)
    }

    override fun isEmpty(): Boolean {
        return size() == 0
    }

    override fun size(): Int {
        return st.size()
    }

    override fun keys(): Iterable<K> {
        return pq
    }
}

fun main() {
    val st = RandomAccessST<Int, Int>()
    repeat(10) {
        st.put(it, it)
    }
    repeat(10) {
        println(st.randomDelete())
    }
}