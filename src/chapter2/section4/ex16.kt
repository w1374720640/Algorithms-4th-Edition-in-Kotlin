package chapter2.section4

import chapter2.getCompareTimes
import extensions.formatInt
import extensions.shuffle

/**
 * 对于N=32，构造数组使得堆排序使用的比较次数最多以及最少
 * 参考https://alg4.ikesnowy.com/2-4-16/
 * 最坏输入论文https://arxiv.org/abs/1504.01459
 */
fun main() {
    val size = 32

    val worstArray = arrayOf(1, 4, 7, 12, 10, 16, 14, 19, 17, 20, 5, 27, 8, 28, 2, 24, 9, 18, 6, 23, 11, 22, 21, 31, 13, 26, 25, 30, 15, 29, 3, 32)
    val bestArray = Array(size) { 1 }
    println("worst: ${getCompareTimes(worstArray, ::heapSort)}")
    println("best: ${getCompareTimes(bestArray, ::heapSort)}")

    val ascArray = Array(size) { it + 1 }
    println("asc: ${getCompareTimes(ascArray, ::heapSort)}")
    val descArray = Array(size) { 32 - it }
    println("desc: ${getCompareTimes(descArray, ::heapSort)}")

    repeat(10) {
        val randomArray = Array(size) { it + 1 }
        shuffle(randomArray)
        val copyArray = randomArray.copyOf()
        val times = getCompareTimes(copyArray, ::heapSort)
        println("random times=${times} array=${randomArray.joinToString { formatInt(it, 2) }}")
    }
}