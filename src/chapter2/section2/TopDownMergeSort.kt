package chapter2.section2

import chapter2.ArrayInitialState
import chapter2.enumValueOf
import chapter2.getDoubleArray
import chapter2.less
import extensions.delayExit
import extensions.inputPrompt
import extensions.readInt
import extensions.readLong

/**
 * 归并排序（自顶向下）
 */
fun <T : Comparable<T>> topDownMergeSort(originalArray: Array<T>) {
    if (originalArray.size <= 1) return
    val extraArray = originalArray.copyOf()
    topDownMergeSort(originalArray, extraArray, 0, originalArray.size - 1)
}

fun <T : Comparable<T>> topDownMergeSort(originalArray: Array<T>, extraArray: Array<T>, start: Int, end: Int) {
    if (start >= end) return
    val mid = (start + end) / 2
    topDownMergeSort(originalArray, extraArray, start, mid)
    topDownMergeSort(originalArray, extraArray, mid + 1, end)
    merge(originalArray, extraArray, start, mid, end)
}

fun <T : Comparable<T>> merge(originalArray: Array<T>, extraArray: Array<T>, start: Int, mid: Int, end: Int) {
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
                originalArray[k++] = extraArray[j++]
            }
            j > end -> {
                mergeSortCallbackList.forEach { it.copyToOriginal(i, k) }
                originalArray[k++] = extraArray[i++]
            }
            //这里必须先处理a[j]<a[i]的情况，保证a[i]==a[j]时先将左侧的a[i]赋值到a[k]中，保证算法的稳定
            extraArray.less(j, i) -> {
                mergeSortCallbackList.forEach { it.copyToOriginal(j, k) }
                originalArray[k++] = extraArray[j++]
            }
            else -> {
                mergeSortCallbackList.forEach { it.copyToOriginal(i, k) }
                originalArray[k++] = extraArray[i++]
            }
        }
    }
}

fun main() {
    inputPrompt()
    val size = readInt("size: ")
    val delay = readLong("delay time millis: ")
    val ordinal = readInt("array initial state(0~5): ")
    val state = enumValueOf<ArrayInitialState>(ordinal)
    val array = getDoubleArray(size, state)
    showMergeSortProcess(array, ::topDownMergeSort, delay)
    delayExit()

    //运行时间曲线图
//    runningTimeGraph(500_0000, ::topDownMergeSort)
//    delayExit()

    //双倍增长测试
//    doubleGrowthTest(1000_0000, ::topDownMergeSort) { N -> N * log2(N.toDouble()) }

    //和希尔排序性能对比
//    val sortMethods: Array<Pair<String, (Array<Double>) -> Unit>> = arrayOf(
//            "Shell Sort" to ::shellSort,
//            "Top Down Merge Sort" to ::topDownMergeSort
//    )
//    sortMethodsCompare(sortMethods, 10, 100_0000, ArrayInitialState.RANDOM)
}