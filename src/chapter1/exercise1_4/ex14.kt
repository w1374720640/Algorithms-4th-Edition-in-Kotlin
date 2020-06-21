package chapter1.exercise1_4

import chapter1.exercise1_1.binarySearch

/**
 * 求一个数组中四个数的和为0的组合数量
 */
fun ex14(array: IntArray): Long {
    array.sort()
    var count = 0L
    for (i in array.indices) {
        for (j in i + 1 until array.size) {
            for (k in j + 1 until array.size) {
                if (binarySearch(-array[i] - array[j] - array[k], array) > k) {
                    count++
                }
            }
        }
    }
    return count
}

fun main() {
    timeRatio { ex14(it) }
}