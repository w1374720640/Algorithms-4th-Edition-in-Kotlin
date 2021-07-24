package chapter2.section2

import extensions.delayExit

/**
 * 按照本节开头所示轨迹的格式给出原地归并排序的抽象merge()方法是如何将数组
 * A E Q S U Y E I N O S T 排序的。
 */
fun ex1(array: Array<Char>, delay: Long) {
    val doubleArray = Array(array.size) { array[it].toDouble() }
    showMergeSortProcess(doubleArray, ::topDownMergeSort, delay)
    delayExit()
}

fun main() {
    ex1(arrayOf('A', 'E', 'Q', 'S', 'U', 'Y', 'E', 'I', 'N', 'O', 'S', 'T'), 500)
}