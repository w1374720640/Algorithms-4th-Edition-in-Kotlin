package chapter1.section1

import extensions.inputPrompt
import extensions.readDouble

//判断两个数是否都在0和1之间
fun ex5(x: Double, y: Double): Boolean {
    fun between0And1(num: Double): Boolean = num > 0 && num < 1
    return between0And1(x) && between0And1(y)
}

fun main() {
    inputPrompt()
    val a = readDouble()
    val b = readDouble()
    if (ex5(a, b)) {
        println("true")
    } else {
        println("false")
    }
}