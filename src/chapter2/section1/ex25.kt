package chapter2.section1

import chapter2.ArrayInitialState
import chapter2.sortMethodsCompare
import extensions.inputPrompt
import extensions.readInt

/**
 * 不需要交换的插入排序
 * 在插入排序中，只有较大元素向右移动一位，不需要交换两个元素
 */
fun <T : Comparable<T>> ex25(array: Array<T>) {
    for (i in 1 until array.size) {
        var index = i
        val value = array[i]
        for (j in i - 1 downTo 0) {
            if (value > array[j]) break
            array[j + 1] = array[j]
            index = j
        }
        if (index != i) {
            array[index] = value
        }
    }
}

fun main() {
    inputPrompt()
    val size = readInt("size: ")
    //检查排序方法是否正确
//    val result = ex16(getDoubleArray(size, ArrayInitialState.RANDOM), ::ex25)
//    println("Check result = $result")
    sortMethodsCompare(arrayOf("Insert Sort" to ::insertSort, "ex25" to ::ex25), 1, size, ArrayInitialState.RANDOM)
}