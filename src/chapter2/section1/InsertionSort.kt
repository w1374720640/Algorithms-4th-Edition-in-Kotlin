package chapter2.section1

import chapter2.less
import chapter2.swap

/**
 * 插入排序
 */
fun <T : Comparable<T>> insertionSort(array: Array<T>) {
    for (i in 1 until array.size) {
        for (j in i downTo 1) {
            if (!array.less(j, j - 1)) break
            array.swap(j, j - 1)
        }
    }
}

/**
 * 对数组中的指定范围进行插入排序（范围包括start和end）
 */
fun <T : Comparable<T>> insertionSort(array: Array<T>, start: Int, end: Int) {
    for (i in start + 1..end) {
        for (j in i downTo start + 1) {
            if (array.less(j, j - 1)) {
                array.swap(j, j - 1)
            } else {
                break
            }
        }
    }
}

fun main() {
    sortingMethodMainFunTemplate("Insertion Sort", ::insertionSort, false)
}