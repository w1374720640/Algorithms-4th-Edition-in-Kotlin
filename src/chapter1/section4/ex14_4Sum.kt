package chapter1.section4

import chapter1.section1.binarySearch

/**
 * 4-sum
 * 为4-sum设计一个算法。
 */
fun ex14(array: IntArray): Long {
    array.sort()
    var count = 0L
    for (i in array.indices) {
        for (j in i + 1 until array.size) {
            for (k in j + 1 until array.size) {
                //需要注意这里二分查找的结果必须大于k才代表找到
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