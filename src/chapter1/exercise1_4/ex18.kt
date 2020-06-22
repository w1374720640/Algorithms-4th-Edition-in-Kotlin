package chapter1.exercise1_4

import extensions.inputPrompt
import extensions.readAllInts

/**
 * 求N个不同整数数组的局部最小值的索引
 * 局部最小值表示a[i]<a[i-1]且a[i]<a[i+1]
 * 最左侧和最右侧的值只需要和一侧的值比较，比隔壁值小则为局部最小值
 * 所以N个不同整数的数组肯定有局部最小值
 */
fun ex18(array: IntArray): Int {
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
    println("Local minimum index = ${ex18(array)}")
}