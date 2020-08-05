package chapter2.section2

import chapter2.ArrayInitialState
import chapter2.getDoubleArray
import chapter2.section1.checkAscOrder
import chapter2.sortMethodsCompare

/**
 * 快速归并
 * 实现一个merge()方法，按降序将a[]的后半部分复制到aux[]，然后将其归并回a[]中
 * 这样就可以去掉内循环中检测某半边是否用尽的代码
 *
 * 解：如果左侧的最大值比右侧的最大值小，则左侧循环结束后无需将右侧的值复制回原数组，因为剩余的值在原数组中已经有序
 * 如果左侧的最大值比右侧的最大值大，则右侧循环结束后会将左侧值依次与左侧最大值对比，永远返回左侧值
 */
fun <T : Comparable<T>> ex10(array: Array<T>) {
    val extraArray = array.copyOf()
    ex10(array, extraArray, 0, array.size - 1)
}

fun <T : Comparable<T>> ex10(array: Array<T>, extraArray: Array<T>, start: Int, end: Int) {
    if (start >= end) return
    val mid = (start + end) / 2
    ex10(array, extraArray, start, mid)
    ex10(array, extraArray, mid + 1, end)
    ex10Merge(array, extraArray, start, mid, end)
}

fun <T : Comparable<T>> ex10Merge(array: Array<T>, extraArray: Array<T>, start: Int, mid: Int, end: Int) {
    for (i in start..mid) {
        extraArray[i] = array[i]
    }
    for (i in end downTo mid + 1) {
        extraArray[end - i + mid + 1] = array[i]
    }
    var leftIndex = start
    var rightIndex = end
    var index = start
    while (index <= end) {
        if (extraArray[leftIndex] <= extraArray[rightIndex]) {
            array[index++] = extraArray[leftIndex++]
        } else {
            array[index++] = extraArray[rightIndex--]
        }
    }
}

fun main() {
    //检查排序方法是否正确
    repeat(10) {
        val array = getDoubleArray(1000)
        ex10(array)
        val result = array.checkAscOrder()
        println("result=$result")
    }

    //对比原始由顶向下归并排序和修改后的快速归并方法的效率
//    val sortMethodList = arrayOf<Pair<String, (Array<Double>)->Unit>>(
//            "Top Down Merge Sort" to ::topDownMergeSort,
//            "ex10" to ::ex10
//    )
//    sortMethodsCompare(sortMethodList, 10, 100_0000, ArrayInitialState.RANDOM)
}