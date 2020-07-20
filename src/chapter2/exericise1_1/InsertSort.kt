package chapter2.exericise1_1

import chapter2.less
import chapter2.swap

/**
 * 插入排序
 */
fun <T : Comparable<T>> insertSort(array: Array<T>) {
    for (i in 1 until array.size) {
        for (j in i downTo 1) {
            if (array.less(j, j - 1)) {
                array.swap(j, j - 1)
            } else break
        }
    }
}

fun main() {
    sortingMethodMainFunTemplate("Insert Sort", ::insertSort, false)
}