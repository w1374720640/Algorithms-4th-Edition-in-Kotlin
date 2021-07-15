package chapter1.section1

import extensions.inputPrompt
import extensions.readDouble

/**
 * 编写一段程序，如果double类型的变量x和y都严格位于0和1之间则打印true，否则打印false
 */
fun ex5(x: Double, y: Double): Boolean {
    fun between0And1(num: Double): Boolean = num > 0 && num < 1
    return between0And1(x) && between0And1(y)
}

fun main() {
    inputPrompt()
    val x = readDouble("x: ")
    val y = readDouble("y: ")
    if (ex5(x, y)) {
        println("true")
    } else {
        println("false")
    }
}