package chapter1.section1

import extensions.formatDouble
import extensions.inputPrompt
import extensions.readInt
import kotlin.math.ln

/**
 * 编写一个递归的静态方法计算ln(N!)的值
 *
 * 解：由对数函数的性质可知：lnMN=lnM+lnN，ln(N!)=lnN+ln(N-1)+ln(N-2)+...+ln1
 * 分别使用递归和循环计算ln(N!)的值
 */
//递归版本
fun ex20a(N: Int): Double {
    require(N > 0)
    if (N == 1) return 0.0
    return ln(N.toDouble()) + ex20a(N - 1)
}

//循环版本
fun ex20b(N: Int): Double {
    require(N > 0)
    if (N == 1) return 0.0
    var result = 0.0
    for (i in 1..N) {
        result += ln(i.toDouble())
    }
    return result
}

fun main() {
    inputPrompt()
    val N = readInt()
    println("ex20a: ${formatDouble(ex20a(N), 3)}")
    println("ex20b: ${formatDouble(ex20b(N), 3)}")
}