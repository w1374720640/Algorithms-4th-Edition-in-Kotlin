package capter1.exercise1_3

import edu.princeton.cs.algs4.Stack

/**
 * 求一个前序表达式的值
 * 和练习1.3.10结合可以直接求中序表达式的值
 * 和后序表达式求值的区别是：从后向前读取数据，且求最小表达式值时参数顺序不同
 */
fun ex11b(array: Array<String>): String {

    val valueStack = Stack<String>()
    //从后向前读取数据
    for (i in (array.size - 1) downTo 0) {
        val value = array[i]
        if (value.isOperator()) {
            val value1 = valueStack.pop()
            val value2 = valueStack.pop()
            valueStack.push(calculate(value, value1, value2))
        } else {
            valueStack.push(value)
        }
    }
    return valueStack.pop()
}
