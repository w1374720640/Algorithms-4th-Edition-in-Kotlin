package chapter1.section3

import edu.princeton.cs.algs4.Stack
import extensions.readInt
import extensions.readString
import extensions.safeCall
import java.security.InvalidParameterException

class Buffer {
    //控制台无法打印Char，所以用一个字符长度的String代替
    private val firstHalfStack = Stack<String>()
    private val secondHalfStack = Stack<String>()

    fun insert(char: String) {
        if (char.length > 1) {
            throw InvalidParameterException("Not a character")
        }
        firstHalfStack.push(char)
    }

    fun delete(): String {
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

    fun output(): Iterator<String> {
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
                    val value = readString("insert value: ")
                    if (value.length > 1) {
                        println("Please enter a character")
                    } else {
                        buffer.insert(value)
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