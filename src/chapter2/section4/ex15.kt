package chapter2.section4

import extensions.inputPrompt
import extensions.readAllInts

/**
 * 设计一个程序，在线性时间内检测数组pq[]是否是一个面向最小元素的堆
 */
fun <T : Comparable<T>> ex15(pq: Array<T>): Boolean {
    for (i in 0 until pq.size / 2) {
        if (pq[i] > pq[2 * i + 1]) {
            return false
        }
        if (2 * i + 2 < pq.size && pq[i] > pq[2 * i + 2]) {
            return false
        }
    }
    return true
}

fun main() {
    inputPrompt()
    val array = readAllInts().toTypedArray()
    println(ex15(array))
}