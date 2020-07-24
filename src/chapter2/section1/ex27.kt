package chapter2.section1

import chapter2.ArrayInitialState
import chapter2.sortMethodsCompare

/**
 * 实验证明希尔排序是次平方级的
 * 对比希尔排序、插入排序、以及选择排序的性能，数组大小以2的幂次递增，从128开始
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