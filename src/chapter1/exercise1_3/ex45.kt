package chapter1.exercise1_3

import edu.princeton.cs.algs4.Stack
import extensions.inputPrompt
import extensions.readAllStrings

/**
 * 序列中的整数表示入栈操作，减号表示出栈操作
 * 判断给定的混合序列是否会使数组向下溢出
 */
fun ex45a(array: Array<String>): Boolean {
    var size = 0
    array.forEach {
        if (it == "-") {
            size--
        } else {
            size++
        }
        if (size < 0) {
            return true
        }
    }
    return false
}

/**
 * 判断由整数和减号组成的序列中，进行入栈和出栈操作，能否产生给定的排序
 */
fun ex45b(originArray: Array<String>, resultArray: Array<String>): Boolean {
    val stack = Stack<String>()
    var index = 0
    originArray.forEach {
        if (it == "-") {
            if (stack.pop() != resultArray[index++]) return false
        } else {
            stack.push(it)
        }
    }
    return index == resultArray.size
}

fun main() {
    inputPrompt()
    //输入中普通字符表示入栈，减号 - 表示出栈
    val originArray = readAllStrings()
    if (ex45a(originArray)) {
        println("Will overflow")
    } else {
        println("Won't overflow")
        //这个结果对应的原始序列为["0","1","2","3","-","4","-","-","-","-"]
        val resultArray = arrayOf("3", "4", "2", "1", "0")
        if (ex45b(originArray, resultArray)) {
            println("Can produce a given sequence")
        } else {
            println("Cannot produce a given sequence")
        }
    }
}