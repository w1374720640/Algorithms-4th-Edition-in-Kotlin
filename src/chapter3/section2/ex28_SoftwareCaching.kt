package chapter3.section2

import chapter3.section1.frequencyCounter
import chapter3.section1.testOrderedST
import edu.princeton.cs.algs4.In
import extensions.spendTimeMillis

/**
 * 缓存
 * 修改二叉查找树的实现，将最近访问的结点Node保存在一个变量中，这样get()或put()再次访问同一个键时就只需要常数时间了
 */
class CacheBinaryTreeST<K : Comparable<K>, V : Any> : BinaryTreeST<K, V>() {
    private var recentNode: Node<K, V>? = null

    override fun get(key: K): V? {
        if (recentNode != null && recentNode!!.key == key) {
            return recentNode!!.value
        }
        recentNode = if (isEmpty()) {
            null
        } else {
            get(root!!, key)
        }
        return recentNode?.value
    }

    override fun put(key: K, value: V) {
        if (recentNode != null && recentNode!!.key == key) {
            recentNode!!.value = value
            return
        }
        super.put(key, value)
    }

    override fun deleteMin() {
        recentNode = null
        super.deleteMin()
    }

    override fun deleteMax() {
        recentNode == null
        super.deleteMax()
    }

    override fun delete(key: K) {
        recentNode = null
        super.delete(key)
    }
}

fun main() {
    testOrderedST(CacheBinaryTreeST())

    val time1 = spendTimeMillis {
        frequencyCounter(In("./data/tale.txt"), 0, BinaryTreeST())
    }
    println("$time1 ms")
    val time2 = spendTimeMillis {
        frequencyCounter(In("./data/tale.txt"), 0, CacheBinaryTreeST())
    }
    println("$time2 ms")
}