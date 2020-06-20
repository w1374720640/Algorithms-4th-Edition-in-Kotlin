package chapter1.exercise1_4

import extensions.printf
import extensions.random
import extensions.spendTimeMillis

fun timeTrial(N: Int): Long {
    val MAX = 1000000
    val a = IntArray(N)
    for (i in 0 until N) {
        a[i] = random(-MAX, MAX)
    }
    return spendTimeMillis {
        threeSum(a)
    }
}

/**
 * 判断程序运行时间大致的增长数量级
 */
fun main() {
    var prev = timeTrial(125)
    var n = 250
    while (true) {
        val time = timeTrial(n)
        printf("%6d %7d %5.1f\n", n, time, time.toFloat() / prev)
        n *= 2
        prev = time
    }
}