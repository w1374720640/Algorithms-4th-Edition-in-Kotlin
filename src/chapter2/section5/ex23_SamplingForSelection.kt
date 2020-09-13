package chapter2.section5

import chapter2.getCompareTimes
import chapter2.getDoubleArray
import chapter2.less
import chapter2.swap
import extensions.random
import extensions.shuffle

/**
 * 选择的取样
 * 实验使用取样来改进select()函数的想法。提示：使用中位数可能并不总是有效
 *
 * 解：可以使用三取样快速排序的思想优化，也可以用更复杂的练习2.3.24来实现
 * 这里为简单起见使用了三取样快速排序的思想
 * 和标准三取样快速排序不同的是，先比较k和array.size的大小，
 * 若k<size/3，则每次取三个数中最小的，若size/3<k<2*size/3则每次取中值，若k>2*size/3则每次取最大值
 */
fun <T : Comparable<T>> ex23_SamplingForSelection(array: Array<T>, k: Int): T {
    array.shuffle()
    return samplingForSelection(array, k, 0, array.size - 1)
}

fun <T : Comparable<T>> samplingForSelection(array: Array<T>, k: Int, start: Int, end: Int): T {
    //k必须在有效范围内
    require(k in start..end)
    if (start == end) return array[k]

    //范围较小时直接排序，不取样
    val size = end - start + 1
    if (size > 10) {
        val indexArray = intArrayOf(start, (start + end) / 2, end)
        sortByIndex(array, indexArray)
        val select = when {
            k < size / 3 -> indexArray[0]
            k > 2 * size / 3 -> indexArray[2]
            else -> indexArray[1]
        }
        array.swap(start, select)
    }

    val partition = chapter2.section3.partition(array, start, end)
    return when {
        partition == k -> array[partition]
        partition < k -> samplingForSelection(array, k, partition + 1, end)
        else -> samplingForSelection(array, k, start, partition - 1)
    }
}

private fun <T : Comparable<T>> sortByIndex(array: Array<T>, indexArray: IntArray) {
    for (i in 1 until indexArray.size) {
        for (j in i downTo 1) {
            if (array.less(indexArray[j], indexArray[j - 1])) {
                indexArray.swap(j, j - 1)
            } else {
                break
            }
        }
    }
}

fun main() {
    val size = 10_0000
    val times = 100

    val array = getDoubleArray(size)
    var compareTimes1 = 0L
    var compareTimes2 = 0L
    repeat(times) {
        //当k很小或很大时，能节省更多的比较次数
        val k = random(size)
        var value1 = 0.0
        var value2 = 1.0
        compareTimes1 += getCompareTimes(array.copyOf()) {
            value1 = select(it, k)
        }
        compareTimes2 += getCompareTimes(array.copyOf()) {
            value2 = ex23_SamplingForSelection(it, k)
        }
        check(value1 == value2)
    }
    println("select average compare: ${compareTimes1 / times} times")
    println("  ex23 average compare: ${compareTimes2 / times} times")
}