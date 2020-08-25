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
fun <T : Comparable<T>> ex16_NaturalMergesort(array: Array<T>) {
    if (array.size <= 1) return
    val extraArray = array.copyOf()
    var start = 0
    var mid = 0
    var end = 0

    fun findSortedSubArray(array: Array<T>, start: Int): Int {
        for (i in start until array.size - 1) {
            if (array[i + 1] < array[i]) {
                return i
            }
        }
        return array.size - 1
    }

    while (true) {
        mid = findSortedSubArray(array, start)
        if (mid == array.size - 1) {
            if (start == 0) {
                break
            } else {
                start = 0
                continue
            }
        }
        end = findSortedSubArray(array, mid + 1)
        merge(array, extraArray, start, mid, end)
        start = if (end == array.size - 1) {
            0
        } else {
            end + 1
        }
    }
}

fun main() {
    val array = getDoubleArray(1000)
    ex16_NaturalMergesort(array)
    val result = array.checkAscOrder()
    println("result = $result")

    val sortMethods: Array<Pair<String, (Array<Double>) -> Unit>> = arrayOf(
            "Top Down Merge Sort" to ::topDownMergeSort,
            "ex16" to ::ex16_NaturalMergesort
    )
    sortMethodsCompare(sortMethods, 10, 100_0000, ArrayInitialState.RANDOM)
}