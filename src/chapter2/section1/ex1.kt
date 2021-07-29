package chapter2.section1

import chapter2.showSortingProcess
import extensions.delayExit

/**
 * 按照算法2.1所示轨迹的格式给出选择排序是如何将数组 E A S Y Q U E S T I O N 排序的。
 */
fun ex1(array: Array<Char>, delay: Long) {
    val doubleArray = Array(array.size) { array[it].toDouble() }
    showSortingProcess(doubleArray, ::selectionSort, delay, true)
    delayExit()
}

fun main() {
    ex1(arrayOf('E', 'A', 'S', 'Y', 'Q', 'U', 'E', 'S', 'T', 'I', 'O', 'N'), 500)
}