package chapter1.section4

import extensions.inputPrompt
import extensions.readAllInts

/**
 * 求三个数相加和为0的组合数量，要求使用平方级算法，参考ex15a
 */
fun ex15b_ThreeSumFaster(array: IntArray): Long {
    fun getZeroCount(array: IntArray): Int {
        var count = 0
        array.forEach { if (it == 0) count++ }
        return count
    }

    var count = 0L
    val zeroCount = getZeroCount(array)
    if (zeroCount > 2) {
        count += combination(zeroCount, 3)
    }
    for (i in array.indices) {
        if (array[i] >= 0) break
        var left = i + 1
        var right = array.size - 1
        while (left < right) {
            when {
                array[i] + array[left] + array[right] > 0 -> right--
                array[i] + array[left] + array[right] < 0 -> left++
                else -> {
                    var leftEqualCount = 1
                    val leftValue = array[left]
                    //两数和为0不用判断left<right-1，但是三数和需要判断，因为array[left]可能等于array[right]
                    while (left < right - 1 && array[left + 1] == leftValue) {
                        leftEqualCount++
                        left++
                    }

                    var rightEqualCount = 1
                    val rightValue = array[right]
                    while (right > left + 1 && array[right - 1] == rightValue) {
                        rightEqualCount++
                        right--
                    }
                    count += leftEqualCount * rightEqualCount
                    left++
                    right--
                }
            }
        }
    }
    return count
}

fun main() {
    inputPrompt()
    // -4 -3 -2 -2 -1 -1 -1 -1 0 0 0 0 0 1 1 2 4 4 4 5 5
    val array = readAllInts()
    println("ThreeSum count=${threeSum(array)}")
    array.sort()
    println("ex15b count=${ex15b_ThreeSumFaster(array)}")
}