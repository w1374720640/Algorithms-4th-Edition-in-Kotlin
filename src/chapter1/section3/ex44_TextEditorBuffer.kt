package chapter1.section3

import edu.princeton.cs.algs4.Stack
import extensions.readInt
import extensions.readString
import extensions.safeCall

/**
 * 文本编辑器的缓冲区
 * 为文本编辑器的缓冲区设计一种数据类型并实现表1.3.13中的API。
 * 提示：使用两个栈
 */
class Buffer {
    private val firstHalfStack = Stack<Char>()
    private val secondHalfStack = Stack<Char>()

    fun insert(char: Char) {
        firstHalfStack.push(char)
    }

    fun delete(): Char {
        return firstHalfStack.pop()
    }

    fun left(k: Int) {
        repeat(k) {
            secondHalfStack.push(firstHalfStack.pop())
        }
    }

    fun right(k: Int) {
        repeat(k) {
            firstHalfStack.push(secondHalfStack.pop())
        }
    }

    fun size() = firstHalfStack.size() + secondHalfStack.size()

    fun site() = firstHalfStack.size()

    fun output(): Iterator<Char> {
        left(site())
        return secondHalfStack.iterator()
    }
}

fun main() {
    println("Please input command:")
    println("0: exit, 1: insert, 2: delete, 3: left, 4: right, 5: size, 6: site, 7: output")
    val buffer = Buffer()
    while (true) {
        safeCall {
            val command = readInt("command: ")
            when (command) {
                0 -> return
                1 -> {
                    // 可以一次性读取一个字符串，再将每个字符依次插入缓冲区中
                    val s = readString("insert value: ")
                    s.forEach {
                        buffer.insert(it)
                    }
                }
                2 -> {
                    buffer.delete()
                    println("has delete")
                }
                3 -> {
                    buffer.left(readInt("left value: "))
                }
                4 -> {
                    buffer.right(readInt("right value: "))
                }
                5 -> {
                    println("buffer.size = ${buffer.size()}")
                }
                6 -> {
                    println("buffer.site = ${buffer.site()}")
                }
                7 -> {
                    print("buffer output: ")
                    buffer.output().forEach {
                        print(it)
                        print(" ")
                    }
                    println()
                }
                else -> {
                }
            }
        }
    }
}