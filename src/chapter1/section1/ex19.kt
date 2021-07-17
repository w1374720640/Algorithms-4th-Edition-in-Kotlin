package chapter1.section1

import extensions.inputPrompt
import extensions.readInt
import extensions.spendTimeMillis

/**
 * 在计算机上运行以下程序：
 * 计算机用这段程序在一个小时内能够得到F(N)结果的最大N值是多少？
 * 开发F(N)的一个更好的实现，用数组保存已经计算过的值。
 */
//低效版本
fun ex19a(N: Int): Long {
    require(N >= 0)
    if (N == 0) return 0
    if (N == 1) return 1
    return ex19a(N - 1) + ex19a(N - 2)
}

//高效版本 使用循环改进低效率的递归，消除重复的回溯
fun ex19b(N: Int): Long {
    require(N >= 0)
    if (N == 0) return 0
    if (N == 1) return 1
    val array = arrayOf<Long>(0, 1, 0)
    for (i in 2..N) {
        array[i % 3] = array[(i - 1) % 3] + array[(i - 2) % 3]
    }
    return array[N % 3]
}

fun main() {
    inputPrompt()
    val N = readInt("N: ")
    var resultA = 0L
    val timeA = spendTimeMillis {
        resultA = ex19a(N)
    }
    println("ex19a: $resultA  spend: $timeA ms")
    var resultB = 0L
    val timeB = spendTimeMillis {
        resultB = ex19b(N)
    }
    println("ex19b: $resultB  spend: $timeB ms")

}