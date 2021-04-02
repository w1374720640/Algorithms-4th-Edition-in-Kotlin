package chapter5.section3

import edu.princeton.cs.algs4.In
import edu.princeton.cs.algs4.Queue

/**
 * 暴力子字符串查找算法中的缓冲区
 * 向你为练习5.3.1给出的解答中添加一个search()方法，
 * 接受一个（In类型的）输入流作为参数并在给定的输入流查找模式字符串。
 * 注意：你需要维护一个至少能够保存输入流的前M个字符的缓冲区。
 * 面临的挑战是要编写高效的代码为任意输入流初始化、更新和清理缓冲区。
 *
 * 解：练习5.3.25中RabinKarp的search()方法也需要缓存字符串，但是缓存的字符串是定长的，
 * 暴力子字符串缓存的字符串是不定长的，
 * 每次从流中取出一个字符放入缓存中，如果匹配失败，需要先检查缓存中字符，
 * 只有当缓存中的字符都检查完毕时才从流中取下一个字符
 */
fun BruteForceSearch.search(input: In): Int {
    var i = 0
    var j = 0
    val queue = Queue<Char>()
    while (input.hasNextChar() && j < pat.length) {
        val char = input.readChar()
        if (j != 0) {
            // j==0时，当前字符不会被再次检查，不会被缓存
            queue.enqueue(char)
        }
        if (char == pat[j]) {
            i++
            j++
        } else {
            i = i - j + 1
            j = 0
            // 匹配失败时需要检查缓存队列中的内容
            label@ while (!queue.isEmpty) {
                // 使用迭代器而不是直接出队
                val iterator = queue.iterator()
                while (iterator.hasNext()) {
                    val nextChar = iterator.next()
                    if (nextChar == pat[j]) {
                        i++
                        j++
                    } else {
                        queue.dequeue()
                        i = i - j + 1
                        j = 0
                        // 重新遍历缓存的字符队列
                        continue@label
                    }
                }
                // 缓存的所有字符都相同，继续从流中取出下一个字符
                break
            }
        }
    }
    return if (j == pat.length) i - pat.length else i
}

fun main() {
    testStreaming<BruteForceSearch>(BruteForceSearch::search) { BruteForceSearch(it) }
    println("check succeed.")
}