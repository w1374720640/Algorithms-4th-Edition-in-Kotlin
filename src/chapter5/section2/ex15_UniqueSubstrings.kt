package chapter5.section2

import edu.princeton.cs.algs4.Queue
import extensions.inputPrompt
import extensions.readString

/**
 * 不同子字符串
 * 编写一个TST的用例，从标准输入读取文本并计算其中任意长度的不同子字符串的数量。
 * 后缀树能够高效完成这个任务————请见第6章
 */
fun ex15_UniqueSubstrings(string: String): Iterable<Iterable<String>> {
    val queue = Queue<Iterable<String>>()
    for (i in 1..string.length) {
        queue.enqueue(ex14_UniqueSubstringsOfLengthL(string, i))
    }
    return queue
}

fun main() {
    inputPrompt()
    val string = readString()
    val iterator = ex15_UniqueSubstrings(string).iterator()
    var length = 1
    while (iterator.hasNext()) {
        val iterable = iterator.next()
        println("length=$length ${iterable.joinToString()}")
        length++
    }
}