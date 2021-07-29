package chapter2.section2

import chapter2.SwapCallback
import chapter2.getDoubleArray
import chapter2.section1.insertionSort
import chapter2.swapCallbackList

/**
 * 倒置
 * 编写一个线性对数级别的算法，统计给定数组中的“倒置”数量（即插入排序所需的交换次数）
 *
 * 解：使用归并排序对数组排序
 * 归并两个数组的过程中，设两个数组大小都为M，对比第一个数组中的i元素和第二个数组中的j元素
 * 如果第二个数组中的j元素比第一个数组中的i元素小，则总倒置数量需要加上 M-i
 */
fun <T : Comparable<T>> ex19_Inversions(array: Array<T>): Long {
    if (array.size <= 1) return 0L
    val extraArray = array.copyOf()
    return ex19_Inversions(array, extraArray, 0, array.size - 1)
}

fun <T : Comparable<T>> ex19_Inversions(array: Array<T>, extraArray: Array<T>, start: Int, end: Int): Long {
    if (start >= end) return 0L
    val mid = (start + end) / 2
    val leftCount = ex19_Inversions(array, extraArray, start, mid)
    val rightCount = ex19_Inversions(array, extraArray, mid + 1, end)
    val mergeCount = ex19Merge(array, extraArray, start, mid, end)
    return leftCount + rightCount + mergeCount
}

fun <T : Comparable<T>> ex19Merge(array: Array<T>, extraArray: Array<T>, start: Int, mid: Int, end: Int): Long {
    for (i in start..end) {
        extraArray[i] = array[i]
    }
    var i = start
    var j = mid + 1
    var k = start
    var count = 0L
    while (k <= end) {
        when {
            i > mid -> array[k++] = extraArray[j++]
            j > end -> array[k++] = extraArray[i++]
            extraArray[i] <= extraArray[j] -> array[k++] = extraArray[i++]
            else -> {
                array[k++] = extraArray[j++]
                count += mid - i + 1
            }
        }
    }
    return count
}

fun main() {
    val array1 = getDoubleArray(10000)
    val array2 = array1.copyOf()

    val inversionsCount = ex19_Inversions(array1)
    println("inversionsCount=$inversionsCount")

    //用练习19求出的倒置数应该和插入排序的交换数相同
    var insertionSortSwapCount = 0L
    val swapCallback = object : SwapCallback {
        override fun before(tag: Any, i: Int, j: Int) {
            if (tag === array2) {
                insertionSortSwapCount++
            }
        }

        override fun after(tag: Any, i: Int, j: Int) {
        }

    }
    swapCallbackList.add(swapCallback)
    insertionSort(array2)
    swapCallbackList.remove(swapCallback)
    println("insertionSortSwapCount=$insertionSortSwapCount")
}