package capter1.exercise1_3

import edu.princeton.cs.algs4.Queue
import extensions.inputPrompt
import extensions.readInt

/**
 * 一共有N个人，编号从0~N-1，编号为0的人从1开始报数，报到M的整数倍的人会被杀死，
 * 一圈结束后从第一位继续报数，直到最后一个人留下来
 * 使用Queue找出不同N和M中，不会被杀死的人
 */
fun main() {
    inputPrompt()
    val result = Queue<Int>()
    var origin = Queue<Int>()
    val N = readInt()
    val M = readInt()
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