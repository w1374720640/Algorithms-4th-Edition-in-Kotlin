package chapter2.section5

import chapter2.getCompareTimes
import chapter2.getDoubleArray
import extensions.formatDouble
import extensions.random

/**
 * 用select()找出N个元素中的最小值平均大约需要多少次比较
 */
fun main() {
    val size = 10000
    val times = 1000
    var compareTimes = 0L
    repeat(times) {
        compareTimes += getCompareTimes(getDoubleArray(size)) { array ->
            //因为需要找最小值，所以k=0
            select(array, 0)
        }
    }
    println("Average ${formatDouble((compareTimes / times) / size.toDouble(), 2)} N comparisons")
}