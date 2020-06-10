package capter1.exercise1_3

import extensions.inputPrompt
import extensions.readAllStrings
import extensions.readString

fun <T> SinglyLinkedList<T>.find(key: T): Boolean {
    forEach {
        if (key == it) {
            return true
        }
    }
    return false
}

fun main() {
    inputPrompt()
    val key = readString()
    val array = readAllStrings()
    val list = SinglyLinkedList<String>()
    list.addAll(array.iterator())
    println("key = ${key}")
    println("list = ${list.joinToSting()}")
    println("find result = ${list.find(key)}")
}