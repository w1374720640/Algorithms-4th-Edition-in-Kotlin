package chapter2.section3

import chapter2.ArrayInitialState
import chapter2.section1.cornerCases
import chapter2.sortMethodsCompare
import chapter2.swap
import edu.princeton.cs.algs4.StdRandom
import kotlin.math.pow

/**
 * 取样排序
 * 实现一个快速排序，取样大小为2^k-1
 * 首先将取样得到的元素排序，然后在递归函数中使用样品的中位数切分
 * 分为两部分的其余样品元素无需再次排序并可以分别应用于原数组的两个子数组
 *
 * 解：先打乱数组，取前2^k-1个数据排序，作为取样数组
 * 将取样数组分为三部分，小于中位数的值，中位数，大于中位数的值
 * 将大于中位数的值放到数组的末尾
 * 完整数组被分为四部分，小于中位数的取样数组，中位数，未排序数组，大于中位数的取样数组
 * 使用中位数将未排序的数组排序
 * 排序后数组被分为五部分，小于中位数的取样数组，小于中位数的未排序数组，中位数，大于中位数的未排序数组，大于中位数的取样数组
 * 再以中位数为分割点，左侧将取样数组的后半部分复制到左侧结尾，右侧将取样数组前半部分复制到右侧起始位置，递归调用
 */
fun <T : Comparable<T>> ex24(array: Array<T>, k: Int) {
    StdRandom.shuffle(array)
    val sampleSize = 2.0.pow(k).toInt() - 1
    if (sampleSize < 3 || sampleSize >= array.size) {
        quickSortWithOriginalArray(array)
        return
    }
    //对取样数组排序
    quickSort(array, 0, sampleSize - 1)
    //将取样数组的后半部分调换到数组末尾
    repeat(sampleSize / 2) {
        array.swap(sampleSize - 1 - it, array.size - 1 - it)
    }
    ex24(array, 0, sampleSize / 2, array.size - 1)
}

fun <T : Comparable<T>> ex24(array: Array<T>, start: Int, halfSampleSize: Int, end: Int) {
    //数组已经有序
    if (end - start + 1 <= halfSampleSize * 2 + 1) return
    val midIndex = partition(array, start + halfSampleSize, end - halfSampleSize)
    val newHalfSampleSize = halfSampleSize / 2
    when {
        //取样数组耗尽，直接使用普通快速排序
        newHalfSampleSize < 1 -> {
            quickSort(array, start, midIndex - 1)
            quickSort(array, midIndex + 1, end)
        }
        //左半边有序，只排序右半边
        midIndex == start + halfSampleSize -> {
            //右半边需要复制newHalfSampleSize+1个数到左边
            repeat(newHalfSampleSize + 1) {
                array.swap(midIndex + 1 + it, end - halfSampleSize + 1 + it)
            }
            ex24(array, midIndex + 1, newHalfSampleSize, end)
        }
        //右半边有序，只排序左半边
        midIndex == end - halfSampleSize -> {
            repeat(newHalfSampleSize) {
                array.swap(start + halfSampleSize - 1 - it, midIndex - 1 - it)
            }
            ex24(array, start, newHalfSampleSize, midIndex - 1)
        }
        //正常逻辑
        else -> {
            repeat(newHalfSampleSize) {
                array.swap(start + halfSampleSize - 1 - it, midIndex - 1 - it)
                array.swap(midIndex + 1 + it, end - halfSampleSize + 1 + it)
            }
            //右半边需要复制newHalfSampleSize+1个数到左边
            array.swap(midIndex + 1 + newHalfSampleSize, end - halfSampleSize + 1 + newHalfSampleSize)
            ex24(array, start, newHalfSampleSize, midIndex - 1)
            ex24(array, midIndex + 1, newHalfSampleSize, end)
        }
    }
}

fun main() {
    cornerCases { ex24(it, 3) }

    val sortMethods: Array<Pair<String, (Array<Double>) -> Unit>> = arrayOf(
            "Quick Sort" to ::quickSort,
            "ex24" to { array: Array<Double> -> ex24(array, 10) }
    )
    sortMethodsCompare(sortMethods, 10, 100_0000, ArrayInitialState.RANDOM)
}