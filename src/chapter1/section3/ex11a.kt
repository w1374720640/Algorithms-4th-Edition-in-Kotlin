package chapter1.section3

import edu.princeton.cs.algs4.Stack
import extensions.inputPrompt
import extensions.readAllStrings

/**
 * 编写一段程序EvaluatePostfix，从标准输入中得到一个后序表达式，求值并打印结果
 * （将上一题中的程序中得到的输出用管道传递给这一段程序可以得到和Evaluate相同的行为）。
 *
 * 解：后序表达式求值过程：从前向后读取数据，如果为数字，则放入栈中，如果为操作符，从栈中取出两条数据，
 * 执行运算，遍历结束时，栈中存在的唯一值就是结果
 * 中序 2 * 3 / ( 2 - 1 ) + 3 * ( 4 - 1 ) 对应后序 2 3 * 2 1 - / 3 4 1 - * +
 */
fun ex11a(array: Array<String>): String {
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
    // 读取中序表达式
    val midOrderExpression = readAllStrings()
    // 转换成后序表达式
    val postOrderExpression = ex10a(midOrderExpression)
    println("Post-order expression: $postOrderExpression")
    println("result=${ex11a(postOrderExpression.split(" ").toTypedArray())}")
    // 转换成前序表达式
    val preorderExpression = ex10b(midOrderExpression)
    println("Preorder expression: $preorderExpression")
    println("result=${ex11b(preorderExpression.split(" ").toTypedArray())}")
}