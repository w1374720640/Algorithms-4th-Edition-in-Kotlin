package chapter2.section1

import chapter2.comparisonCallbackList
import edu.princeton.cs.algs4.StdRandom
import extensions.inputPrompt
import extensions.readInt

/**
 * 构造一个希尔排序的最坏情况
 * 要求用1到100构造一个大小为100的数组，用希尔排序和递增序列1 4 13 40对其排序，使对比次数尽可能的多
 *
 * 解：无法从理论上得出最坏情况，使用随机打乱的方式，暴力计算最大的比较次数（倒序不是最坏情况）
 */
fun ex19(size: Int): Pair<Long, Array<Int>> {
    var count = 0L
    val array = Array(size) { it + 1 }
    StdRandom.shuffle(array)
    val originArray = array.copyOf()
    val comparisonCallback = { tag: Any, i: Int, j: Int ->
        if (tag === array) {
            count++
        }
    }
    comparisonCallbackList.add(comparisonCallback)
    shellSort(array)
    comparisonCallbackList.remove(comparisonCallback)
    return count to originArray
}

fun main() {
    inputPrompt()
    val size = readInt("size: ")
    var maxComparisonNum = 0L
    while (true) {
        val pair = ex19(size)
        if (pair.first > maxComparisonNum) {
            maxComparisonNum = pair.first
            println("maxComparisonNum: $maxComparisonNum")
            println(pair.second.joinToString())
        }
    }
}