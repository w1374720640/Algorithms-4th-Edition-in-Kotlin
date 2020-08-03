package chapter2.section3

import chapter2.showSortingProcess
import chapter2.swap
import extensions.*

fun <T : Comparable<T>> quick3WaySort(array: Array<T>) {
    quick3WaySort(array, 0, array.size - 1)
}

fun <T : Comparable<T>> quick3WaySort(array: Array<T>, start: Int, end: Int) {
    if (start >= end) return
    var lowerThan = start
    var greaterThan = end
    var i = start + 1
    val value = array[start]
    while (i <= greaterThan) {
        val result = array[i].compareTo(value)
        when {
            result < 0 -> {
                array.swap(lowerThan, i)
                lowerThan++
                i++
            }
            result > 0 -> {
                array.swap(greaterThan, i)
                greaterThan--
            }
            else -> i++
        }
    }
    quick3WaySort(array, start, lowerThan - 1)
    quick3WaySort(array, greaterThan + 1, end)
}

fun main() {
    inputPrompt()
    val size = readInt("size: ")
    val delay = readLong("delay: ")
    val array = Array(size) { random(5).toDouble() }
    showSortingProcess(array, ::quick3WaySort, delay, true)
    delayExit()
}