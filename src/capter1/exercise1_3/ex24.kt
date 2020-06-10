package capter1.exercise1_3

import extensions.inputPrompt
import extensions.readAllStrings
import extensions.readInt

//删除某个节点的所有后续节点
fun <T> SinglyLinkedList<T>.removeAfter(removeNode: Node<T>) {
    var node = first
    while (node != null) {
        if (node == removeNode) {
            node.next = null
            return
        } else {
            node = node.next
        }
    }
}

fun main() {
    inputPrompt()
    //需要删除的起始位置，从0开始
    val index = readInt()
    val array = readAllStrings()
    val list = SinglyLinkedList<String>()
    list.addAll(array.iterator())
    println("index = $index")
    println("origin list = ${list.joinToSting()}")
    val node = list.getNode(index)
    node?.let { list.removeAfter(it) }
    println("end list = ${list.joinToSting()}")
}