package chapter2.section2

import extensions.delayExit

fun ex3(array: Array<Char>, delay: Long) {
    val doubleArray = Array(array.size) { array[it].toDouble() }
    showMergeSortProcess(doubleArray, ::buttonUpMergeSort, delay)
    delayExit()
}

fun main() {
    ex3(arrayOf('E', 'A', 'S', 'Y', 'Q', 'U', 'E', 'S', 'T', 'I', 'O', 'N'), 500)
}