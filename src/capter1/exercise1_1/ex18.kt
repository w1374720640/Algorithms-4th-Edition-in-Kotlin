package capter1.exercise1_1

import extensions.inputPrompt
import extensions.readInt

//求给定正整数a和b，ex18(a, b)的计算结果
//将 + 替换为 * ,return 0 改为 return 1 然后回答问题
fun ex18(a: Int, b: Int): Int {
    if (b == 0) return 0
    if (b % 2 == 0) return ex18(a + a, b / 2)
    return ex18(a + a, b / 2) + a
}

fun main() {
    inputPrompt()
    println(ex18(readInt(), readInt()))
}