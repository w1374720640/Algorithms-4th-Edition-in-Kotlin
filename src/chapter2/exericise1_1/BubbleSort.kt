package chapter2.exericise1_1

import chapter2.less
import chapter2.swap

/**
 * 冒泡排序
 */
fun <T: Comparable<T>> bubbleSort(array: Array<T>) {
    for (i in array.size - 1 downTo 1) {
        for (j in 0 until i) {
            if (array.less(j + 1, j)) {
                array.swap(j + 1, j)
            }
        }
    }
}

fun main() {
    sortingMethodMainFunTemplate("Bubble Sort", ::bubbleSort, false)
}