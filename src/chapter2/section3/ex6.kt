package chapter2.section3

import chapter2.comparisonCallbackList
import chapter2.getDoubleArray
import extensions.formatDouble
import extensions.formatInt
import extensions.formatLong
import kotlin.math.ln

/**
 * 计算快速排序的交换次数，返回实际值和估计值2NlnN
 */
fun <T : Comparable<T>> ex6(array: Array<T>): Pair<Long, Long> {
    var count = 0L
    val callback: (Any, Int, Int) -> Unit = { tag, _, _ ->
        if (tag === array) {
            count++
        }
    }
    comparisonCallbackList.add(callback)
    quickSort(array)
    comparisonCallbackList.remove(callback)
    return count to (2 * array.size * ln(array.size.toDouble())).toLong()
}

fun main() {
    var size = 100
    repeat(3) {
        val array = getDoubleArray(size)
        val pair = ex6(array)
        println("size=${formatInt(size, 6)} actual=${formatLong(pair.first, 8)} expected=${formatLong(pair.second, 8)} " +
                "ratio=${formatDouble(pair.first.toDouble() / pair.second, 2)}")
        size *= 10
    }
}