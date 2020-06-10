package capter1.exercise1_3

import extensions.inputPrompt
import extensions.readAllInts

fun max(node: Node<Int>?, max: Int = 0): Int {
    if (node == null) return max
    return max(node.next, if (node.item > max) node.item else max)
}

fun main() {
    inputPrompt()
    val array = readAllInts().toTypedArray()
    val list = SinglyLinkedList<Int>()
    list.addAll(array.iterator())
    println("list = ${list.joinToSting()}")
    println("list.max() = ${max(list.first)}")
}