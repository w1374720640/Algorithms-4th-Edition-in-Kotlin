package chapter2.section3

import chapter2.compare
import chapter2.getDoubleArray
import chapter2.section1.checkAscOrder
import chapter2.section1.doubleGrowthTest
import chapter2.section1.insertionSort
import chapter2.swap
import kotlin.math.log2

/**
 * 三取样切分
 */
fun <T : Comparable<T>> quickSort3Select(array: Array<T>) {
    quickSort3Select(array, 0, array.size - 1)
}

fun <T : Comparable<T>> quickSort3Select(array: Array<T>, start: Int, end: Int) {
    if (start >= end) return
    if (end - start + 1 < 15) {
        insertionSort(array, start, end)
        return
    }
    val mid = partition3Select(array, start, end)
    quickSort3Select(array, start, mid - 1)
    quickSort3Select(array, mid + 1, end)
}

fun <T : Comparable<T>> partition3Select(array: Array<T>, start: Int, end: Int): Int {
    val value = midOf(array[start], array[start + 1], array[start + 2])
    var index = start
    repeat(3) {
        if (array[start + it] == value) {
            index = start + it
        }
    }
    //将中值放到第一位，循环结束后将第一位与中间位置调换，保证左侧都小于等于中值，右侧都大于等于中值
    if (index != start) {
        array.swap(start, index)
    }
    var i = start
    var j = end + 1
    while (true) {
        while (array.compare(++i, value) < 0) {
            if (i >= end) break
        }
        while (array.compare(--j, value) > 0) {
            if (j <= start) break
        }
        if (i >= j) break
        array.swap(i, j)
    }
    if (j != start) array.swap(start, j)
    return j
}

/**
 * 返回三个数大小的中间值
 */
fun <T : Comparable<T>> midOf(a: T, b: T, c: T): T {
    return when {
        (a >= b && a <= c) || (a <= b && a >= c) -> a
        (b >= a && b <= c) || (b >= c && b <= a) -> b
        else -> c
    }
}

fun main() {
//    val array = getDoubleArray(1000)
//    quickSort3Select(array)
//    println("isAscOrder=${array.checkAscOrder()}")

    println("quickSortWithOriginalArray:")
    doubleGrowthTest(1000_0000, ::quickSortWithOriginalArray) { N -> N * log2(N.toDouble()) }
    println("quickSort3Select:")
    doubleGrowthTest(1000_0000, ::quickSort3Select) { N -> N * log2(N.toDouble()) }
}