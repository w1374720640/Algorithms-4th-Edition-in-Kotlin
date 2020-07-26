package chapter2.section2

import chapter2.*

/**
 * 实现2.2.2节所述的对归并排序的三项改进：
 * 加快小数组的排序速度，检测数组是否已经有序，以及通过在递归中交换参数来避免数组的复制
 *
 * 在递归中交换参数来避免数组的复制，这个理解起来有点困难
 * 因为归并的次数总是奇数次（左侧归并+右侧归并+总归并）
 * 所以第一次归并传参时需要将数组顺序颠倒，就能得到原数组有序的结果
 */
inline fun <reified T : Comparable<T>> optimizedTopDownMergeSort(originalArray: Array<T>) {
    if (originalArray.size <= 1) return
    val extraArray = originalArray.clone()
    optimizedTopDownMergeSort(extraArray, originalArray, 0, originalArray.size - 1)
}

fun <T : Comparable<T>> optimizedTopDownMergeSort(originalArray: Array<T>, extraArray: Array<T>, start: Int, end: Int) {
    if (start >= end) return
    if (end - start < 15) {
        insertionSort(extraArray, start, end)
        return
    }
    val mid = (start + end) / 2
    optimizedTopDownMergeSort(extraArray, originalArray, start, mid)
    optimizedTopDownMergeSort(extraArray, originalArray, mid + 1, end)
    if (originalArray[mid] <= originalArray[mid + 1]) {
        for (i in start..end) {
            extraArray[i] = originalArray[i]
        }
        return
    }
    optimizedMerge(originalArray, extraArray, start, mid, end)
}

/**
 * 插入排序（包括start和end）
 */
fun <T : Comparable<T>> insertionSort(array: Array<T>, start: Int, end: Int) {
    for (i in start + 1..end) {
        for (j in i downTo start + 1) {
            if (array.less(j, j - 1)) {
                array.swap(j, j - 1)
            } else {
                break
            }
        }
    }
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
            originalArray[i] < originalArray[j] -> {
                mergeSortCallbackList.forEach { it.copyToOriginal(i, k) }
                extraArray[k++] = originalArray[i++]
            }
            else -> {
                mergeSortCallbackList.forEach { it.copyToOriginal(j, k) }
                extraArray[k++] = originalArray[j++]
            }
        }
    }
}

fun main() {
//    repeat(100) {
//        val array = getDoubleArray(it + 10)
//        mergeSortOptimized(array)
//        val result = array.checkAscOrder()
//        println("size=${array.size} result=$result")
//    }

    val sortMethodList = arrayOf<Pair<String,(Array<Double>) -> Unit>>(
            "Top Down Merge Sort" to ::topDownMergeSort,
            "Optimized Top Down Merge Sort" to ::optimizedTopDownMergeSort
    )
    sortMethodsCompare(sortMethodList, 10, 100_0000, ArrayInitialState.RANDOM)
}