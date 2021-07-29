package chapter1.section3

import extensions.inputPrompt
import extensions.readAllInts

/**
 * 用递归的方式求链表中键最大的节点的值
 */
fun max(node: SinglyLinkedList.Node<Int>?, max: Int = 0): Int {
    if (node == null) return max
    return max(node.next, if (node.item > max) node.item else max)
}

fun main() {
    inputPrompt()
    val array = readAllInts().toTypedArray()
    val list = SinglyLinkedList<Int>()
    list.addAll(array.iterator())
    println("list = ${list.joinToString()}")
    println("list.max() = ${max(list.first)}")
}