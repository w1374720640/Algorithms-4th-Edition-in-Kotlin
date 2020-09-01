package chapter2.section2

import chapter2.ArrayInitialState
import chapter2.getDoubleArray
import chapter2.less
import chapter2.section1.checkAscOrder
import chapter2.section1.insertionSort
import chapter2.sortMethodsCompare

/**
 * 实现2.2.2节所述的对归并排序的三项改进：
 * 加快小数组的排序速度，检测数组是否已经有序，以及通过在递归中交换参数来避免数组的复制
 *
 * 在递归中交换参数来避免数组的复制，这个理解起来有点困难
 * 因为归并的次数总是奇数次（左侧归并+右侧归并+总归并）
 * 所以第一次归并传参时需要将数组顺序颠倒，就能得到原数组有序的结果
 */
fun <T : Comparable<T>> optimizedTopDownMergeSort(originalArray: Array<T>) {
    if (originalArray.size <= 1) return
    val extraArray = originalArray.copyOf()
    optimizedTopDownMergeSort(extraArray, originalArray, 0, originalArray.size - 1, 15)
}

/**
 * 可以设置需要改为插入排序的临界值，如果设置函数的默认参数，会导致函数的类型推断异常
 */
fun <T : Comparable<T>> optimizedTopDownMergeSort(originalArray: Array<T>, insertionSize: Int) {
    if (originalArray.size <= 1) return
    val extraArray = originalArray.copyOf()
    optimizedTopDownMergeSort(extraArray, originalArray, 0, originalArray.size - 1, insertionSize)
}

fun <T : Comparable<T>> optimizedTopDownMergeSort(originalArray: Array<T>, extraArray: Array<T>, start: Int, end: Int, insertionSize: Int) {
    if (start >= end) return
    if (end - start <= insertionSize) {
        insertionSort(extraArray, start, end)
        return
    }
    val mid = (start + end) / 2
    optimizedTopDownMergeSort(extraArray, originalArray, start, mid, insertionSize)
    optimizedTopDownMergeSort(extraArray, originalArray, mid + 1, end, insertionSize)
    if (originalArray[mid] <= originalArray[mid + 1]) {
        for (i in start..end) {
            extraArray[i] = originalArray[i]
        }
        return
    }
    optimizedMerge(originalArray, extraArray, start, mid, end)
}

fun <T : Comparable<T>> optimizedMerge(originalArray: Array<T>, extraArray: Array<T>, start: Int, mid: Int, end: Int) {
    var i = start
    var j = mid + 1
    var k = start
    while (k <= end) {
        when {
            i > mid -> {
                mergeSortCallbackList.forEach { it.copyToOriginal(j, k) }
                extraArray[k++] = originalArray[j++]
            }
            j > end -> {
                mergeSortCallbackList.forEach { it.copyToOriginal(i, k) }
                extraArray[k++] = originalArray[i++]
            }
            originalArray.less(j, i) -> {
                mergeSortCallbackList.forEach { it.copyToOriginal(j, k) }
                extraArray[k++] = originalArray[j++]
            }
            else -> {
                mergeSortCallbackList.forEach { it.copyToOriginal(i, k) }
                extraArray[k++] = originalArray[i++]
            }
        }
    }
}

fun main() {
//    repeat(100) {
//        val array = getDoubleArray(it + 10)
//        optimizedTopDownMergeSort(array)
//        val result = array.checkAscOrder()
//        println("size=${array.size} result=$result")
//    }

    val sortMethodList = arrayOf<Pair<String, (Array<Double>) -> Unit>>(
            "Top Down Merge Sort" to ::topDownMergeSort,
            "Optimized Top Down Merge Sort" to ::optimizedTopDownMergeSort
    )
    sortMethodsCompare(sortMethodList, 10, 100_0000, ArrayInitialState.RANDOM)
}