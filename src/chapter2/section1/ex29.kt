package chapter2.section1

import chapter2.*
import extensions.inputPrompt
import extensions.readInt

/**
 * 使用不同的递增序列和原来的希尔函数对比
 */
fun <T : Comparable<T>> ex29(array: Array<T>) {
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
    val ordinal = readInt("array initial state(0~4): ")
    val state = ArrayInitialState::class.getEnumByOrdinal(ordinal)
    //检查排序方法是否正确
//    val result = ex16(getDoubleArray(size, ArrayInitialState.RANDOM), ::ex29)
//    println("Check result = $result")
    sortMethodsCompare(arrayOf("Shell Sort" to ::shellSort, "ex29" to ::ex29), 10, size, state)
}