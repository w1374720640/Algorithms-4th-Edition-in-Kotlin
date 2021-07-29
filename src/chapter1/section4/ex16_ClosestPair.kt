package chapter1.section4

/**
 * 最接近的一对（一维）
 * 编写一个程序，给定一个含有N个double值的数组a[]，在其中找到一对最接近的值：两者之差（绝对值）最小的两个数。
 * 程序在最坏情况下所需的运行时间应该是线性对数级别的。
 */
fun ex16_ClosestPair(array: IntArray): Pair<Int, Int> {
    array.sort()
    var min = Int.MAX_VALUE
    var left = 0
    var right = 0
    for (i in 0 until array.size - 1) {
        if (array[i + 1] - array[i] < min) {
            left = array[i]
            right = array[i + 1]
            min = array[i + 1] - array[i]
        }
    }
    return left to right
}

fun main() {
//    inputPrompt()
//    val array = readAllInts()
//    val pair = ex16(array)
//    println("left = ${pair.first} right = ${pair.second} min = ${pair.second - pair.first}")
    timeRatio { ex16_ClosestPair(it) }
}