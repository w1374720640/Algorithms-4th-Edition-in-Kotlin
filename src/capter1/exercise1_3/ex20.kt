package capter1.exercise1_3

import extensions.inputPrompt
import extensions.readAllStrings
import extensions.readInt

/**
 * 删除单向链表的第k个节点
 */
fun <T> SinglyLinkedList<T>.delete(k: Int) {
    //k从1开始计数
    if (k == 1) {
        first = first?.next
        return
    }
    var node = first
    var i = 0
    while (node?.next != null) {
        //因为要删除的是node.next元素，所有i要先加一
        if (++i == k - 1) {
            node.next = node.next?.next
            return
        } else {
            node = node.next
        }
    }
}

fun main() {
    inputPrompt()
    val k = readInt()
    val array = readAllStrings()
    val list = SinglyLinkedList<String>()
    list.addAll(array.iterator())
    println("origin list = ${list.joinToSting()}")
    list.delete(k)
    println("end list    = ${list.joinToSting()}")
}