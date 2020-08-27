package chapter2.section4

import chapter2.*
import chapter2.section1.cornerCases
import extensions.formatInt
import extensions.formatLong

/**
 * 根据正文中Floyd的先沉后浮思想实现堆排序
 * 对于N=10³、10⁶和10⁹大小的随机不重复数组，记录你的程序所使用的比较次数和标准实现所使用的比较次数
 *
 * 解：在下沉过程中，不将父结点和子结点比较，直接比较两个子结点，将较大的子结点与父结点交换，直到达到堆底
 * 在将堆底的原父结点上浮到正确位置，最高只能上浮到原来的位置
 * （原文中说这种方法需要额外空间，但是并没有用到额外空间）
 */
fun <T : Comparable<T>> ex40_FloydsMethod(array: Array<T>) {
    //构造大顶堆
    for (i in array.size / 2 - 1 downTo 0) {
        array.sink(i, array.size - 1)
    }
    //将堆顶数据交换到数组末尾，并对堆顶执行下沉操作
    for (i in array.size - 1 downTo 1) {
        array.swap(0, i)
        array.sink(0, i - 1)
    }
}

private fun <T : Comparable<T>> Array<T>.sink(k: Int, end: Int) {
    var i = k
    while (2 * i + 1 <= end) {
        var j = 2 * i + 1
        if (j + 1 <= end && less(j, j + 1)) {
            j++
        }
        swap(i, j)
        i = j
    }
    swim(i, k)
}

private fun <T : Comparable<T>> Array<T>.swim(k: Int, start: Int) {
    var i = k
    while (i > start && less((i - 1) / 2, i)) {
        swap((i - 1) / 2, i)
        i = (i - 1) / 2
    }
}

fun main() {
    cornerCases(::ex40_FloydsMethod)
    var size = 1000
    repeat(4) {
        val stdCompareTimes = getCompareTimes(getDoubleArray(size), ::heapSort)
        val floydCompareTimes = getCompareTimes(getDoubleArray(size), ::ex40_FloydsMethod)
        println("compare size: ${formatInt(size, 8)} std: ${formatLong(stdCompareTimes, 8)} floyds: ${formatLong(floydCompareTimes, 8)}")
        size *= 10
    }
    size = 1000
    repeat(4) {
        val stdSwapTimes = getSwapTimes(getDoubleArray(size), ::heapSort)
        val floydSwapTimes = getSwapTimes(getDoubleArray(size), ::ex40_FloydsMethod)
        println("swap    size: ${formatInt(size, 8)} std: ${formatLong(stdSwapTimes, 8)} floyds: ${formatLong(floydSwapTimes, 8)}")
        size *= 10
    }
}