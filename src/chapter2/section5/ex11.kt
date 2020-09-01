package chapter2.section5

import chapter2.ArrayInitialState
import chapter2.SwapCallback
import chapter2.getDoubleArray
import chapter2.section1.bubbleSort
import chapter2.section1.insertionSort
import chapter2.section1.selectionSort
import chapter2.section1.shellSort
import chapter2.section2.MergeSortCallback
import chapter2.section2.bottomUpMergeSort
import chapter2.section2.mergeSortCallbackList
import chapter2.section2.topDownMergeSort
import chapter2.section3.quickSortNotShuffle
import chapter2.section4.heapSort
import chapter2.swapCallbackList
import extensions.formatDouble
import extensions.formatStringLength

/**
 * 描述排序结果的一种方法是创建一个保存0到a.length-1的排列p[]，使得p[i]的值为a[i]元素的最终位置
 * 用这种方法描述插入排序、选择排序、希尔排序、归并排序、快速排序和堆排序对一个含有7个相同元素的数组的排序结果
 *
 * 解：因为所有的排序都使用swap()方法排序，所以监听swap回调，调换数组p中对应位置的元素，得到的最终结果就是排序结果
 * 归并排序用的swap()方法和其他排序方法不同，要用另外的回调监控
 */
fun <T : Comparable<T>> ex11(array: Array<T>, sortMethod: (Array<T>) -> Unit): IntArray {
    val p = IntArray(array.size) { it }
    val swapCallback = object : SwapCallback {
        override fun before(tag: Any, i: Int, j: Int) {
            if (tag === array) {
                val temp = p[i]
                p[i] = p[j]
                p[j] = temp
            }
        }

        override fun after(tag: Any, i: Int, j: Int) {
        }
    }
    val mergeSortCallback = object : MergeSortCallback {
        //归并排序需要创建一个额外数组
        val q = IntArray(array.size)
        override fun mergeStart(start: Int, end: Int) {
            for (i in start..end) {
                q[i] = p[i]
            }
        }

        override fun copyToOriginal(extraIndex: Int, originalIndex: Int) {
            p[originalIndex] = q[extraIndex]
        }

    }
    swapCallbackList.add(swapCallback)
    mergeSortCallbackList.add(mergeSortCallback)
    sortMethod(array)
    swapCallbackList.remove(swapCallback)
    mergeSortCallbackList.remove(mergeSortCallback)

    //p数组表示原来在p[i]位置的值现在放到了i的位置，和题目要求相反
    val result = IntArray(p.size)
    for (i in p.indices) {
        result[p[i]] = i
    }
    return result
}

fun main() {
    val sortMethods: Array<Pair<String, (Array<Double>) -> Unit>> = arrayOf(
            "Selection Sort" to ::selectionSort, //不稳定
            "Bubble Sort" to ::bubbleSort, //稳定
            "Insertion Sort" to ::insertionSort, //稳定
            "Shell Sort" to ::shellSort, //不稳定
            "Top Down Merge Sort" to ::topDownMergeSort, //稳定
            "Bottom Up Merge Sort" to ::bottomUpMergeSort, //稳定
            "Quick Sort" to ::quickSortNotShuffle, //不稳定
            "Heap Sort" to ::heapSort //不稳定
    )
    //名称对齐
    var maxNameLength = 0
    sortMethods.forEach {
        if (it.first.length > maxNameLength) maxNameLength = it.first.length
    }
    val array = Array(7) { 1.0 }
    //放开下面的注释，测试由大量重复值组成的数组排序结果
//    val array = getDoubleArray(10, ArrayInitialState.REPEAT)
//    println("${formatStringLength("array", maxNameLength)}: ${array.joinToString { formatDouble(it, 2) }}")
//    println("${formatStringLength("result", maxNameLength)}: ${array.sorted().joinToString { formatDouble(it, 2) }}")
    sortMethods.forEach {
        val copyArray = array.copyOf()
        val result = ex11(copyArray, it.second)
        println("${formatStringLength(it.first, maxNameLength)}: ${result.joinToString()}")
    }
}