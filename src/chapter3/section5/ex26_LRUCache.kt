package chapter3.section5

import chapter1.section3.*
import chapter3.section4.LinearProbingHashST

/**
 * LRU缓存
 * 创建一个支持以下操作的数据结构：访问和删除
 * 访问操作会将不存在于数据结构中的元素插入，删除操作会删除并返回最近最少访问的元素
 * 提示：将元素按照访问的先后顺序保存在一条双向链表中，并保存指向开头和结尾元素的指针
 * 将元素和元素在链表中的位置分别作为键和相应的值保存在一张符号表中
 * 当你访问一个元素时，将它从链表中删除并重新插入链表的头部
 * 当你删除一个元素时，将它从链表的尾部和符号表中删除
 *
 * 解：符号表的值不应该是元素在链表中的位置，而是键绑定的其他对象
 * 和普通符号表的区别是：删除方法不接受参数，直接删除最近最少访问的元素
 */
class LRUCache<K : Any, V : Any> {
    private val linkedList = DoublyLinkedList<K>()
    private val st = LinearProbingHashST<K, V>()

    fun get(key: K): V? {
        val value = st.get(key)
        if (value != null) {
            linkedList.moveToFront(key)
        }
        return value
    }

    fun put(key: K, value: V) {
        st.put(key, value)
        linkedList.moveToFront(key)
    }

    fun delete() {
        if (linkedList.isEmpty()) return
        val deleteKey = linkedList.deleteTail()
        st.delete(deleteKey)
    }

    fun keys(): Iterable<K> {
        return object : Iterable<K> {
            override fun iterator(): Iterator<K> {
                return linkedList.forwardIterator()
            }
        }
    }
}

/**
 * 参考练习1.3.40单向链表的方法
 */
fun <T> DoublyLinkedList<T>.moveToFront(value: T) {
    if (isEmpty()) {
        first = DoubleNode(value)
        last = first
        return
    }
    var node = first
    while (node != null) {
        if (node.item == value) {
            node.previous?.next = node.next
            node.next?.previous = node.previous
            if (node == first) first = node.next
            if (node == last) last = node.previous
            break
        }
        node = node.next
    }
    addHeader(value)
}

fun main() {
    val lruCache = LRUCache<Int, String>()
    for (i in 0..5) {
        lruCache.put(i, i.toString())
    }
    println(lruCache.keys().joinToString())
    lruCache.get(3)
    println(lruCache.keys().joinToString())
    lruCache.put(0, "0000")
    println(lruCache.keys().joinToString())
    lruCache.delete()
    println(lruCache.keys().joinToString())
}