package chapter2.section1

import chapter2.ArrayInitialState
import chapter2.getDoubleArray
import chapter2.less
import chapter2.swap
import extensions.formatDouble

/**
 * 在希尔排序中，打印出递增序列的每个元素所带来的比较次数和数组大小的比值
 * 对随机Double数组进行希尔排序，验证该值是一个小常数，数组大小按照10的幂次递增，不小于100
 */
fun ex12(array: Array<Double>) {
    fun <T : Comparable<T>> shellSort(array: Array<T>): List<Pair<Int, Double>> {
        val list = mutableListOf<Pair<Int, Double>>()
        var h = 1
        while (h < array.size / 3) h = 3 * h + 1
        while (h >= 1) {
            var count = 0.0
            for (i in h until array.size) {
                for (j in i downTo h step h) {
                    if (!array.less(j, j - h)) break
                    array.swap(j, j - h)
                    count++
                }
            }
            list.add(h to count / array.size)
            h /= 3
        }
        return list
    }

    val list = shellSort(array)
    println(list.joinToString { pair ->
        "h=${pair.first},ratio=${formatDouble(pair.second, 2)} "
    })
    println("maxRatio=${formatDouble(list.maxBy { it.second }?.second ?: 0.0, 2)}")
}

fun main() {
    var size = 10
    repeat(5) {
        size *= 10
        val array = getDoubleArray(size)
        ex12(array)
    }
}