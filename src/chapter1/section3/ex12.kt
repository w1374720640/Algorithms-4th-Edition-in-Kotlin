package chapter1.section3

import edu.princeton.cs.algs4.Stack

/**
 * 编写一个可迭代的Stack用例，它含有一个静态的copy()方法，接受一个字符串的栈作为参数并返回该栈的一个副本。
 * 注意：这种能力是迭代器价值的一个重要体现，因为有了它我们无需改变基本API就能够实现这种功能。
 */
fun <T> Stack<T>.clone(): Stack<T> {
    val tempStack = Stack<T>()
    val iterator = this.iterator()
    while (iterator.hasNext()) {
        tempStack.push(iterator.next())
    }
    val resultStack = Stack<T>()
    while (!tempStack.isEmpty) {
        resultStack.push(tempStack.pop())
    }
    return resultStack
}

fun main() {
    val stack = Stack<String>()
    stack.push("0")
    stack.push("1")
    stack.push("2")
    stack.push("3")
    stack.push("4")
    stack.push("5")
    stack.push("6")
    stack.push("7")
    val cloneStack = stack.clone()
    println("stack      = ${stack.joinToString()}")
    println("cloneStack = ${cloneStack.joinToString()}")
}