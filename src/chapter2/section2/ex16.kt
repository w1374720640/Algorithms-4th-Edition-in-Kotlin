package chapter2.section2

import chapter2.ArrayInitialState
import chapter2.getDoubleArray
import chapter2.section1.checkAscOrder
import chapter2.sortMethodsCompare

/**
 * 自然的归并排序
 * 编写一个自底向上的归并排序，当需要将两个子数组排序时，能够利用数组中已经有序的部分
 * 首先找到一个有序的子数组（移动指针直到当前元素比上一个元素小为止），然后找出另一个并将它们归并
 * 根据数组大小和数组中递增子数组的最大长度分析算法的运行时间
 */
fun <T : Comparable<T>> ex16(array: Array<T>) {
    if (array.size <= 1) return
    val extraArray = array.copyOf()
    var firstRangeStart = 0
    var firstRangeEnd = 0
    var secondRangStart = 0
    var secondRangeEnd = 0

    while (true) {
        firstRangeEnd = firstRangeStart
        for (i in firstRangeStart until array.size - 1) {
            if (array[i + 1] < array[i]) {
                break
            }
            firstRangeEnd = i + 1
        }
        secondRangStart = firstRangeEnd + 1
        secondRangeEnd = secondRangStart
        for (i in secondRangStart until array.size - 1) {
            if (array[i + 1] < array[i]) {
                break
            }
            secondRangeEnd = i + 1
        }
        if (firstRangeStart == 0 && secondRangStart >= array.size) {
            break
        }
        if (secondRangStart >= array.size) {
            firstRangeStart = 0
            continue
        }
        ex16Merge(array, extraArray, firstRangeStart, firstRangeEnd, secondRangeEnd)
        firstRangeStart = if (secondRangeEnd >= array.size - 1) {
            0
        } else {
            secondRangeEnd + 1
        }
    }
}

fun <T : Comparable<T>> ex16Merge(originalArray: Array<T>, extraArray: Array<T>, start: Int, mid: Int, end: Int) {
    for (i in start..end) {
        extraArray[i] = originalArray[i]
    }
    var i = start
    var j = mid + 1
    var k = start
    while (k <= end) {
        when {
            i > mid -> originalArray[k++] = extraArray[j++]
            j > end -> originalArray[k++] = extraArray[i++]
            extraArray[i] < extraArray[j] -> originalArray[k++] = extraArray[i++]
            else -> originalArray[k++] = extraArray[j++]
        }
    }
}

fun main() {
    val array = getDoubleArray(1000)
    ex16(array)
    val result = array.checkAscOrder()
    println("result = $result")

    val sortMethods: Array<Pair<String, (Array<Double>) -> Unit>> = arrayOf(
            "Top Down Merge Sort" to ::topDownMergeSort,
            "ex16" to ::ex16
    )
    sortMethodsCompare(sortMethods, 10, 100_0000, ArrayInitialState.RANDOM)
}