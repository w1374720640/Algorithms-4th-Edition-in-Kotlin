package chapter2.exericise1_1

import chapter2.showSortingProcess
import chapter2.swap
import extensions.*

/**
 * 选择排序
 */
fun <T : Comparable<T>> selectSort(array: Array<T>) {
    for (i in array.indices) {
        var minIndex = i
        for (j in i + 1 until array.size) {
            if (array[j] < array[minIndex]) minIndex = j
        }
        if (minIndex != i) array.swap(i, minIndex)
    }
}

fun main() {
    inputPrompt()
    val size = readInt("size: ")
    val delay = readLong("delay time millis: ")
    //随机数组
    val array = Array(size) { random() }
    //逆序数组
//    val array = Array(size) { (size - it).toDouble() }
    //顺序数组
//    val array = Array(size) { it.toDouble() }
    val swapTimes = showSortingProcess(array, ::selectSort, delay)
    println("Swap $swapTimes times")
    delayExit()
}