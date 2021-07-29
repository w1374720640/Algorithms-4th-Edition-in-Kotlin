package chapter2.section2

import extensions.inputPrompt
import extensions.random
import extensions.readInt
import kotlin.math.min

/**
 * 间接排序
 * 编写一个不改变数组的归并排序，它返回一个Int类型的数组perm
 * perm[i]的值是原数组中第i小的元素的位置
 *
 * 解：创建两个和原数组大小相等的IntArray，第一个数组从0到size-1递增，是最终的返回对象，第二个数组是额外空间
 * 对第一个数组进行归并排序（由顶向下或由底向上都可以，这里用由底向上）
 * 需要注意的是，对比的值不是第一个数组中的值，而是将第一个数组的值作为索引到原数组中取值对比
 * 对比后交换的是第一和第二个数组中的索引值而不是原数组中的值
 */
fun <T : Comparable<T>> ex20_IndexSort(originalArray: Array<T>): IntArray {
    val extraArray1 = IntArray(originalArray.size) { it }
    val extraArray2 = IntArray(originalArray.size)
    var step = 1
    while (step < originalArray.size) {
        for (start in originalArray.indices step step * 2) {
            val mid = start + step - 1
            val end = min(start + 2 * step - 1, originalArray.size - 1)
            if (mid < originalArray.size - 1) {
                ex20Merge(originalArray, extraArray1, extraArray2, start, mid, end)
            }
        }
        step *= 2
    }
    return extraArray1
}

fun <T : Comparable<T>> ex20Merge(originalArray: Array<T>, extraArray1: IntArray, extraArray2: IntArray, start: Int, mid: Int, end: Int) {
    for (i in start..end) {
        extraArray2[i] = extraArray1[i]
    }
    var i = start
    var j = mid + 1
    var k = start
    while (k <= end) {
        when {
            i > mid -> extraArray1[k++] = extraArray2[j++]
            j > end -> extraArray1[k++] = extraArray2[i++]
            originalArray[extraArray2[i]] <= originalArray[extraArray2[j]] -> extraArray1[k++] = extraArray2[i++]
            else -> extraArray1[k++] = extraArray2[j++]
        }
    }
}

fun <T : Comparable<T>> ex20CheckAscOrder(originalArray: Array<T>, extraArray: IntArray): Boolean {
    if (originalArray.size <= 1) return true
    for (i in 1 until originalArray.size) {
        if (originalArray[extraArray[i - 1]] > originalArray[extraArray[i]]) return false
    }
    return true
}

fun main() {
    inputPrompt()
    val size = readInt("size: ")
    //为了更直观的显示，用较小的随机整数填充数组
    val originalArray = Array(size) { random(size * 2) }
    println("originalArray: ${originalArray.joinToString(limit = 100)}")
    val extraArray = ex20_IndexSort(originalArray)
    println("extraArray: ${extraArray.joinToString(limit = 100)}")
    val isAscOrder = ex20CheckAscOrder(originalArray, extraArray)
    println("isAscOrder=$isAscOrder")
}