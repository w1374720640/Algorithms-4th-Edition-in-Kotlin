package chapter1.section3

import extensions.inputPrompt
import extensions.readAllInts

fun SinglyLinkedList<Int>.max(): Int {
    var max = 0
    forEach {
        if (it > max) {
            max = it
        }
    }
    return max
}

fun main() {
    inputPrompt()
    val array = readAllInts()
    val list = SinglyLinkedList<Int>()
    list.addAll(array.iterator())
    println("list = ${list.joinToString()}")
    println("list.max() = ${list.max()}")
}