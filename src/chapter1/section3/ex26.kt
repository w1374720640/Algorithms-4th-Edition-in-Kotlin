package chapter1.section3

import extensions.inputPrompt
import extensions.readAllStrings
import extensions.readString

//删除链表中所有值为key的节点
fun <T> SinglyLinkedList<T>.remove(key: T) {
    var node = first
    if (node?.item == key) {
        first = first?.next
    }
    while (node?.next != null) {
        if (node.next?.item == key) {
            node.next = node.next?.next
        } else {
            node = node.next
        }
    }
}

fun main() {
    inputPrompt()
    val key = readString()
    val array = readAllStrings()
    val list = SinglyLinkedList<String>()
    list.addAll(array.iterator())
    println("key = $key")
    println("origin list = ${list.joinToSting()}")
    list.remove(key)
    println("end list = ${list.joinToSting()}")
}