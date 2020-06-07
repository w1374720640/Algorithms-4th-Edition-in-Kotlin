package capter1.exercise1_3

import edu.princeton.cs.algs4.Stack
import extensions.inputPrompt
import extensions.readAllStrings

/**
 * 求一个后序表达式的值
 * 和练习1.3.10结合可以直接求中序表达式的值
 */
fun ex11(array: Array<String>): String {
    fun String.isOperator() = "+" == this || "-" == this || "*" == this || "/" == this

    //求每个最小表达式的值
    fun calculate(operator: String, first: String, second: String): String {
        val firstInt = first.toInt()
        val secondInt = second.toInt()
        return when(operator) {
            "+" -> (firstInt + secondInt).toString()
            "-" -> (firstInt - secondInt).toString()
            "*" -> (firstInt * secondInt).toString()
            "/" -> (firstInt / secondInt).toString()
            else -> ""
        }
    }

    val valueStack = Stack<String>()
    array.forEach {
        if (it.isOperator()) {
            val value1 = valueStack.pop()
            val value2 = valueStack.pop()
            valueStack.push(calculate(it, value2, value1))
        } else {
            valueStack.push(it)
        }
    }
    return valueStack.pop()
}


fun main() {
    inputPrompt()
    //后序表达式
    val postOrderExpression = ex10(readAllStrings())
    println("Post-order expression: $postOrderExpression")
    val result = ex11(postOrderExpression.split(" ").toTypedArray())
    println("result=$result")
}