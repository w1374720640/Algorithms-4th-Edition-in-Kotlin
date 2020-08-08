package chapter2.section3

import chapter2.getDoubleArray
import chapter2.showSortingProcess
import chapter2.swap
import extensions.*

/**
 * 编写一段程序来生成quickSort表现最佳的数组（无重复元素）
 * 数组大小为N且不包含重复元素，每次切分后两个子数组的大小最多差1
 * 子数组的大小与含有N个相同元素的数组的切分情况相同
 * （对于这道练习，我们不需要在排序开始时打乱数组）
 *
 * 解：最佳情况应该切分数组的次数最少，且交换元素的次数最少
 * 对应的行为是：每次切分前第一个元素的值是中位数，且左侧半边都小于第一个值，右半边都大于第一个值
 * 先创建一个有序数组，取中间值与第一位元素交换
 * 递归左半边（不包括中间值和第一个元素）和右半边，直到范围小于等于1
 *
 * （不是真的最优解，还有优化空间，不应该将中间值和第一个元素交换，应该将中间值放到第一位，新区间的中间值放到旧的中间值位置，判断比较复杂，未完成）
 */
fun ex16(size: Int): Array<Double> {
    val array = Array(size) { it.toDouble() }
    ex16(array, 0, array.size - 1)
    return array
}

fun ex16(array: Array<Double>, start: Int, end: Int) {
    if (start >= end) return
    val mid = (start + end) / 2
    array.swap(start, mid)
    ex16(array, start + 1, mid - 1)
    ex16(array, mid + 1, end)
}

private fun drawSortingProcess() {
    inputPrompt()
    val size = readInt("size: ")
    val delay = readLong("delay: ")
    val array = ex16(size)
    showSortingProcess(array, ::quickSortWithOriginalArray, delay, true)
    delayExit()
}

private fun compareWithRandomArray() {
    val size = 100_0000
    val bestArray = ex16(size)
    val bestTime = spendTimeMillis {
        quickSortWithOriginalArray(bestArray)
    }
    println("bestTime=$bestTime")
    repeat(20) {
        val randomArray = getDoubleArray(size)
        val randomTime = spendTimeMillis { quickSortWithOriginalArray(randomArray) }
        println("randomTime=$randomTime")
    }
}

fun main() {
    drawSortingProcess()
//    compareWithRandomArray()
}