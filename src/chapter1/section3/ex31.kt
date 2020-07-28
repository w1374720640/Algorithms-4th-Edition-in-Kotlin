package chapter1.section3

import extensions.readInt
import extensions.readString
import extensions.safeCall

fun <T> DoublyLinkedList<T>.addHeader(value: T) {
    val node = DoubleNode(value, next = first)
    if (first == null) {
        first = node
        last = node
    } else {
        first!!.previous = node
        first = node
    }
}

fun <T> DoublyLinkedList<T>.addTail(value: T) {
    val node = DoubleNode(value, previous = last)
    if (last == null) {
        first = node
        last = node
    } else {
        last!!.next = node
        last = node
    }
}

fun <T> DoublyLinkedList<T>.deleteHeader(): T {
    if (first == null) throw NoSuchElementException("List is empty")
    val value = first!!.item
    val node = first!!.next
    if (node == null) {
        first = null
        last = null
    } else {
        node.previous = null
        first = node
    }
    return value
}

fun <T> DoublyLinkedList<T>.deleteTail(): T {
    if (last == null) throw NoSuchElementException("List is empty")
    val value = last!!.item
    val node = last!!.previous
    if (node == null) {
        first = null
        last = null
    } else {
        node.next = null
        last = node
    }
    return value
}

fun <T> DoublyLinkedList<T>.addBefore(index: Int, value: T) {
    var i = 0
    var node = first
    var previousNode: DoubleNode<T>? = null
    while (node != null) {
        if (i++ == index) {
            val newNode = DoubleNode(value, next = node, previous = previousNode)
            node.previous = newNode
            if (previousNode == null) {
                first = newNode
            } else {
                previousNode.next = newNode
            }
            return
        }
        previousNode = node
        node = node.next
    }
    throw NoSuchElementException("No such element with index: $index")
}

fun <T> DoublyLinkedList<T>.addAfter(index: Int, value: T) {
    var i = 0
    var node = first
    while (node != null) {
        val nextNode = node.next
        if (i++ == index) {
            val newNode = DoubleNode(value, next = nextNode, previous = node)
            node.next = newNode
            if (nextNode == null) {
                last = newNode
            } else {
                nextNode.previous = newNode
            }
            return
        }
        node = nextNode
    }
    throw NoSuchElementException("No such element with index: $index")
}

fun <T> DoublyLinkedList<T>.delete(index: Int): T {
    var i = 0
    var node = first
    var previousNode: DoubleNode<T>? = null
    while (node != null) {
        val nextNode = node.next
        if (i++ == index) {
            if (previousNode == null && nextNode == null) {
                first = null
                last = null
            } else if (previousNode == null) {
                first = nextNode
                first!!.previous = null
            } else if (nextNode == null) {
                last = previousNode
                last!!.next = null
            } else {
                previousNode.next = nextNode
                nextNode.previous = previousNode
            }
            return node.item
        }
        previousNode = node
        node = nextNode
    }
    throw NoSuchElementException("No such element with index: $index")
}

fun <T> DoublyLinkedList<T>.get(index: Int): T {
    var i = 0
    var node = first
    while (node != null) {
        if (i++ == index) {
            return node.item
        }
        node = node.next
    }
    throw NoSuchElementException("No such element with index: $index")
}

fun <T> DoublyLinkedList<T>.size(): Int {
    var size = 0
    var node = first
    while (node != null) {
        size++
        node = node.next
    }
    return size
}

fun <T> DoublyLinkedList<T>.isEmpty() = first == null && last == null

fun <T> DoublyLinkedList<T>.clear() {
    first = null
    last = null
}

/**
 * 正向迭代器
 */
fun <T> DoublyLinkedList<T>.forwardIterator(): Iterator<T> = object : Iterator<T> {
    var node = first

    override fun hasNext(): Boolean {
        return node != null
    }

    override fun next(): T {
        if (node == null) throw NoSuchElementException()
        val value = node!!.item
        node = node!!.next
        return value
    }

}

/**
 * 反向迭代器
 */
fun <T> DoublyLinkedList<T>.reverseIterator(): Iterator<T> = object : Iterator<T> {
    var node = last

    override fun hasNext(): Boolean {
        return node != null
    }

    override fun next(): T {
        if (node == null) throw NoSuchElementException()
        val value = node!!.item
        node = node!!.previous
        return value
    }
}

fun <T> DoublyLinkedList<T>.joinToString(iterator: Iterator<T> = forwardIterator()): String {
    var result = ""
    while (iterator.hasNext()) {
        result += iterator.next().toString() + " "
    }
    return result
}

fun main() {
    fun DoublyLinkedList<String>.println() {
        println("forward traversal list: ${joinToString()}")
        println("reverse traversal list: ${joinToString(reverseIterator())}")
    }

    val list = DoublyLinkedList<String>()
    println("Please input command:")
    println("0: exit, 1: add header, 2: add tail, 3: delete header, 4: delete tail")
    println("5: add before, 6: add after, 7: delete index, 8: get index, 9: get size")
    while (true) {
        when (readInt("command: ")) {
            0 -> return
            1 -> {
                list.addHeader(readString("add header: "))
                list.println()
            }
            2 -> {
                list.addTail(readString("add tail: "))
                list.println()
            }
            3 -> {
                println("delete header")
                safeCall { list.deleteHeader() }
                list.println()
            }
            4 -> {
                println("delete tail")
                safeCall { list.deleteTail() }
                list.println()
            }
            5 -> {
                val index = readInt("add before index: ")
                val value = readString("value: ")
                safeCall { list.addBefore(index, value) }
                list.println()
            }
            6 -> {
                val index = readInt("add after index: ")
                val value = readString("value: ")
                safeCall { list.addAfter(index, value) }
                list.println()
            }
            7 -> {
                safeCall { list.delete(readInt("delete index: ")) }
                list.println()
            }
            8 -> {
                safeCall { println(list.get(readInt("get index: "))) }
            }
            9 -> {
                println("list.size = ${list.size()}")
            }
        }
    }
}
