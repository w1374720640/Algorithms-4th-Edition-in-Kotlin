package chapter1.exercise1_3

import edu.princeton.cs.algs4.Stack
import extensions.inputPrompt
import extensions.readAllStrings

/**
 * 求一个后序表达式的值
 * 和练习1.3.10结合可以直接求中序表达式的值
 * 后序表达式求值过程：从前向后读取数据，如果为数字，则放入栈中，如果为操作符，从栈中取出两条数据，
 * 执行运算，遍历结束时，栈中存在的唯一值就是结果
 */
fun ex11a(array: Array<String>): String {
    fun String.isOperator() = "+" == this || "-" == this || "*" == this || "/" == this

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
    val midOrderExpression = readAllStrings()
    //后序表达式
    val postOrderExpression = ex10a(midOrderExpression)
    println("Post-order expression: $postOrderExpression")
    println("result=${ex11a(postOrderExpression.split(" ").toTypedArray())}")
    val preorderExpression = ex10b(midOrderExpression)
    println("Preorder expression: $preorderExpression")
    println("result=${ex11b(preorderExpression.split(" ").toTypedArray())}")
}