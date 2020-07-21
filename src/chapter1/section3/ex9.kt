package chapter1.section3

import edu.princeton.cs.algs4.Stack
import extensions.inputPrompt
import extensions.readAllStrings

/**
 * 从一个缺少左括号的表达式获取补全括号后的中序表达式
 * 如：输入1 + 2 ) * 3 - 4 ) * 5 - 6 ) ) ) 返回 ( 1 + 2 ) * ( ( 3 - 4 ) * ( 5 - 6 ) ) )
 */
fun ex9(array: Array<String>): String {
    val valueStack = Stack<String>()
    val operatorStack = Stack<String>()
    array.forEach {
        when (it) {
            ")" -> {
                val value1 = valueStack.pop()
                val value2 = valueStack.pop()
                val operator = operatorStack.pop()
                valueStack.push("( $value2 $operator $value1 )")
            }
            "+", "-", "*", "/" -> {
                operatorStack.push(it)
            }
            else -> valueStack.push(it)
        }
    }
    return valueStack.pop()
}

fun main() {
    inputPrompt()
    //每个数字、括号、操作符之间必须用空格或换行分隔
    val array = readAllStrings()
    val result = ex9(array)
    println(result)
}