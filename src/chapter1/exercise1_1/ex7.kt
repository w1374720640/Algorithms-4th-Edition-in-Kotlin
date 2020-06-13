package chapter1.exercise1_1

import kotlin.math.abs

//求打印结果，个人感觉没有实际意义
fun ex7a() {
    var t = 9.0f
    while (abs(t - 9.0f / t) > 0.001f) {
        t = (9.0f / t + t) / 2.0f
    }
    println(t)
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
}