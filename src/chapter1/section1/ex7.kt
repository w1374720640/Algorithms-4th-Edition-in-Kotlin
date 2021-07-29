package chapter1.section1

import extensions.formatDouble
import kotlin.math.abs

/**
 * 分别给出以下代码段打印出的值
 */
fun ex7a() {
    var t = 9.0
    while (abs(t - 9.0 / t) > 0.001) {
        t = (9.0 / t + t) / 2.0
    }
    println(formatDouble(t, 5))
}

fun ex7b() {
    var sum = 0
    for (i in 1 until 1000) {
        for (j in 0 until i) {
            sum++
        }
    }
    println(sum)
}

fun ex7c() {
    var sum = 0
    var i = 1
    while (i < 1000) {
        for (j in 0 until 1000) {
            sum++
        }
        i *= 2
    }
    println(sum)
}

fun main() {
    ex7a()
    ex7b()
    ex7c()
}