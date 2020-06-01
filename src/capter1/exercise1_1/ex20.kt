package capter1.exercise1_1

import extensions.inputPrompt
import extensions.readInt

//计算N的阶乘
//递归版本
fun ex20a(N: Int): Int {
    if (N == 0) return 1
    return ex20a(N - 1) * N
}

//计算N的阶乘
//循环版本
fun ex20b(N: Int): Int {
    if (N == 0) return 1
    var result = 1
    for (i in 1..N) {
        result *= i
    }
    return result
}

fun main() {
    inputPrompt()
    val N = readInt()
    println("${N}!=${ex20b(N)}")
}