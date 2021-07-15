package chapter1.section1

import extensions.inputPrompt
import extensions.readInt

/**
 * 编写一个程序，从命令行得到三个整数参数。如果它们都相等则打印equal，否则打印not equal。
 */
fun ex3(args: Array<Int>): Boolean {
    if (args.isEmpty()) return false
    val start = args[0]
    for (i in 1 until args.size) {
        if (start != args[i]) return false
    }
    return true
}

fun main() {
    inputPrompt()
    val a = readInt("a: ")
    val b = readInt("b: ")
    val c = readInt("c: ")
    if (ex3(arrayOf(a, b, c))) {
        println("equal")
    } else {
        println("not equal")
    }
}
