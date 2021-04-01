package chapter5.section3

import edu.princeton.cs.algs4.In
import edu.princeton.cs.algs4.Queue

/**
 * 流输入
 * 为KMP类添加一个search()方法，接受一个In类型的变量作为参数，
 * 在不适用其他任何实例变量的条件下在指定的输入流中查找模式字符串。
 * 为RabinKarp类也添加一个类似的方法。
 */
fun KMP.search(input: In): Int {
    var i = 0
    var j = 0
    while (input.hasNextChar() && j < pat.length) {
        val char = input.readChar()
        j = dfa[alphabet.toIndex(char)][j]
        i++
    }
    return if (j == pat.length) i - j else i
}

fun RabinKarp.search(input: In): Int {
    val M = pat.length
    val patHash = hash(pat, M)
    var i = 0
    var txtHash = 0L
    // 因为计算新hash值时需要减去第一个元素的hash值，所以使用队列保存M个字符
    // 从这个角度看，也可以算是需要回退的算法，用额外空间解决流不能回退的问题
    val charQueue = Queue<Char>()
    while (input.hasNextChar() && i < M) {
        val char = input.readChar()
        txtHash = (txtHash * alphabet.R() + alphabet.toIndex(char)) % Q
        i++
        charQueue.enqueue(char)
    }
    if (i < M) return i
    if (patHash == txtHash) return 0 // 流输入不能调用check方法
    while (input.hasNextChar()) {
        val nextChar = input.readChar()
        // 先计算旧hash值排除第一个元素后的hash值
        txtHash = (txtHash + Q - RM * alphabet.toIndex(charQueue.dequeue()) % Q) % Q
        // 再添加新元素重新计算hash值
        txtHash = (txtHash * alphabet.R() + alphabet.toIndex(nextChar)) % Q
        i++
        if (txtHash == patHash) return i - M
        charQueue.enqueue(nextChar)
    }
    return i
}

private fun getTinyTaleStream() = In("./data/tinyTale.txt")

private fun <T> testStreaming(searchFun: T.(In) -> Int, create: (String) -> T) {
    check(create("it was the best").searchFun(getTinyTaleStream()) == 0)
    check(create("t was").searchFun(getTinyTaleStream()) == 1)
    check(create(" ").searchFun(getTinyTaleStream()) == 2)
    check(create("worst").searchFun(getTinyTaleStream()) == 36)
    check(create("times\nit").searchFun(getTinyTaleStream()) == 45)
    check(create("it was the age").searchFun(getTinyTaleStream()) == 51)
    check(create("despair").searchFun(getTinyTaleStream()) == 269)
    check(create("it has").searchFun(getTinyTaleStream()) == 277)
}

fun main() {
    testStreaming<KMP>(KMP::search) { KMP(it) }
    testStreaming<RabinKarp>(RabinKarp::search) { RabinKarp(it) }
    println("check succeed.")
}