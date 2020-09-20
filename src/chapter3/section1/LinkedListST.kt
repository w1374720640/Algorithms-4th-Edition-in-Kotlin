package chapter3.section1

import extensions.inputPrompt
import extensions.readInt
import extensions.readString
import extensions.safeCall

/**
 * 基于链表的无序符号表
 */
open class LinkedListST<K : Any, V : Any> : ST<K, V> {
    protected var first: Node<K, V>? = null
    protected var size = 0

    class Node<K : Any, V : Any>(val key: K, var value: V, var next: Node<K, V>? = null) {
        override fun equals(other: Any?): Boolean {
            if (other == null) return false
            if (this === other) return true
            if (other !is Node<*, *>) return false
            return key == other.key
        }

        override fun hashCode(): Int {
            return key.hashCode()
        }
    }

    override fun put(key: K, value: V) {
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
            } else {
                node.value = value
            }
        }
    }

    override fun get(key: K): V? {
        var node = first
        while (node != null) {
            if (node.key == key) {
                return node.value
            }
            node = node.next
        }
        return null
    }

    override fun delete(key: K) {
        if (first == null) {
            throw NoSuchElementException()
        }
        if (first!!.key == key) {
            first = first!!.next
            size--
            return
        }
        var node = first!!
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
        return get(key) != null
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun size(): Int {
        return size
    }

    override fun keys(): Iterable<K> {
        return object : Iterable<K> {
            override fun iterator(): Iterator<K> {
                return object : Iterator<K> {
                    var node = first
                    override fun hasNext(): Boolean {
                        return node != null
                    }

                    override fun next(): K {
                        if (node == null) throw NoSuchElementException()
                        val key = node!!.key
                        node = node!!.next
                        return key
                    }
                }
            }
        }
    }
}

fun main() {
    testST(LinkedListST())

    inputPrompt()
    val st = LinkedListST<String, String>()
    println("Please input commands:")
    println("0: exit, 1: put, 2: get, 3: delete, 4: contains, 5: isEmpty, 6: size, 7: keys")
    while (true) {
        safeCall {
            when (readInt("command: ")) {
                0 -> return
                1 -> {
                    val key = readString("put: key=")
                    val value = readString("value=")
                    st.put(key, value)
                }
                2 -> {
                    val key = readString("get: key=")
                    println(st.get(key))
                }
                3 -> {
                    val key = readString("delete: key=")
                    st.delete(key)
                }
                4 -> {
                    val key = readString("contains: key=")
                    println(st.contains(key))
                }
                5 -> println("isEmpty=${st.isEmpty()}")
                6 -> println("size=${st.size()}")
                7 -> println("keys=${st.keys().joinToString()}")
            }
        }
    }
}