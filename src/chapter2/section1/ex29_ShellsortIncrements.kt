package chapter2.section1

import chapter2.*
import extensions.inputPrompt
import extensions.readInt

/**
 * 希尔排序的递增序列
 * 通过实验比较算法2.3中所使用的递增序列和递增序列
 * 1, 5, 19, 41, 109, 209, 505, 929, 2161, 3905, 8929, 16001, 36289, 64769, 146305, 260609
 * （这是通过序列9*4^k-9*2^k+1和4^k-3*2^k+1综合得到的）。
 * 可以参考练习2.1.11.
 */
fun <T : Comparable<T>> ex29_ShellsortIncrements(array: Array<T>) {
    val increasingSequence = arrayOf(1, 5, 19, 41, 109, 209, 505, 929, 2161, 3905, 8929, 16001, 36289, 64769, 146305, 260609)
    var index = 1
    while (index < increasingSequence.size - 1 && increasingSequence[index + 1] < array.size) {
        index++
    }
    while (index >= 0) {
        val h = increasingSequence[index]
        for (i in h until array.size) {
            for (j in i downTo h step h) {
                if (!array.less(j, j - h)) break
                array.swap(j, j - h)
            }
        }
        index--
    }
}

fun main() {
    inputPrompt()
    val size = readInt("size: ")
    val ordinal = readInt("array initial state(0~5): ")
    val state = enumValueOf<ArrayInitialState>(ordinal)
    //检查排序方法是否正确
//    val result = ex16(getDoubleArray(size, ArrayInitialState.RANDOM), ::ex29)
//    println("Check result = $result")
    sortMethodsCompare(arrayOf("Shell Sort" to ::shellSort, "ex29" to ::ex29_ShellsortIncrements), 10, size, state)
}