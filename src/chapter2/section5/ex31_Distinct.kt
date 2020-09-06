package chapter2.section5

import extensions.formatDouble
import extensions.formatInt
import extensions.random
import kotlin.math.pow

/**
 * 重复元素
 * 编写一段程序，接受命令行参数M、N和T，然后使用正文中的代码进行T遍实验：
 * 生成N个0到M-1间的int值并计算重复值的个数。
 * 令T=10，N=10³、10⁴、10⁵和10⁶以及M=N/2、N和2N
 * 根据概率论，重复值的个数应该约为1-e^(-a)，其中a=N/M
 * 打印一张表格来确认你的实验验证了这个公式
 *
 * 解：题目中有两处错误！！！
 * 这里不是统计重复值的个数，而是不重复值的个数，否则和公式不符
 * 不重复个数的公式1-e^(-a)还需要再乘以M才是正确答案
 */
fun ex31_Distinct(M: Int, N: Int, T: Int): Pair<Int, Double> {
    var count = 0
    repeat(T) {
        val array = Array(N) { random(M) }
        array.sort()
        var diffCount = 1
        for (i in 1 until array.size) {
            if (array[i] != array[i - 1]) {
                diffCount++
            }
        }
        count += diffCount
    }
    val expect = M * (1 - Math.E.pow(N.toDouble() / M * -1))
    return count / T to expect
}

fun main() {
    val T = 10
    var N = 1000
    repeat(4) {
        var M = N / 2
        repeat(3) {
            val result = ex31_Distinct(M, N, T)
            println("N:${formatInt(N, 7)} M:${formatInt(M, 7)} count:${formatInt(result.first, 7)} expect:${formatDouble(result.second, 2, 7)}")
            M *= 2
        }
        N *= 10
    }
}