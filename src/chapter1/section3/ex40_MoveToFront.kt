package chapter1.section3

import extensions.inputPrompt
import extensions.readString

/**
 * LRU算法，如果在列表中找到，将数据移至起始位，未找到，在起始位添加
 */
fun <T> SinglyLinkedList<T>.moveToFront(value: T) {
    var preNode: Node<T>? = null
    var node = first
    while (node != null) {
        if (node.item == value) {
            if (preNode == null) {
                first = node.next
            } else {
                preNode.next = node.next
            }
        } else {
            preNode = node
        }
        node = node.next
    }
    val newNode = Node(value)
    newNode.next = first
    first = newNode
}

fun main() {
    inputPrompt()
    val list = SinglyLinkedList<String>()
    println("Enter ':q' to exit")
    while (true) {
        val value = readString()
        if (":q" == value) {
            return
        } else {
            list.moveToFront(value)
            println("list = ${list.joinToSting()}")
        }
    }
}