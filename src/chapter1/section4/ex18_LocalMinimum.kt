package chapter1.section4

import extensions.inputPrompt
import extensions.readAllInts

/**
 * 数组的局部最小元素
 * 编写一个程序，给定一个含有N个不同整数的数组，找到一个局部最小元素：
 * 满足a[i]<a[i-1]，且a[i]<a[i+1]的索引i。
 * 程序在最坏情况下所需的比较次数为~2lgN。
 * 答：检查数组的中间值a[N/2]以及和它相邻的元素a[N/2-1]和a[N/2+1]。
 * 如果a[N/2]是一个局部最小值则算法终止；否则在较小的相邻元素的半边中继续查找。
 *
 * 解：最左侧和最右侧的值只需要和一侧的值比较，比隔壁值小则为局部最小值
 * 所以N个不同整数的数组肯定有局部最小值
 */
fun ex18_LocalMinimum(array: IntArray): Int {
    require(array.isNotEmpty())
    if (array.size == 1) return 0
    if (array.size == 2) return if (array[0] < array[1]) 0 else 1
    var start = 0
    var end = array.size - 1
    while (true) {
        val mid = (start + end) / 2
        when {
            mid == 0 || mid == array.size - 1 -> return mid
            array[mid] < array[mid - 1] && array[mid] < array[mid + 1] -> return mid
            array[mid - 1] < array[mid + 1] -> end = mid - 1
            else -> start = mid + 1
        }
    }
}

fun main() {
    inputPrompt()
    val array = readAllInts()
    println("Local minimum index = ${ex18_LocalMinimum(array)}")
}