package chapter1.section3

import edu.princeton.cs.algs4.Stack
import extensions.inputPrompt
import extensions.readAllStrings

/**
 * 栈的可生成性
 * 假设我们的栈测试用例将会进行一系列混合的入栈和出栈操作，
 * 序列中的整数0，1，...，N-1（按此先后顺序排列）表示入栈操作，N个减号表示出栈操作。
 * 设计一个算法，判断给定的混合序列是否会使数组向下溢出（你所使用的空间量与N无关，既不能用某种数据结构存储所有整数）。
 * 设计一个线性时间的算法判定我们的测试用例能否产生某个给定的排列（这取决于出栈操作指令的出现位置）。
 * 解答：除非对于某个整数k，前k次出栈操纵会在前k次入栈操作前完成，否则栈不会向下溢出。
 * 如果某个排列可以产生，那么它产生的方式一定是唯一的：
 * 如果输出排列中的下一个整数在栈顶，则将它弹出，否则将它压入栈之中。
 */
/**
 * 判断栈是否会向下溢出
 */
fun ex45a_StackGenerability(array: Array<String>): Boolean {
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
fun ex45b_StackGenerability(originArray: Array<String>, resultArray: Array<String>): Boolean {
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
    if (ex45a_StackGenerability(originArray)) {
        println("Will overflow")
    } else {
        println("Won't overflow")
        //这个结果对应的原始序列为["0","1","2","3","-","4","-","-","-","-"]
        val resultArray = arrayOf("3", "4", "2", "1", "0")
        if (ex45b_StackGenerability(originArray, resultArray)) {
            println("Can produce a given sequence")
        } else {
            println("Cannot produce a given sequence")
        }
    }
}