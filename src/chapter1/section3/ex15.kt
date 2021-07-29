package chapter1.section3

import edu.princeton.cs.algs4.Queue
import extensions.inputPrompt
import extensions.readAllStrings
import extensions.readInt

/**
 * 编写一个Queue的用例，接受一个命令行参数k并打印出标准输入中的倒数第k个字符串
 * （假设标准输入中至少有k个字符串）
 */
fun main() {
    inputPrompt()
    val queue = Queue<String>()
    val k = readInt("k: ")
    val array = readAllStrings("array: ")
    check(k > 0 && k <= array.size)
    array.forEach {
        queue.enqueue(it)
        if (queue.size() > k) {
            queue.dequeue()
        }
    }
    println(queue.dequeue())
}