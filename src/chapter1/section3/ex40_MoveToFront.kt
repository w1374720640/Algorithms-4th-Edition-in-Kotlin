package chapter1.section3

import extensions.inputPrompt
import extensions.readString

/**
 * 前移编码
 * 从标准输入读取一串字符，使用链表保存这些字符并清除重复字符。
 * 当你读取了一个从未见过的字符时，将它插入表头。
 * 当你读取了一个重复的字符时，将它从链表中删去并再次插入表头。
 * 将你的程序命名为MoveToFront：
 * 它实现了著名的前移编码策略，这种策略假设最近访问过的元素很可能会再次访问，因此可以用于缓存、数据压缩等许多场景。
 */
fun <T> SinglyLinkedList<T>.moveToFront(value: T) {
    var preNode: SinglyLinkedList.Node<T>? = null
    var node = first
    while (node != null) {
        if (node.item == value) {
            if (preNode == null) {
                first = node.next
            } else {
                preNode.next = node.next
            }
            size--
        } else {
            preNode = node
        }
        node = node.next
    }
    val newNode = SinglyLinkedList.Node(value)
    newNode.next = first
    first = newNode
    size++
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
            println("list = ${list.joinToString()}")
        }
    }
}