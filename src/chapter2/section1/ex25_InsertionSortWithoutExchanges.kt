package chapter2.section1

import chapter2.ArrayInitialState
import chapter2.sortMethodsCompare
import extensions.inputPrompt
import extensions.readInt

/**
 * 不需要交换的插入排序
 * 在插入排序的实现中使较大元素右移一位只需要访问一次数组（而不使用exch()）。
 * 使用SortCompare来评估这种做法的效果。
 */
fun <T : Comparable<T>> ex25_InsertionSortWithoutExchanges(array: Array<T>) {
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
    sortMethodsCompare(arrayOf("Insertion Sort" to ::insertionSort, "ex25" to ::ex25_InsertionSortWithoutExchanges), 1, size, ArrayInitialState.RANDOM)
}