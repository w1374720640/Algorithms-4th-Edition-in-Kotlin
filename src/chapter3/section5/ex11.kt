package chapter3.section5

import chapter1.section3.SinglyLinkedList
import chapter1.section3.add
import chapter1.section3.forEach
import chapter1.section3.size
import edu.princeton.cs.algs4.Queue
import kotlin.NoSuchElementException

/**
 * 开发一个和SET相似的类MultiSET，允许出现相等的键，也就是实现了数学上的多重集合
 *
 * 解：在多重集之中，同一个元素可以出现多次
 * 一个元素在多重集里出现的次数称为这个元素在多重集里面的重数（或重次、重复度）
 * 举例来说，{1,2,3}是一个集合，而{1,1,1,2,2,3}不是一个集合，而是一个多重集。
 * 其中元素1的重数是3，2的重数是2，3的重数是1。
 * {1,1,1,2,2,3}的元素个数是6。
 * 多重集中的元素没有顺序分别，{1,1,1,2,2,3}和{1,1,2,1,2,3}是同一个多重集
 *
 * 参考练习3.5.2中[SequentialSearchSET]类的实现，区别是所有值相等的键通过链表链接到同一个结点中
 */
class MultiSET<K : Any> : SET<K> {
    var first: Node<K>? = null
    private var size = 0

    class Node<K : Any>(
            val key: K,
            var next: Node<K>? = null,
            val list: SinglyLinkedList<K> = SinglyLinkedList()) {
        init {
            list.add(key)
        }
    }

    override fun add(key: K) {
        var node = first
        while (node != null) {
            if (node.key == key) {
                node.list.add(key)
                size++
                return
            }
            node = node.next
        }
        first = Node(key, first)
        size++
    }

    override fun delete(key: K) {
        if (isEmpty()) throw NoSuchElementException()
        var node = first!!
        if (node.key == key) {
            first = node.next
            size -= node.list.size()
            return
        }
        var nextNode = node.next
        while (nextNode != null) {
            if (nextNode.key == key) {
                node.next = nextNode.next
                size -= nextNode.list.size()
                return
            }
            node = nextNode
            nextNode = nextNode.next
        }
        throw NoSuchElementException()
    }

    override fun contains(key: K): Boolean {
        var node = first
        while (node != null) {
            if (node.key == key) return true
            node = node.next
        }
        return false
    }

    fun multiNum(key: K): Int {
        var node = first
        while (node != null) {
            if (node.key == key) {
                return node.list.size()
            }
            node = node.next
        }
        return 0
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun size(): Int {
        return size
    }

    override fun keys(): Iterable<K> {
        val queue = Queue<K>()
        var node = first
        while (node != null) {
            node.list.forEach {
                queue.enqueue(it)
            }
            node = node.next
        }
        return queue
    }
}

private fun testMultiSET(set: MultiSET<Int>) {
    require(set.isEmpty())
    set.add(1)
    set.add(2)
    set.add(3)
    check(set.size() == 3)

    set.add(1)
    set.add(1)
    set.add(2)
    check(set.size() == 6)
    check(set.contains(1))
    check(set.multiNum(1) == 3)
    check(set.multiNum(2) == 2)
    check(set.multiNum(3) == 1)

    set.delete(2)
    check(set.size() == 4)
    check(set.multiNum(2) == 0)
    set.delete(1)
    check(set.size() == 1)
    set.delete(3)
    check(set.isEmpty())
    println("MultiSET check succeed.")
}

fun main() {
    testMultiSET(MultiSET())
}