package chapter2.section4

import chapter2.getCompareTimes
import chapter2.getDoubleArray
import chapter2.less
import chapter2.section1.cornerCases
import chapter2.section2.getMergeSortComparisonTimes
import chapter2.swap

/**
 * 基于完全多叉树的堆排序（区别于完全二叉树）
 * 索引从0开始，设多叉树的父节点为k，分叉为t，则子节点为 t*k+1 t*k+2 ... t*k+t
 * 设多叉树的子节点为m，则父节点为(m-1)/t
 * 设数组大小为N，则最后一个有子节点的父节点为(N-2)/t
 */
fun <T : Comparable<T>> multiwayHeapSort(array: Array<T>, way: Int) {
    require(way > 1)
    //构造大顶堆
    for (i in (array.size - 2) / way downTo 0) {
        array.sink(i, array.size - 1, way)
    }
    //将堆顶数据交换到数组末尾，并对堆顶执行下沉操作
    for (i in array.size - 1 downTo 1) {
        array.swap(0, i)
        array.sink(0, i - 1, way)
    }
}

/**
 * 指定数组大小的下沉操作
 */
private fun <T : Comparable<T>> Array<T>.sink(k: Int, end: Int, way: Int) {
    var i = k
    while (i * way + 1 <= end) {
        val j = maxIndex(i * way + 1, minOf(end, i * way + way))
        if (less(i, j)) {
            swap(i, j)
            i = j
        } else {
            break
        }
    }
}

private fun <T : Comparable<T>> Array<T>.maxIndex(start: Int, end: Int): Int {
    var maxIndex = start
    for (i in start + 1..end) {
        if (less(maxIndex, i)) {
            maxIndex = i
        }
    }
    return maxIndex
}


/**
 * 在堆排序中使用t向堆的情况下找出使比较次数NlgN的系数最小的t值
 */
fun main() {
    //使用7路
    cornerCases { multiwayHeapSort(it, 7) }
    val size = 10000
    repeat(10) {
        val way = it + 2
        val times = getCompareTimes(getDoubleArray(size)) { array ->
            multiwayHeapSort(array, way)
        }
        println("way=$way times=$times")
    }
}