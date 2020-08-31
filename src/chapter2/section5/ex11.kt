package chapter2.section5

import chapter2.SwapCallback
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
        override fun mergeStart(start: Int, end: Int) {
        }

        override fun copyToOriginal(extraIndex: Int, originalIndex: Int) {
            val temp = p[extraIndex]
            p[extraIndex] = p[originalIndex]
            p[originalIndex] = temp
        }

    }
    swapCallbackList.add(swapCallback)
    mergeSortCallbackList.add(mergeSortCallback)
    sortMethod(array)
    swapCallbackList.remove(swapCallback)
    mergeSortCallbackList.remove(mergeSortCallback)
    return p
}

fun main() {
    val array = Array(7) { 1.0 }
    println("insertionSort:       ${ex11(array, ::insertionSort).joinToString()}")
    println("selectionSort:       ${ex11(array, ::selectionSort).joinToString()}")
    println("shellSort:           ${ex11(array, ::shellSort).joinToString()}")
    println("topDownMergeSort:    ${ex11(array, ::topDownMergeSort).joinToString()}")
    println("bottomUpMergeSort:   ${ex11(array, ::bottomUpMergeSort).joinToString()}")
    println("quickSortNotShuffle: ${ex11(array, ::quickSortNotShuffle).joinToString()}")
    println("heapSort:            ${ex11(array, ::heapSort).joinToString()}")
}