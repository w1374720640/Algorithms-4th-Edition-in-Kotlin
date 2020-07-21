package chapter1.section1

import extensions.inputPrompt
import extensions.readInt

//判断数组中的所有值是否相同
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
    val a = readInt()
    val b = readInt()
    val c = readInt()
    if (ex3(arrayOf(a, b, c))) {
        println("equal")
    } else {
        println("not equal")
    }
}
