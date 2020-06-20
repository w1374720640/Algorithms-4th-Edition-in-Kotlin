package chapter1.exercise1_4

import edu.princeton.cs.algs4.In
import extensions.formatDouble
import extensions.inputPrompt
import extensions.readString
import extensions.spendTimeMillis

/**
 * 求数组中三元素和为0的组合数量
 */
fun threeSum(array: IntArray): Long {
    val n = array.size
    var count = 0L
    for (i in 0 until n) {
        for (j in i + 1 until n) {
            for (k in j + 1 until n) {
                if (array[i] + array[j] + array[k] == 0) {
                    count++
                }
            }
        }
    }
    return count
}

fun main() {
    inputPrompt()
    //读取测试数据源，例如 ./data/1Kints.txt
    val path = readString()
    val array = In(path).readAllInts()
    val time = spendTimeMillis {
        val count = threeSum(array)
        println("count = $count")
    }
    println("spend time = ${formatDouble(time / 1000.0, 2)} s")
}