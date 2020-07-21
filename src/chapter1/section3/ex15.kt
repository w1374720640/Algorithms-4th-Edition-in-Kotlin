package chapter1.section3

import edu.princeton.cs.algs4.Queue
import extensions.inputPrompt
import extensions.readAllStrings
import extensions.readInt

//接收参数k，并打印出标准输入中倒数第k个字符串
fun main() {
    inputPrompt()
    val queue = Queue<String>()
    val k = readInt()
    val array = readAllStrings()
    array.forEach { queue.enqueue(it) }
    var value: String? = null
    var index = 0
    val iterator = queue.iterator()
    while (iterator.hasNext()) {
        if (index == queue.size() - k) {
            value = iterator.next()
            break
        } else {
            iterator.next()
            index++
        }
    }
    if (value == null) {
        println("not found")
    } else {
        println(value)
    }
}