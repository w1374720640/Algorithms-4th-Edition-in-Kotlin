package chapter2.section3

import chapter2.compare
import chapter2.section1.checkAscOrder
import chapter2.swap
import extensions.random
import extensions.spendTimeMillis

/**
 * 给出一段代码，将已知只有两种主键值的数组排序
 *
 * 解：用快速排序的三路算法排序一次即可得到正确结果
 */
fun <T : Comparable<T>> ex5(array: Array<T>) {
    if (array.size <= 1) return
    var lowerThan = 0
    var greaterThan = array.size - 1
    var i = 1
    val value = array[0]
    while (i <= greaterThan) {
        val result = array.compare(i, value)
        when {
            result < 0 -> {
                if (lowerThan != i) {
                    array.swap(lowerThan, i)
                }
                lowerThan++
                i++
            }
            result > 0 -> {
                if (greaterThan != i) {
                    array.swap(greaterThan, i)
                }
                greaterThan--
            }
            else -> i++
        }
    }
}

fun main() {
    val size = 1000_0000

    val array = Array(size) { random(2) }
    val time = spendTimeMillis {
        ex5(array)
    }
    check(array.checkAscOrder())
    println("ex5 spend $time ms")

    val quickSort3WayArray = Array(size) { random(2) }
    val quickSort3WayTime = spendTimeMillis {
        quickSort3Way(quickSort3WayArray)
    }
    check(quickSort3WayArray.checkAscOrder())
    println("quickSort3Way spend $quickSort3WayTime ms")
}