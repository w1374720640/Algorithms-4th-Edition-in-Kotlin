package chapter5.section3

import chapter1.section3.DoublyLinkedDeque
import edu.princeton.cs.algs4.In

/**
 * BoyerMoore算法中的缓冲区
 * 为算法5.7添加一个search()方法，接受一个（In类型的）输入流作为参数并在给定的输入流中查找模式字符串。
 *
 * 解：BoyerMoore算法使用双向队列作为缓冲区，
 * 读取数据时从右侧存入缓存，匹配时从右侧开始匹配，
 * 当匹配失败时，继续从流中读取指定数量的字符，同时从左侧弹出字符，保持缓冲区定长
 */
fun BoyerMoore.search(input: In): Int {
    val deque = DoublyLinkedDeque<Char>()
    val M = pat.length
    var i = 0
    var j = M - 1
    var count = M // 需要读取的字符数量
    while (input.hasNextChar()) {
        if (count == 0) {
            while (deque.size() > M) {
                deque.popLeft()
            }
            val iterator = deque.reverseIterator()
            // 前面的代码保证了队列的大小等于M
            while (iterator.hasNext()) {
                val char = iterator.next()
                if (char == pat[j]) {
                    j--
                } else {
                    count = j - right[alphabet.toIndex(char)]
                    if (count <= 0) {
                        count = 1
                    }
                    j = M - 1
                    break
                }
            }
            if (j < 0) return i - M
        } else {
            deque.pushRight(input.readChar())
            i++
            count--
        }
    }
    return i
}

fun main() {
    testStreaming<BoyerMoore>(BoyerMoore::search) { BoyerMoore(it) }
    println("check succeed.")
}