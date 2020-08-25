package chapter2.section3

import chapter2.compare
import chapter2.less
import chapter2.section1.cornerCases
import chapter2.section1.displaySortingProcessTemplate
import chapter2.swap
import edu.princeton.cs.algs4.StdRandom

/**
 * 去除内循环while中的边界检查
 * 由于切分元素本身就是一个哨兵（v不可能小于a[start]），左侧边界的检查是多余的
 * 要去掉另一个检查，可以在打乱数组后将数组的最大元素放在a[length-1]中
 * 该元素永远不会移动（除非和相等的元素交换），可以在所有包含它的子数组中成为哨兵
 * 注意：在处理内部子数组时，右子数组中最左侧的元素可以作为左子数组右边界的哨兵
 */
fun <T : Comparable<T>> ex17QuickSort(array: Array<T>) {
    //消除对输入的依赖
    StdRandom.shuffle(array)
    ex17QuickSortWithOriginalArray(array)
}

/**
 * 用原始数组排序，不打乱
 */
fun <T : Comparable<T>> ex17QuickSortWithOriginalArray(array: Array<T>) {
    if (array.size < 2) return
    var maxValue = array[0]
    var maxIndex = 0
    for (i in 1 until array.size) {
        if (array.compare(i, maxValue) > 0) {
            maxValue = array[i]
            maxIndex = i
        }
    }
    array.swap(maxIndex, array.size - 1)
    ex17QuickSort(array, 0, array.size - 1)
}

fun <T : Comparable<T>> ex17QuickSort(array: Array<T>, start: Int, end: Int) {
    if (start >= end) return
    val mid = ex17Partition(array, start, end)
    ex17QuickSort(array, start, mid - 1)
    ex17QuickSort(array, mid + 1, end)
}

fun <T : Comparable<T>> ex17Partition(array: Array<T>, start: Int, end: Int): Int {
    var i = start
    var j: Int = end + 1
    while (true) {
        while (array.compare(++i, start) < 0) {
            //省略右边界的检查
        }
        while (array.compare(--j, start) > 0) {
            //左边界的检查可以直接去掉
        }
        if (i >= j) break
        if (i != j) array.swap(i, j)
    }
    if (j != start) array.swap(start, j)
    return j
}

fun main() {
    cornerCases(::ex17QuickSortWithOriginalArray)
    displaySortingProcessTemplate("Quick Sort", ::ex17QuickSortWithOriginalArray)
}