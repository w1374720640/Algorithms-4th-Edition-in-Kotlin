package chapter1.section3

import edu.princeton.cs.algs4.Stack

/**
 * 给定以下输入，java Stack的输出是什么
 * it was - the best - of times - - - it was - the - -
 */
fun ex2(s: String) {
    val stack = Stack<String>()
    val list = s.split(" ")
    list.forEach {
        if (it == "-") {
            println(stack.pop())
        } else {
            stack.push(it)
        }
    }
}

fun main() {
    val s = "it was - the best - of times - - - it was - the - -"
    ex2(s)
}