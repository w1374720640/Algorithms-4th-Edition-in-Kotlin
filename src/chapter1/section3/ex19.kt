package chapter1.section3

import extensions.inputPrompt
import extensions.readAllStrings

/**
 * 删除单向链表的尾节点
 */
fun <T> SinglyLinkedList<T>.deleteLast() {
    var node = first
    if (node?.next == null) {
        first = null
        size = 0
        return
    }
    while (node?.next?.next != null) {
        node = node.next
    }
    node?.next = null
    size--
}

//向单向链表中添加数据
fun <T> SinglyLinkedList<T>.add(value: T) {
    if (first == null) {
        first = SinglyLinkedList.Node(value)
    } else {
        var lastNode = first
        while (lastNode?.next != null) {
            lastNode = lastNode.next
        }
        lastNode?.next = SinglyLinkedList.Node(value)
    }
    size++
}

fun <T> SinglyLinkedList<T>.addAll(iterator: Iterator<T>) {
    //先把数据源拼接成一条链，再把链头附加到原来的链尾上
    var firstNode: SinglyLinkedList.Node<T>? = null
    var lastNode: SinglyLinkedList.Node<T>? = null
    while (iterator.hasNext()) {
        val node = SinglyLinkedList.Node(iterator.next())
        if (firstNode == null) {
            firstNode = node
            lastNode = node
        } else {
            lastNode?.next = node
            lastNode = node
        }
        size++
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

fun <T> SinglyLinkedList<T>.getNode(index: Int): SinglyLinkedList.Node<T>? {
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

fun <T> SinglyLinkedList<T>.isEmpty(): Boolean {
    return first == null
}

/**
 * 检查size的大小是否正确
 */
fun <T> SinglyLinkedList<T>.checkSize() {
    var i = 0
    forEach { _ -> i++ }
    check(i == size)
}

fun main() {
    inputPrompt()
    val array = readAllStrings()
    val list = SinglyLinkedList<String>()
    list.addAll(array.iterator())
    list.checkSize()
    println("origin list = ${list.joinToString()}")
    list.deleteLast()
    list.checkSize()
    println("end list    = ${list.joinToString()}")
}