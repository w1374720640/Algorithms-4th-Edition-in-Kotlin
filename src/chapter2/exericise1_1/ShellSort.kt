package chapter2.exericise1_1

import chapter2.less
import chapter2.swap

/**
 * 希尔排序
 */
fun <T: Comparable<T>> shellSort(array: Array<T>) {
    var h = 1
    while (h < array.size / 3) h = 3 * h + 1 // 4 13 40 121 364
    while (h >= 1) {
        for (i in h until array.size) {
            for (j in i downTo h step h) {
                if (array.less(j, j - h)) {
                    array.swap(j, j - h)
                } else break
            }
        }
        h /= 3
    }
}

fun main() {
    sortingMethodMainFunTemplate("Shell Sort", ::shellSort)
}