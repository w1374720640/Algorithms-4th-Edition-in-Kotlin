package chapter1.exercise1_4

import extensions.inputPrompt
import extensions.readAllInts

/**
 * 找出数组中两者之差的绝对值最小的两个数，要求最坏情况下所需时间应该是线性对数级别的
 */
fun ex16(array: IntArray): Pair<Int, Int> {
    array.sort()
    var min = Int.MAX_VALUE
    var left = 0
    var right = 0
    for (i in 0 until array.size - 1) {
        if (array[i + 1] - array[i] < min) {
            left = array[i]
            right = array[i + 1]
            min = array[i + 1] - array[i]
        }
    }
    return left to right
}

fun main() {
//    inputPrompt()
//    val array = readAllInts()
//    val pair = ex16(array)
//    println("left = ${pair.first} right = ${pair.second} min = ${pair.second - pair.first}")
    timeRatio { ex16(it) }
}