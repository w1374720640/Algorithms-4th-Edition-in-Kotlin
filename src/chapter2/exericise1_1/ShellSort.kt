package chapter2.exericise1_1

import chapter2.less
import chapter2.swap

/**
 * 希尔排序
 */
fun <T: Comparable<T>> shellSort(array: Array<T>) {
    var h = if (array.size < 3) 1 else array.size / 3
    while (h >= 1) {
        for (i in h until array.size) {
            for (j in i downTo h step h) {
                if (array.less(j - h, j)) break
                array.swap(j, j - h)
            }
        }
        h = if (h == 2) 1 else h / 3
    }
}

fun main() {
    sortingMethodMainFunTemplate("Shell Sort", ::shellSort)
}