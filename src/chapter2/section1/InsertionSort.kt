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

fun main() {
    sortingMethodMainFunTemplate("Insertion Sort", ::insertionSort, false)
}