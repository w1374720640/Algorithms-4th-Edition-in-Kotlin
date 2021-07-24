package chapter2.section2

import extensions.delayExit

/**
 * 用自底向上的归并排序解答练习2.2.2
 */
fun ex3(array: Array<Char>, delay: Long) {
    val doubleArray = Array(array.size) { array[it].toDouble() }
    showMergeSortProcess(doubleArray, ::bottomUpMergeSort, delay)
    delayExit()
}

fun main() {
    ex3(arrayOf('E', 'A', 'S', 'Y', 'Q', 'U', 'E', 'S', 'T', 'I', 'O', 'N'), 500)
}