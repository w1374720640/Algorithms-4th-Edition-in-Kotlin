package chapter5.section1

import chapter2.swap
import extensions.shuffle

/**
 * 数组排序
 * 编写一个方法，使用三向字符串快速排序处理以整型数组作为键的情况。
 *
 * 解：字符串可以理解为字符型数组，将字符替换成整数即可
 */
fun ex14_ArraySort(array: Array<IntArray>) {
    array.shuffle()
    ex14_ArraySort(array, 0, array.size - 1, 0)
}

private fun ex14_ArraySort(array: Array<IntArray>, low: Int, high: Int, d: Int) {
    // 不使用插入排序优化
    if (low >= high) return
    var i = low + 1
    var j = low
    var k = high
    while (i <= k) {
        // 这里和三向字符串快速排序不同，以j位置为基准点，每个位置都和基准比较
        val result = compare(array, i, j, d)
        when {
            result > 0 -> array.swap(i, k--)
            result < 0 -> array.swap(i++, j++)
            else -> i++
        }
    }
    ex14_ArraySort(array, low, j - 1, d)
    if (array[j].size > d + 1) ex14_ArraySort(array, j, i - 1, d + 1)
    ex14_ArraySort(array, i, high, d)
}

private fun compare(array: Array<IntArray>, i: Int, j: Int, d: Int): Int {
    return when {
        d >= array[i].size && d >= array[j].size -> 0
        d >= array[i].size -> -1
        d >= array[j].size -> 1
        else -> array[i][d].compareTo(array[j][d])
    }
}

fun main() {
    val array = arrayOf(
            intArrayOf(1, 2, 3, 4),
            intArrayOf(2, 3, 4, 5),
            intArrayOf(2, 2, 3, 4),
            intArrayOf(1, 2, 3, 4),
            intArrayOf(5, 4, 3, 2),
            intArrayOf(1, 2, 3, 4),
            intArrayOf(1, 2),
            intArrayOf(2, 4, 3),
            intArrayOf(1, 2, 3, 4),
            intArrayOf(2, 4, 3)
    )
    ex14_ArraySort(array)
    println(array.joinToString(separator = "\n") { it.joinToString() })
}