package chapter1.section3

import edu.princeton.cs.algs4.Stack
import extensions.inputPrompt
import extensions.readAllStrings
import java.util.regex.Pattern

//预定义的操作符优先级
val operatorPrecedence = mapOf("+" to 1, "-" to 1, "*" to 2, "/" to 2)

//是否是操作符
fun String.isOperator() = when (this) {
    "+", "-", "*", "/" -> true
    else -> false
}

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

//求每个最小表达式的值
fun calculate(operator: String, first: String, second: String): String {
    val firstInt = first.toInt()
    val secondInt = second.toInt()
    return when (operator) {
        "+" -> (firstInt + secondInt).toString()
        "-" -> (firstInt - secondInt).toString()
        "*" -> (firstInt * secondInt).toString()
        "/" -> (firstInt / secondInt).toString()
        else -> ""
    }
}

/**
 * 将算术表达式由中序表达式转为后序表达式
 * 如：中序 2 * 3 / ( 2 - 1 ) + 3 * ( 4 - 1 ) 对应后序 2 3 * 2 1 - / 3 4 1 - * +
 * 关于前序、中序、后序参考这里：https://www.jianshu.com/p/a052eb2806a1
 * 1、创建两个栈，一个放值，一个放操作符和左括号
 * 2、规定每个操作符优先级
 * 3、遍历表达式，
 *    如果是数字添加到值栈，
 *    如果是操作符，比较和操作符栈顶的优先级，
 *        如果原操作符栈为空，或操作符栈顶是左括号，直接添加到操作符栈，
 *        如果小于等于栈顶优先级，从值栈中取出两个值，和栈顶操作符拼接，拼接值放入值栈中，然后将操作符放入操作符栈
 *        如果大于栈顶操作符优先级，直接添加到操作符栈，
 *    如果是左括号，放入操作符栈中，
 *    如果是右括号，从值栈中取出两个值，和栈顶操作符拼接后放入值栈中，并弹出栈中左括号
 * 4、遍历结束后如果操作符栈为空，直接返回值栈顶部值，否则从值栈中取出两个值，和栈顶操作符拼接后返回最终值
 */
fun ex10a(array: Array<String>): String {

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
                        valueStack.push("$value2 $value1 ${operatorStack.pop()}")
                        //如果需要直接求值而不是求后序表达式，用下面的代码替换上面的代码
                        //valueStack.push(calculate(operatorStack.pop(), value2, value1))
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
                valueStack.push("$value2 $value1 ${operatorStack.pop()}")
                //如果需要直接求值而不是求后序表达式，用下面的代码替换上面的代码
                //valueStack.push(calculate(operatorStack.pop(), value2, value1))
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
        valueStack.push("$value2 $value1 ${operatorStack.pop()}")
        //如果需要直接求值而不是求后序表达式，用下面的代码替换上面的代码
        //valueStack.push(calculate(operatorStack.pop(), value2, value1))
    }
    return valueStack.pop()
}

fun main() {
    //如：中序 2 * 3 / ( 2 - 1 ) + 3 * ( 4 - 1 ) 对应后序 2 3 * 2 1 - / 3 4 1 - * +
    inputPrompt()
    println(ex10a(readAllStrings()))
}