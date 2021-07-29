package chapter1.section1

import extensions.inputPrompt
import extensions.readInt

/**
 * 请看以下递归函数
 * mystery(2, 25)和mystery(3, 11)的返回值是多少？
 * 给定正整数a和b，mystery(a, b)计算的结果是什么？
 * 将代码中的+替换为*并将return 0改为return 1，然后回答相同的问题
 *
 * 解：原函数每次将b减半，a加倍，最终结果为a和b的乘积
 * 将+替换为*并将return 0改为return 1后，每次b减半，a平方，最终结果为a的b次方
 */
fun ex18a(a: Int, b: Int): Int {
    if (b == 0) return 0
    if (b % 2 == 0) return ex18a(a + a, b / 2)
    return ex18a(a + a, b / 2) + a
}

fun ex18b(a: Int, b: Int): Int {
    if (b == 0) return 1
    if (b % 2 == 0) return ex18b(a * a, b / 2)
    return ex18b(a * a, b / 2) * a
}

fun main() {
    inputPrompt()
    val a = readInt("a: ")
    val b = readInt("b: ")
    println(ex18a(a, b))
    println(ex18b(a, b))
}