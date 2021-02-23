package chapter1.section3

import extensions.inputPrompt
import extensions.readAllStrings
import extensions.readInt

/**
 * 删除单向链表的第k个节点
 */
fun <T> SinglyLinkedList<T>.delete(k: Int) {
    //k从1开始计数
    require(k >= 1)
    if (isEmpty()) throw NoSuchElementException()
    if (k == 1) {
        first = first?.next
        size--
        return
    }
    var node = first
    var i = 0
    while (node?.next != null) {
        //因为要删除的是node.next元素，所有i要先加一
        if (++i == k - 1) {
            node.next = node.next?.next
            size--
            return
        } else {
            node = node.next
        }
    }
    throw NoSuchElementException()
}

fun main() {
    inputPrompt()
    val k = readInt()
    val array = readAllStrings()
    val list = SinglyLinkedList<String>()
    list.addAll(array.iterator())
    println("origin list = ${list.joinToString()}")
    list.delete(k)
    list.checkSize()
    println("end list    = ${list.joinToString()}")
}