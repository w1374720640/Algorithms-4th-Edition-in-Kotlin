package chapter1.section3

import extensions.inputPrompt
import extensions.readAllStrings
import extensions.readInt
import extensions.readString

/**
 * 在指定节点后面插入一个节点
 */
fun <T> SinglyLinkedList<T>.insertAfter(afterNode: SinglyLinkedList.Node<T>, insertNode: SinglyLinkedList.Node<T>) {
    var node = first
    while (node != null) {
        if (node === afterNode) {
            insertNode.next = node.next
            node.next = insertNode
            size++
            break
        } else {
            node = node.next
        }
    }
}

fun main() {
    inputPrompt()
    val index = readInt()
    val value = readString()
    val array = readAllStrings()
    val list = SinglyLinkedList<String>()
    list.addAll(array.iterator())
    println("index = $index value = $value")
    println("origin list = ${list.joinToString()}")
    val afterNode = list.getNode(index)
    afterNode?.let {
        list.insertAfter(it, SinglyLinkedList.Node(value))
    }
    list.checkSize()
    println("end list = ${list.joinToString()}")
}