package chapter2.section3

import chapter2.compare
import chapter2.section1.sortingMethodMainFunTemplate
import chapter2.swap
import edu.princeton.cs.algs4.StdRandom

/**
 * 快速排序基础实现
 *
 * 具体实现为：从第二个值开始向右循环，找到一个大于第一个值的位置
 * 从末尾向左循环，找到一个小于第一个值的位置，交换两个位置上的值
 * 继续循环，直到左右两侧相遇，数组遍历完成
 * 交换左侧的第一位和最后一位，以左侧最后一位为分界线，左边都小于等于它，右边都大于等于它
 * 递归排序左右两侧
 */
fun <T : Comparable<T>> quickSort(array: Array<T>) {
    //消除对输入的依赖
    StdRandom.shuffle(array)
    quickSort(array, 0, array.size - 1)
}

/**
 * 用原始数组排序，不打乱
 */
fun <T : Comparable<T>> quickSortWithOriginalArray(array: Array<T>) {
    quickSort(array, 0, array.size - 1)
}

fun <T : Comparable<T>> quickSort(array: Array<T>, start: Int, end: Int) {
    if (start >= end) return
    val mid = partition(array, start, end)
    quickSort(array, start, mid - 1)
    quickSort(array, mid + 1, end)
}

fun <T : Comparable<T>> partition(array: Array<T>, start: Int, end: Int): Int {
    var i = start
    var j = end + 1
    while (true) {
        while (array.compare(++i, start) < 0) {
            if (i == end) break
        }
        while (array.compare(--j, start) > 0) {
            if (j == start) break
        }
        if (i >= j) break
        if (i != j) array.swap(i, j)
    }
    if (j != start) array.swap(start, j)
    return j
}

fun main() {
    sortingMethodMainFunTemplate("Quick Sort", ::quickSortWithOriginalArray)
}