package capter1.exercise1_3

import edu.princeton.cs.algs4.Stack
import extensions.inputPrompt

/**
 * 判断括号是否完全匹配
 * 如：[()]{}{[()()]()}打印true，[(])打印false
 */
fun ex4(array: CharArray): Boolean {
    val stack = Stack<Char>()
    array.forEach {
        when (it) {
            ' ' -> return@forEach
            ')' -> if ('(' != stack.pop()) return false
            ']' -> if ('[' != stack.pop()) return false
            '}' -> if ('{' != stack.pop()) return false
            else -> {
                stack.push(it)
            }
        }
    }
    return true
}

fun main() {
    inputPrompt()
    val input = readLine()
    val result = if (input.isNullOrEmpty()) false else ex4(input.toCharArray())
    println(result)
}