package chapter1.section3

import edu.princeton.cs.algs4.Stack

/**
 * 返回一个栈的副本
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