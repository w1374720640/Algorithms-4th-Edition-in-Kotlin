package chapter2.section4

import chapter2.less
import chapter2.section1.cornerCases
import chapter2.swap

/**
 * 用大顶堆实现的升序排序
 * 因为数组的索引从0开始，计算子结点和父节点的方式和基于堆的优先队列不同
 * 只需要下沉操作，无需上浮操作
 */
object HeapSort {
    fun <T : Comparable<T>> sort(array: Array<T>) {
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

    /**
     * 指定数组大小的下沉操作
     */
    fun <T : Comparable<T>> Array<T>.sink(k: Int, end: Int) {
        var i = k
        while (2 * i + 1 <= end) {
            var j = 2 * i + 1
            if (j + 1 <= end && less(j, j + 1)) {
                j++
            }
            if (less(i, j)) {
                swap(i, j)
                i = j
            } else {
                break
            }
        }
    }
}

fun main() {
    cornerCases(HeapSort::sort)
}