package chapter2.section5

import chapter2.ArrayInitialState
import chapter2.getDoubleArray
import chapter2.section1.bubbleSort
import chapter2.section1.insertionSort
import chapter2.section1.selectionSort
import chapter2.section1.shellSort
import chapter2.section2.bottomUpMergeSort
import chapter2.section2.topDownMergeSort
import chapter2.section3.quickSortNotShuffle
import chapter2.section4.heapSort
import extensions.formatDouble
import extensions.formatStringLength

/**
 * 强制稳定
 * 编写一段能够将任意排序方法变得稳定的封装代码，创建一种新的数据类型作为键，
 * 将键的原始索引保存在其中，并在调用sort()之后再恢复原始的键
 */
fun <T : Comparable<T>> ex18_ForceStability(array: Array<T>, sort: (Array<IndexWrapper<T>>) -> Unit) {
    val wrapperArray = Array(array.size) { IndexWrapper(array[it], it) }
    sort(wrapperArray)
    for (i in array.indices) {
        array[i] = wrapperArray[i].item
    }
}

/**
 * 通过对原始类型和索引的封装，保证所有的数据都不相等，确保了相同的元素也能按索引顺序排列
 */
class IndexWrapper<T : Comparable<T>>(val item: T, val index: Int) : Comparable<IndexWrapper<T>> {
    override fun compareTo(other: IndexWrapper<T>): Int {
        val itemCompare = item.compareTo(other.item)
        return if (itemCompare != 0) {
            itemCompare
        } else {
            index.compareTo(other.index)
        }
    }
}

fun main() {
    val sortMethods: Array<Pair<String, (Array<IndexWrapper<Double>>) -> Unit>> = arrayOf(
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
    val array = getDoubleArray(10, ArrayInitialState.REPEAT)
    println("${formatStringLength("array", maxNameLength)}: ${array.joinToString { formatDouble(it, 2) }}")
    println("${formatStringLength("result", maxNameLength)}: ${array.sorted().joinToString { formatDouble(it, 2) }}")

    sortMethods.forEach {
        val copyArray = array.copyOf()
        //通过一个匿名函数适配不同的接口参数
        val result = ex17_CheckStability(copyArray, fun(array: Array<Double>) {
            ex18_ForceStability(array, it.second)
        })
        println("${formatStringLength(it.first, maxNameLength)}: $result")
    }
}