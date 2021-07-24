package chapter1.section3

import extensions.inputPrompt
import extensions.readAllStrings
import extensions.readString

/**
 * 返回链表中某个键的索引，类似于index()方法
 */
fun <T> SinglyLinkedList<T>.find(key: T): Int {
    var index = 0
    forEach {
        if (key == it) {
            return index
        }
        index++
    }
    return -1
}

fun main() {
    inputPrompt()
    val key = readString()
    val array = readAllStrings()
    val list = SinglyLinkedList<String>()
    list.addAll(array.iterator())
    println("key = $key")
    println("list = ${list.joinToString()}")
    println("find index = ${list.find(key)}")
}