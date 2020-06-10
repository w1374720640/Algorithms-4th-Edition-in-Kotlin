package capter1.exercise1_3

import extensions.inputPrompt
import extensions.readAllStrings

/**
 * 删除单向链表的尾节点
 */
fun <T> SinglyLinkedList<T>.deleteLast() {
    var node = first
    if (node?.next == null) {
        first = null
        return
    }
    while (node?.next?.next != null) {
        node = node.next
    }
    node?.next = null
}

//向单向链表中添加数据
fun <T> SinglyLinkedList<T>.add(value: T) {
    if (first == null) {
        first = Node(value)
    } else {
        var lastNode = first
        while (lastNode?.next != null) {
            lastNode = lastNode.next
        }
        lastNode?.next = Node(value)
    }
}

fun <T> SinglyLinkedList<T>.addAll(iterator: Iterator<T>) {
    //先把数据源拼接成一条链，再把链头附加到原来的链尾上
    var firstNode: Node<T>? = null
    var lastNode: Node<T>? = null
    while (iterator.hasNext()) {
        val node = Node(iterator.next())
        if (firstNode == null) {
            firstNode = node
            lastNode = node
        } else {
            lastNode?.next = node
            lastNode = node
        }
    }

    var originNode = first
    if (originNode == null) {
        first = firstNode
    }
    while (originNode?.next != null) {
        originNode = originNode.next
    }
    originNode?.next = firstNode
}

fun <T> SinglyLinkedList<T>.iterator() = object : Iterator<T> {
    private var node = first
    override fun hasNext() = node != null
    override fun next(): T {
        if (node == null) {
            throw NoSuchElementException()
        }
        val result = node!!.item
        node = node!!.next
        return result
    }
}

inline fun <T> SinglyLinkedList<T>.forEach(action: (T) -> Unit) {
    val iterator = iterator()
    while (iterator.hasNext()) {
        action(iterator.next())
    }
}

fun <T> SinglyLinkedList<T>.getNode(index: Int): Node<T>? {
    var i = 0
    var node = first
    while (node != null) {
        if (i++ == index) {
            break
        } else {
            node = node.next
        }
    }
    return node
}

fun <T> SinglyLinkedList<T>.get(index: Int): T? = getNode(index)?.item

fun <T> SinglyLinkedList<T>.size(): Int {
    var size = 0
    forEach { size++ }
    return size
}

fun <T> SinglyLinkedList<T>.joinToSting(): String {
    var result = ""
    forEach { result += it.toString() + " " }
    return result
}

fun main() {
    inputPrompt()
    val array = readAllStrings()
    val list = SinglyLinkedList<String>()
    list.addAll(array.iterator())
    println("origin list = ${list.joinToSting()}")
    list.deleteLast()
    println("end list    = ${list.joinToSting()}")
}