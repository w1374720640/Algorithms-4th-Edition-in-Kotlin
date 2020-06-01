package capter1.exercise1_1

import extensions.inputPrompt
import extensions.readInt

//使用欧几里得算法计算最大公约数
fun ex24(a: Int, b: Int): Int {
    require(a > 0 && b > 0)
    //两个整数的最大公约数等于其中较小的那个数和两数相除余数的最大公约数
    val large = if (a > b) a else b
    val small = if (a > b) b else a
    //商数
    val quotient = large / small
    //余数
    val remainder = large % small
    if (remainder == 0) return small
    return ex24(small, remainder)
}

fun main() {
    inputPrompt()
    val a = readInt()
    val b = readInt()
    println("The greatest common divisor of ${a} and ${b} is ${ex24(a, b)}")
}