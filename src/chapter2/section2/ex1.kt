package chapter2.section2

import extensions.delayExit

fun ex1(array: Array<Char>, delay: Long) {
    val doubleArray = Array(array.size) { array[it].toDouble() }
    showMergeSortProcess(doubleArray, ::topDownMergeSort, delay)
    delayExit()
}

fun main() {
    ex1(arrayOf('A', 'E', 'Q', 'S', 'U', 'Y', 'E', 'I', 'N', 'O', 'S', 'T'), 500)
}