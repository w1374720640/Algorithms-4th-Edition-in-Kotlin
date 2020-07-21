package chapter1.section4

/**
 * 找出数组中两者之差绝对值最大的数，要求程序在最坏情况下所需的运行时间是线性级别的
 */
fun ex17(array: IntArray): Pair<Int, Int> {
    var min = Int.MAX_VALUE
    var max = Int.MIN_VALUE
    array.forEach {
        if (it < min) {
            min = it
        }
        if (it > max) {
            max = it
        }
    }
    return min to max
}

fun main() {
    timeRatio { ex17(it) }
}