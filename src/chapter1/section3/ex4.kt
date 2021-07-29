package chapter1.section3

import edu.princeton.cs.algs4.Stack
import extensions.inputPrompt

/**
 * 编写一个Stack的用例Parentheses，从标准输入中读取一个文本流并使用栈判定其中的括号是否配对完整。
 * 例如，对于[()]{}{[()()]()}程序应打印true，对于[(])则打印false。
 */
fun ex4(array: CharArray): Boolean {
    val stack = Stack<Char>()
    for (i in array.indices) {
        when (val char = array[i]) {
            ' ' -> continue
            ')' -> if (stack.isEmpty || '(' != stack.pop()) return false
            ']' -> if (stack.isEmpty || '[' != stack.pop()) return false
            '}' -> if (stack.isEmpty || '{' != stack.pop()) return false
            else -> {
                stack.push(char)
            }
        }
    }
    return stack.isEmpty
}

fun main() {
    inputPrompt()
    val input = readLine() ?: return
    val result = ex4(input.toCharArray())
    println(result)
}