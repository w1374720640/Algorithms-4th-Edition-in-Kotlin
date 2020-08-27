package chapter2.section4

import chapter2.getDoubleArray
import chapter2.less
import chapter2.swap
import extensions.spendTimeMillis

/**
 * 构造函数的代价
 * 对于N=10³、10⁶和10⁹，根据经验判断堆排序时构造堆占总耗时的比例
 */
fun <T : Comparable<T>> ex39_CostOfConstruction(array: Array<T>): Double {
    val constructTime = spendTimeMillis {
        construct(array)
    }
    val sortTime = spendTimeMillis {
        sort(array)
    }
    return constructTime.toDouble() / (constructTime + sortTime).toDouble()
}

//构造大顶堆
private fun <T : Comparable<T>> construct(array: Array<T>) {
    for (i in array.size / 2 - 1 downTo 0) {
        array.sink(i, array.size - 1)
    }
}

//将堆顶数据交换到数组末尾，并对堆顶执行下沉操作
private fun <T : Comparable<T>> sort(array: Array<T>) {
    for (i in array.size - 1 downTo 1) {
        array.swap(0, i)
        array.sink(0, i - 1)
    }
}

/**
 * 指定数组大小的下沉操作
 */
private fun <T : Comparable<T>> Array<T>.sink(k: Int, end: Int) {
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

fun main() {
    var size = 1000
    //10⁹消耗内存太大，OutOfMemoryError
    repeat(4) {
        val percent = ex39_CostOfConstruction(getDoubleArray(size))
        println("size=$size percent=$percent")
        size *= 10
    }
}