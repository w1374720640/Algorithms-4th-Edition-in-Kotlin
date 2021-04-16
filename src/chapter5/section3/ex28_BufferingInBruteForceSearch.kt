package chapter5.section3

import chapter1.section3.ResizingArrayQueue
import edu.princeton.cs.algs4.In

/**
 * 暴力子字符串查找算法中的缓冲区
 * 向你为练习5.3.1给出的解答中添加一个search()方法，
 * 接受一个（In类型的）输入流作为参数并在给定的输入流查找模式字符串。
 * 注意：你需要维护一个至少能够保存输入流的前M个字符的缓冲区。
 * 面临的挑战是要编写高效的代码为任意输入流初始化、更新和清理缓冲区。
 *
 * 解：使用一个队列作为缓冲区保存输入流中的字符串
 * 开始时直接从输入流中读取M个字符，匹配时直接匹配缓冲区中的字符，
 * 当匹配失败时，从队列中弹出一个字符，再从输入流中读取一个新字符，直到匹配成功或输入流结束
 */
fun BruteForceSearch.search(input: In): Int {
    var i = 0
    var j = 0
    // 使用基于数组的队列而不是基于链表的队列
    val queue = ResizingArrayQueue<Char>()
    // 读取pat.length-1个字符
    while (input.hasNextChar() && queue.size() < pat.length - 1) {
        queue.enqueue(input.readChar())
    }
    if (queue.size() < pat.length - 1) return queue.size()
    // 缓存区大小固定为pat.length
    while (input.hasNextChar() && j < pat.length) {
        val char = input.readChar()
        queue.enqueue(char)
        val iterator = queue.iterator()
        while (iterator.hasNext()) {
            if (iterator.next() == pat[j]) {
                j++
            } else {
                i++
                j = 0
                break
            }
        }
        queue.dequeue()
    }
    return if (j == pat.length) i else i + queue.size()
}

fun main() {
    testStreaming<BruteForceSearch>(BruteForceSearch::search) { BruteForceSearch(it) }
    println("check succeed.")
}