package chapter2.section5

import chapter1.section3.DoublyLinkedList
import chapter1.section3.addTail
import chapter1.section3.deleteHeader
import chapter1.section3.isEmpty
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
import kotlin.collections.HashMap
import kotlin.collections.copyOf
import kotlin.collections.forEach
import kotlin.collections.joinToString
import kotlin.collections.set
import kotlin.collections.sorted

/**
 * 检测稳定性
 * 扩展练习2.1.16中的check()方法，对指定数组调用sort()，如果排序结果是稳定的则返回true，否则返回false
 * 不要假设sort()只会使用exch()移动数据
 *
 * 解：将练习2.1.16中Map的Value类型由Int改为LinkedList
 * 排序前遍历数组，依次将对象放入键对应的列表中
 * 排序后，遍历数组，对比对象是否在键对应的列表头部，是则移除头部，否则直接返回
 * 最后检查Map是否为空
 */
fun <T : Comparable<T>> ex17_CheckStability(array: Array<T>, sort: (Array<T>) -> Unit): Boolean {
    val map = HashMap<T, DoublyLinkedList<T>>()
    array.forEach {
        val list = map[it]
        if (list == null) {
            val newList = DoublyLinkedList<T>().apply { addTail(it) }
            map[it] = newList
        } else {
            list.addTail(it)
        }
    }
    sort(array)
    array.forEach {
        val list = map[it]
        if (list == null || list.isEmpty()) {
            return false
        }
        //因为排序方法只能排序对象类型数组，所以可以直接比较是否为同一对象
        if (list.first?.item === it) {
            list.deleteHeader()
            if (list.isEmpty()) {
                map.remove(it)
            }
        } else {
            return false
        }
    }
    return map.isEmpty()
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
    val array = getDoubleArray(10, ArrayInitialState.REPEAT)
    println("${formatStringLength("array", maxNameLength)}: ${array.joinToString { formatDouble(it, 2) }}")
    println("${formatStringLength("result", maxNameLength)}: ${array.sorted().joinToString { formatDouble(it, 2) }}")
    sortMethods.forEach {
        val copyArray = array.copyOf()
        val result = ex17_CheckStability(copyArray, it.second)
        println("${formatStringLength(it.first, maxNameLength)}: $result")
    }
}

