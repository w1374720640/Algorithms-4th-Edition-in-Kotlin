package chapter2.section1

import chapter2.less
import chapter2.swap

/**
 * 希尔排序
 */
fun <T : Comparable<T>> shellSort(array: Array<T>) {
    var h = 1
    while (h < array.size / 3) h = 3 * h + 1 // 4 13 40 121 364
    while (h >= 1) {
        for (i in h until array.size) {
            for (j in i downTo h step h) {
                if (!array.less(j, j - h)) break
                array.swap(j, j - h)
            }
        }
        h /= 3
    }
}

fun main() {
    displaySortingProcessTemplate("Shell Sort", ::shellSort)
}