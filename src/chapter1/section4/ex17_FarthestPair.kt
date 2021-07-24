package chapter1.section4

/**
 * 最遥远的一对（一维）
 * 编写一个程序，给定一个含有N个double值的数组a[]，在其中找到一对最遥远的值：两者之差（绝对值）最大的两个数。
 * 程序在最坏情况下所需的运行时间应该是线性对数级别的。
 */
fun ex17_FarthestPair(array: IntArray): Pair<Int, Int> {
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
    timeRatio { ex17_FarthestPair(it) }
}