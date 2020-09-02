package chapter2.section5

import chapter1.section3.DoublyLinkedList
import chapter1.section3.addTail
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
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

/**
 * 强制稳定
 * 编写一段能够将任意排序方法变得稳定的封装代码，创建一种新的数据类型作为键，
 * 将键的原始索引保存在其中，并在调用sort()之后再恢复原始的键
 */
fun <T : Comparable<T>> ex18_ForceStability(array: Array<T>, sort: (Array<T>) -> Unit) {
    val map = HashMap<T, DoublyLinkedList<T>>()
    for (i in array.indices) {
        val key = array[i]
        val list = map[key]
        if (list == null) {
            map[key] = DoublyLinkedList<T>().apply { addTail(key) }
        } else {
            list.addTail(key)
        }
    }
    sort(array)

    fun DoublyLinkedList<T>.indexOf(item: T): Int {
        var node = first
        var i = 0
        while (node != null) {
            if (node.item === item) return i
            i++
            node = node.next
        }
        return -1
    }

    //最坏情况下这里的时间复杂度为N^2
    //如果用新的数据类型包裹原类型，则无法使用sort函数排序，所以采用这种低效的方法
    val comparator = Comparator<T> { o1, o2 ->
        require(o1 == o2)
        val list1 = map[o1]
        require(list1 != null)
        val index1 = list1.indexOf(o1)
        val index2 = list1.indexOf(o2)
        require(index1 != -1 && index2 != -1)
        index1.compareTo(index2)
    }
    var i = 0
    for (j in array.indices) {
        if (i == j || array[i] == array[j]) continue
        if (j - i == 1) {
            i = j
            continue
        }
        //array[i] != array[j] && j - i > 1
        Arrays.sort(array, i, j, comparator)
        i = j
    }
    if (i < array.size - 1) {
        Arrays.sort(array, i, array.size, comparator)
    }
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
        //通过一个匿名函数适配接口参数
        val result = ex17_CheckStability(copyArray, fun(array: Array<Double>) {
            ex18_ForceStability(array, it.second)
        })
        println("${formatStringLength(it.first, maxNameLength)}: $result")
    }
}