package chapter1.section3

import extensions.inputPrompt
import extensions.readAllStrings
import extensions.readString

/**
 * 删除链表中所有值为key的节点
 */
fun <T> SinglyLinkedList<T>.remove(key: T) {
    var node = first
    while (node != null) {
        if (node.item == key) {
            size--
            node = node.next
        } else {
            first = node
            break
        }
    }
    if (node == null) return
    var next = node.next
    while (next != null) {
        if (next.item == key) {
            node!!.next = next.next
            next = next.next
            size--
        } else {
            node = next
            next = next.next
        }
    }
}

fun main() {
    inputPrompt()
    val key = readString()
    val array = readAllStrings()
    val list = SinglyLinkedList<String>()
    list.addAll(array.iterator())
    list.checkSize()
    println("key = $key")
    println("origin list = ${list.joinToString()}")
    list.remove(key)
    list.checkSize()
    println("end list = ${list.joinToString()}")
}