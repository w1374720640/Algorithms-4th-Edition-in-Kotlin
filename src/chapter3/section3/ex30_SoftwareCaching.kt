package chapter3.section3

import chapter3.section1.testOrderedST

/**
 * 缓存
 * 修改RedBlackBST的实现，将最近访问的结点Node保存在一个变量中
 * 这样get()或put()在再次访问同一个键时就只需要常数时间了（请参考练习3.1.25）
 */
class CacheRedBlackBST<K : Comparable<K>, V : Any> : RedBlackBST<K, V>() {
    private var recentNode: Node<K, V>? = null

    override fun put(key: K, value: V) {
        if (recentNode != null && recentNode!!.key == key) {
            recentNode!!.value = value
            return
        }
        super.put(key, value)
    }

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

    override fun deleteMin() {
        recentNode = null
        super.deleteMin()
    }

    override fun deleteMax() {
        recentNode = null
        super.deleteMax()
    }

    override fun delete(key: K) {
        recentNode = null
        super.delete(key)
    }
}

fun main() {
    testOrderedST(CacheRedBlackBST())
    testRedBlackBST(CacheRedBlackBST())
}