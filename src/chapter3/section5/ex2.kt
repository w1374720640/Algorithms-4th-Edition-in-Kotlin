package chapter3.section5

import edu.princeton.cs.algs4.In
import edu.princeton.cs.algs4.Queue

/**
 * 删除[chapter3.section1.SequentialSearchST]中的值相关的所有代码来实现SequentialSearchSET
 */
class SequentialSearchSET<K : Any> : SET<K> {
    var first: Node<K>? = null
    var size = 0

    class Node<K>(val key: K, var next: Node<K>? = null)

    override fun add(key: K) {
        if (contains(key)) return
        first = Node(key, first)
        size++
    }

    override fun delete(key: K) {
        if (isEmpty()) throw NoSuchElementException()
        var node = first!!
        if (node.key == key) {
            first = node.next
            size--
            return
        }
        var nextNode = node.next
        while (nextNode != null) {
            if (nextNode.key == key) {
                node.next = nextNode.next
                size--
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
            queue.enqueue(node.key)
            node = node.next
        }
        return queue
    }
}

fun main() {
    testSET(SequentialSearchSET())

    val st = SequentialSearchSET<String>()
    val input = In("./data/tinyTale.txt")
    while (!input.isEmpty) {
        st.add(input.readString())
    }
    println(st.keys().joinToString())
}