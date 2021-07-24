package chapter1.section3

import edu.princeton.cs.algs4.Queue
import extensions.inputPrompt
import extensions.readInt

/**
 * Josephus问题。
 * 在这个古老的问题中，N个身陷绝境的人一致同意通过以下方式减少生存人数。
 * 他们围坐成一圈（位置记为0到N-1）并从第一个人开始报数，报到M的人会被杀死，直到最后一个人留下来。
 * 传说中Josephus找到了不会被杀死的位置。
 * 编写一个Queue的用例Josephus，从命令行接受N和M并打印出人们被杀死的顺序（这也将显示Josephus在圈中的位置）。
 */
fun main() {
    inputPrompt()
    val result = Queue<Int>()
    var origin = Queue<Int>()
    val N = readInt("N: ")
    val M = readInt("M: ")
    require(N > 1 && M > 1) { "N and M should greater than 1" }
    (0 until N).forEach { origin.enqueue(it) }
    var count = 1
    while (origin.size() > 1) {
        val last = Queue<Int>()
        origin.forEach {
            if (count++ % M == 0) {
                result.enqueue(it)
            } else {
                last.enqueue(it)
            }
        }
        origin = last
    }
    result.enqueue(origin.dequeue())
    println("result = ${result.joinToString()}")
}