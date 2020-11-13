package chapter3.section5

import chapter2.section5.ex31_Distinct
import extensions.formatDouble
import extensions.formatInt
import extensions.random
import extensions.spendTimeMillis
import kotlin.math.pow

/**
 * 重复元素（续）
 * 使用3.5.2.1节的dedup过滤器重新完成练习2.5.31，比较两种解决方法的运行时间
 * 然后使用dedup运行实验，其中N=10⁷、10⁸和10⁹
 * 使用随机的long值重新完成实验并讨论结果
 *
 * 解：应该是统计不重复元素的数量而不是重复元素，两个数相等记数量为1
 * [M]代表随机值的取值范围0~M-1，[N]代表元素数量，[T]代表重复次数
 */
fun ex30_DuplicatesRevisited(M: Int, N: Int, T: Int): Pair<Int, Double> {
    var count = 0
    repeat(T) {
        val set = LinearProbingHashSET<Int>()
        repeat(N) {
            set.add(random(M))
        }
        count += set.size()
    }
    val expect = M * (1 - Math.E.pow(N.toDouble() / M * -1))
    return count / T to expect
}

fun main() {
    val T = 10
    val N = 100_0000
    var M = N / 2
    repeat(3) {
        var count1 = 0
        var count2 = 0
        val time1 = spendTimeMillis {
            count1 = ex31_Distinct(M, N, T).first
        }
        val time2 = spendTimeMillis {
            count2 = ex30_DuplicatesRevisited(M, N, T).first
        }
        println("N:${formatInt(N, 7)} M:${formatInt(M, 7)}")
        println("count1=$count1 time1=$time1 ms")
        println("count2=$count2 time2=$time2 ms")
        println()
        M *= 2
    }
}