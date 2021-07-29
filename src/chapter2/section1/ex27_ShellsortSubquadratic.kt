package chapter2.section1

import chapter2.ArrayInitialState
import chapter2.sortMethodsCompare

/**
 * 希尔排序的用时是次平方级的
 * 在你的计算机上用SortCompare比较希尔排序和插入排序以及选择排序。
 * 测试数组的大小按照2的幂次递增，从128开始。
 */
fun main() {
    val sortMethods: Array<Pair<String, (Array<Double>) -> Unit>> = arrayOf(
            "Selection Sort" to ::selectionSort,
            "Insertion Sort" to ::insertionSort,
            "Shell Sort" to ::shellSort
    )
    var size = 128
    repeat(9) {
        println("size=$size")
        sortMethodsCompare(sortMethods, 10, size, ArrayInitialState.RANDOM)
        size *= 2
    }
}