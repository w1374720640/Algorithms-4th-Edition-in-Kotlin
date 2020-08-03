package chapter2.section3

import chapter2.compare
import chapter2.section1.sortingMethodMainFunTemplate
import chapter2.swap
import edu.princeton.cs.algs4.StdRandom

/**
 * 快速排序基础实现
 */
fun <T : Comparable<T>> quickSort(array: Array<T>) {
    //消除对输入的依赖
//    StdRandom.shuffle(array)
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
    sortingMethodMainFunTemplate("Quick Sort", ::quickSort)
}