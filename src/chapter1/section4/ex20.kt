package chapter1.section4

import extensions.inputPrompt
import extensions.readInt

/**
 * 如果一个数组先递增后递减，则称这个数组为双调的
 * 给定一个含有N个不同Int值的双调数组，判断它是否含有给定的整数
 * 要求程序在最坏情况下所需的比较次数为~3lgN
 *
 * 解：先用二分法找出双调点，比较次数为lgN
 * 在最坏情况下，给定整数小于转折点，可能存在与左右两侧的任何一侧
 * 在左右两侧分别用二分查找搜索，设左侧占据的百分比为x，则比较次数分别为lg(N*x)、lg(N*(1-x))
 * lg(N*x)+lg(N*(1-x))=lgN+lgx+lgN+lg(1-x) ~=2lgN
 * 所以整体最坏情况下需要比较次数为~3lgN
 */
fun ex20(array: IntArray, value: Int): Boolean {
    /**
     * 自定义对比条件的二分查找
     * condition返回0表示完全匹配，大于0表示需要在右侧查找，小于0表示需要在左侧查找
     */
    fun search(array: IntArray, startIndex: Int = 0, endIndex: Int = array.size - 1, condition: (Int) -> Int): Int {
        var start = startIndex
        var end = endIndex
        while (start <= end) {
            val mid = (start + end) / 2
            val result = condition(mid)
            when {
                result == 0 -> return mid
                result < 0 -> end = mid - 1
                result > 0 -> start = mid + 1
            }
        }
        return -1
    }
    //查找转折点
    val turningPoint = search(array) { index ->
        val result: Int
        when {
            index == 0 || index == array.size - 1 -> {
                throw IllegalArgumentException("The array is not incremented and then decremented")
            }
            array[index - 1] < array[index] && array[index + 1] < array[index] -> {
                //左右两侧都比中间点低，说明这个位置就是双调点
                result = 0
            }
            array[index - 1] < array[index] && array[index + 1] > array[index] -> {
                //左低右高，说明在双调点左侧，需要在右侧查找双调点
                result = 1
            }
            array[index - 1] > array[index] && array[index + 1] < array[index] -> {
                //左高右低，说明在双调点右侧，需要在左侧查找双调点
                result = -1
            }
            else -> {
                throw IllegalArgumentException("The array is not incremented and then decremented")
            }
        }
        result
    }
    return when {
        value > array[turningPoint] -> false
        value == array[turningPoint] -> true
        else -> {
            val leftIndex = search(array, endIndex = turningPoint - 1) { index ->
                value - array[index]
            }
            val rightIndex = search(array, startIndex = turningPoint + 1) { index ->
                array[index] - value
            }
            leftIndex != -1 || rightIndex != -1
        }
    }
}

fun main() {
    inputPrompt()
    val array = intArrayOf(1, 3, 5, 7, 11, 8, 6, 4, 3, 2, 0)
    val hasFound = ex20(array, readInt())
    println("hasFound = $hasFound")
}