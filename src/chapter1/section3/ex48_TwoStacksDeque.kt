package chapter1.section3

import extensions.readInt
import extensions.readString
import extensions.safeCall

/**
 * 双向队列与栈
 * 用一个双向队列实现两个栈，保证每个栈操作只需要常数次的双向队列操作（请见练习1.3.33）。
 */
class DoublyDequeStack<T> {
    private val deque = DoublyLinkedDeque<T>()
    private var leftSize = 0
    private var rightSize = 0

    fun isLeftEmpty(): Boolean = leftSize == 0

    fun isRightEmpty(): Boolean = rightSize == 0

    fun leftSize(): Int = leftSize

    fun rightSize(): Int = rightSize

    fun pushLeft(item: T) {
        deque.pushLeft(item)
        leftSize++
    }

    fun pushRight(item: T) {
        deque.pushRight(item)
        rightSize++
    }

    fun popLeft(): T {
        require(leftSize > 0) { "Left stack is empty" }
        leftSize--
        return deque.popLeft()
    }

    fun popRight(): T {
        require(rightSize > 0) { "Right stack is empty" }
        rightSize--
        return deque.popRight()
    }

    fun leftIterator(): Iterator<T> = object : Iterator<T> {
        val iterator = deque.iterator()
        var index = 0

        override fun hasNext(): Boolean = index < leftSize

        override fun next(): T {
            if (index >= leftSize) throw NoSuchElementException()
            index++
            return iterator.next()
        }

    }

    fun rightIterator(): Iterator<T> = object : Iterator<T> {
        val iterator = deque.reverseIterator()
        var index = 0

        override fun hasNext(): Boolean = index < rightSize

        override fun next(): T {
            if (index >= rightSize) throw NoSuchElementException()
            index++
            return iterator.next()
        }

    }
}

fun main() {
    val stack = DoublyDequeStack<String>()
    fun iterate() {
        print("left stack = ")
        stack.leftIterator().forEach { print("$it ") }
        println()
        print("right stack = ")
        stack.rightIterator().forEach { print("$it ") }
        println()
    }
    println("Please input command:")
    println("0: exit, 1: push left, 2: push right, 3: pop left, 4: pop right, 5: stack size, 6, is empty")
    while (true) {
        safeCall {
            when (readInt("command: ")) {
                0 -> return
                1 -> {
                    stack.pushLeft(readString("push left value: "))
                    iterate()
                }
                2 -> {
                    stack.pushRight(readString("push right: "))
                    iterate()
                }
                3 -> {
                    println("pop left")
                    stack.popLeft()
                    iterate()
                }
                4 -> {
                    println("pop right")
                    stack.popRight()
                    iterate()
                }
                5 -> {
                    println("left stack size = ${stack.leftSize()}")
                    println("right stack size = ${stack.rightSize()}")
                }
                6 -> {
                    println("left stack isEmpty = ${stack.isLeftEmpty()}")
                    println("right stack isEmpty = ${stack.isRightEmpty()}")
                }
            }
        }
    }
}