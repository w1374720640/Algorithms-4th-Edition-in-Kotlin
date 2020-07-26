package chapter2.section2

import chapter2.ArrayInitialState
import chapter2.getDoubleArray
import chapter2.getEnumByOrdinal
import chapter2.section1.doubleGrowthTest
import chapter2.section1.runningTimeGraph
import chapter2.section1.shellSort
import chapter2.sortMethodsCompare
import extensions.delayExit
import extensions.inputPrompt
import extensions.readInt
import extensions.readLong
import kotlin.math.log2

/**
 * 归并排序（自顶向下）
 */
inline fun <reified T : Comparable<T>> mergeSort(originalArray: Array<T>) {
    if (originalArray.size <= 1) return
    val extraArray = arrayOfNulls<T>(originalArray.size)
    mergeSort(originalArray, extraArray, 0, originalArray.size - 1)
}

fun <T : Comparable<T>> mergeSort(originalArray: Array<T>, extraArray: Array<T?>, start: Int, end: Int) {
    if (start >= end) return
    val mid = (start + end) / 2
    mergeSort(originalArray, extraArray, start, mid)
    mergeSort(originalArray, extraArray, mid + 1, end)
    merge(originalArray, extraArray, start, mid, end)
}

fun <T : Comparable<T>> merge(originalArray: Array<T>, extraArray: Array<T?>, start: Int, mid: Int, end: Int) {
    mergeSortCallbackList.forEach {
        it.mergeStart(start, end)
    }
    for (i in start..end) {
        extraArray[i] = originalArray[i]
    }
    var i = start
    var j = mid + 1
    var k = start
    while (k <= end) {
        when {
            i > mid -> {
                mergeSortCallbackList.forEach { it.copyToOriginal(j, k) }
                originalArray[k++] = extraArray[j++]!!
            }
            j > end -> {
                mergeSortCallbackList.forEach { it.copyToOriginal(i, k) }
                originalArray[k++] = extraArray[i++]!!
            }
            extraArray[i]!! < extraArray[j]!! -> {
                mergeSortCallbackList.forEach { it.copyToOriginal(i, k) }
                originalArray[k++] = extraArray[i++]!!
            }
            else -> {
                mergeSortCallbackList.forEach { it.copyToOriginal(j, k) }
                originalArray[k++] = extraArray[j++]!!
            }
        }
    }
}

fun main() {
    inputPrompt()
    val size = readInt("size: ")
    val delay = readLong("delay time millis: ")
    val ordinal = readInt("array initial state(0~4): ")
    val state = ArrayInitialState::class.getEnumByOrdinal(ordinal)
    val array = getDoubleArray(size, state)
    showMergeSortProcess(array, ::mergeSort, delay)
    delayExit()

    //运行时间曲线图
//    runningTimeGraph(500_0000, ::shellSort)
//    delayExit()

    //双倍增长测试
//    doubleGrowthTest(1000_0000, ::mergeSort) { N -> N * log2(N.toDouble()) }

    //和希尔排序性能对比
//    val sortMethods: Array<Pair<String, (Array<Double>) -> Unit>> = arrayOf(
//            "ShellSort" to ::shellSort,
//            "MergeSort" to ::mergeSort
//    )
//    sortMethodsCompare(sortMethods, 10, 100_0000, ArrayInitialState.RANDOM)
}