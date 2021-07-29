package chapter1.section3

import edu.princeton.cs.algs4.Stack
import extensions.readInt
import extensions.readString
import extensions.safeCall
import kotlin.NoSuchElementException

/**
 * 栈与队列
 * 用有限个栈实现一个队列，保证每个队列操作（在最坏情况下）都只需要常数次的栈操作。
 * 警告：非常难。
 *
 * 解：使用两个栈实现队列
 * 平均复杂度为O(2)，但在最坏情况下单次操作复杂度为O(n)
 * 如果需要保证每次操作在最坏情况下都只需要常数次栈操作，需要使用6个栈，可以参考：
 * https://corresponding.github.io/2018/02/06/20180206-stack-become-queue/
 * https://www.cnblogs.com/dacc123/p/10574939.html
 * 但是都没完全理解，代码以后再补充
 */
class StackQueue<T> {
    private val head = Stack<T>()
    private val tail = Stack<T>()

    fun enqueue(item: T) {
        tail.push(item)
    }

    fun dequeue(): T {
        if (isEmpty()) throw NoSuchElementException()
        if (head.isEmpty) {
            while (!tail.isEmpty) {
                head.push(tail.pop())
            }
        }
        return head.pop()
    }

    fun isEmpty(): Boolean = head.isEmpty() && tail.isEmpty()

    fun size(): Int = head.size() + tail.size()

    fun iterator(): Iterator<T> = object : Iterator<T> {
        val head2 = Stack<T>().apply {
            val tailIterator = tail.iterator()
            while (tailIterator.hasNext()) {
                push(tailIterator.next())
            }
        }
        val headIterator = head.iterator()
        val head2Iterator = head2.iterator()

        override fun hasNext(): Boolean {
            return headIterator.hasNext() || head2Iterator.hasNext()
        }

        override fun next(): T = when {
            headIterator.hasNext() -> {
                headIterator.next()
            }
            head2Iterator.hasNext() -> {
                head2Iterator.next()
            }
            else -> {
                throw NoSuchElementException()
            }
        }
    }
}

fun main() {
    println("Please input command:")
    println("0: exit, 1: enqueue, 2: dequeue, 3: queue size, 4: is empty")
    val queue = StackQueue<String>()
    fun iterate() {
        print("queue = ")
        queue.iterator().forEach { print("$it ") }
        println()
    }
    while (true) {
        safeCall {
            when (readInt("command: ")) {
                0 -> return
                1 -> {
                    queue.enqueue(readString("enqueue value: "))
                    iterate()
                }
                2 -> {
                    println("dequeue value = ${queue.dequeue()}")
                    iterate()
                }
                3 -> println("queue size = ${queue.size()}")
                4 -> println("queue is empty = ${queue.isEmpty()}")
            }
        }
    }
}