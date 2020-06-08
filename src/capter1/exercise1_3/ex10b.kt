package capter1.exercise1_3

import edu.princeton.cs.algs4.Stack
import extensions.inputPrompt
import extensions.readAllStrings
import java.util.regex.Pattern

/**
 * 将中序表达式转化为前序表达式
 * 如：中序 2 * 3 / ( 2 - 1 ) + 3 * ( 4 - 1 ) 对应前序 + / * 2 3 - 2 1 * 3 - 4 1
 * 和中序转后序大体相同，不同的地方在于拼接两个值和操作符时，操作符放在前面
 */
fun ex10b(array: Array<String>): String {
//预定义的操作符优先级
    val operatorPrecedence = mapOf("+" to 1, "-" to 1, "*" to 2, "/" to 2)

    //是否是操作符
    fun String.isOperator() = "+" == this || "-" == this || "*" == this || "/" == this

    //是否是左括号
    fun String.isLeftBracket() = "(" == this

    //是否是右括号
    fun String.isRightBracket() = ")" == this

    //是否是数值
    fun String.isNum() = Pattern.matches("^-?[1-9]\\d*\$", this)

    //比较两个操作符的优先级
    fun String.largePrecedence(topOperator: String): Boolean {
        require(isOperator() && topOperator.isOperator()) { "This method only accepts operators as parameters" }
        return operatorPrecedence[this]!! > operatorPrecedence[topOperator]!!
    }

    //检查是否有未知字符，主要因为没有正确用空格分隔表达式
    array.forEach {
        require(it.isNum() || it.isOperator() || it.isLeftBracket() || it.isRightBracket()) {
            "Contains unknown characters '${it}'"
        }
    }

    val valueStack = Stack<String>()
    val operatorStack = Stack<String>()
    array.forEach {
        when {
            it.isOperator() -> {
                when {
                    operatorStack.isEmpty || operatorStack.peek().isLeftBracket() -> {
                        //操作符栈为空直接添加数据
                        operatorStack.push(it)
                    }
                    it.largePrecedence(operatorStack.peek()) -> {
                        //新操作符优先级较高直接将操作符加入到栈中
                        operatorStack.push(it)
                    }
                    else -> {
                        //新操作符优先级小于等于栈顶操作符优先级（前面已经判断了栈顶操作符不是左括号）
                        //从值栈中取出两个值，用栈顶操作符连接后放入值栈
                        //最后将操作符放入栈中
                        val value1 = valueStack.pop()
                        val value2 = valueStack.pop()
                        //和后序的唯一区别在这里
                        valueStack.push("${operatorStack.pop()} $value2 $value1")
                        operatorStack.push(it)
                    }
                }
            }
            it.isLeftBracket() -> {
                operatorStack.push(it)
            }
            it.isRightBracket() -> {
                //右括号从值栈中取出两个值，用栈顶操作符连接后放入值栈
                //再弹出左括号
                val value1 = valueStack.pop()
                val value2 = valueStack.pop()
                valueStack.push("${operatorStack.pop()} $value2 $value1")
                operatorStack.pop()
            }
            else -> {
                //数值直接添加到值栈
                valueStack.push(it)
            }
        }
    }
    while (!operatorStack.isEmpty) {
        val value1 = valueStack.pop()
        val value2 = valueStack.pop()
        valueStack.push("${operatorStack.pop()} $value2 $value1")
    }
    return valueStack.pop()
}

fun main() {
    //如：中序 2 * 3 / ( 2 - 1 ) + 3 * ( 4 - 1 ) 对应前序 + / * 2 3 - 2 1 * 3 - 4 1
    inputPrompt()
    println(ex10b(readAllStrings()))
}