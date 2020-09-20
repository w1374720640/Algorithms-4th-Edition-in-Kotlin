package chapter3.section1

import edu.princeton.cs.algs4.In
import extensions.spendTimeMillis

/**
 * 为了提高查找效率，我们可以用一种叫缓存的技术手段，即将最近访问的键的位置保存在一个变量中
 * 修改LinkedListST和ArrayOrderedST来实现这个点子
 *
 * 解：书上要求将访问最频繁的键的位置保存在一个变量中，实际上是要求将最近访问的键保存在一个变量中
 * 对ArrayOrderedST来说很简单，在rank方法中保存最近访问的键，下次rank时，先检查最近访问的键是否有效
 * 对LinkedListST来说，在get和put方法中保存最近访问的键，下次在get和put方法中先检查最近访问的键是否匹配
 */
class CacheArrayOrderedST<K : Comparable<K>, V : Any> : ArrayOrderedST<K, V>() {
    private var recentIndex = -1

    override fun rank(key: K): Int {
        if (recentIndex != -1 && keys[recentIndex] == key) {
            return recentIndex
        }
        recentIndex = super.rank(key)
        return recentIndex
    }
}

class CacheLinkedListST<K : Any, V : Any> : LinkedListST<K, V>() {
    private var recentNode: Node<K, V>? = null

    override fun get(key: K): V? {
        if (recentNode != null && recentNode!!.key == key) {
            return recentNode!!.value
        }
        var node = first
        while (node != null) {
            if (node.key == key) {
                recentNode = node
                return node.value
            }
            node = node.next
        }
        return null
    }

    override fun put(key: K, value: V) {
        if (recentNode != null && recentNode!!.key == key) {
            recentNode!!.value = value
            return
        }
        if (first == null) {
            first = Node(key, value)
            size++
        } else {
            var node = first
            while (node != null) {
                if (node.key == key) break
                node = node.next
            }
            if (node == null) {
                val newNode = Node(key, value, first)
                first = newNode
                size++
                recentNode = newNode
            } else {
                node.value = value
                recentNode = node
            }
        }
    }

    override fun delete(key: K) {
        if (recentNode != null && recentNode!!.key == key) {
            recentNode = null
        }
        super.delete(key)
    }
}

fun main() {
    testOrderedST(CacheArrayOrderedST())
    testST(CacheLinkedListST())

    val time1 = spendTimeMillis {
        frequencyCounter(In("./data/tale.txt"), 0, ArrayOrderedST())
    }
    println("$time1 ms")
    val time2 = spendTimeMillis {
        frequencyCounter(In("./data/tale.txt"), 0, CacheArrayOrderedST())
    }
    println("$time2 ms")
    val time3 = spendTimeMillis {
        frequencyCounter(In("./data/tale.txt"), 7, LinkedListST())
    }
    println("$time3 ms")
    val time4 = spendTimeMillis {
        frequencyCounter(In("./data/tale.txt"), 7, CacheLinkedListST())
    }
    println("$time4 ms")
}