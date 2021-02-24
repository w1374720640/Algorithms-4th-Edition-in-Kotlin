package chapter1.section3

import extensions.readInt
import extensions.readString
import extensions.safeCall

/**
 * 双向链表实现的双向队列
 */
class DoublyLinkedDeque<T> : Deque<T> {
    private val list = DoublyLinkedList<T>()

    override fun isEmpty(): Boolean = list.isEmpty()

    override fun size(): Int = list.size

    override fun pushLeft(item: T) = list.addHeader(item)

    override fun pushRight(item: T) = list.addTail(item)

    override fun popLeft(): T = list.deleteHeader()

    override fun popRight(): T = list.deleteTail()

    override fun iterator(): Iterator<T> = list.forwardIterator()

    fun reverseIterator(): Iterator<T> = list.reverseIterator()
}

/**
 * 基于数组的自动扩容双向队列
 * 初始状态下head和tail都为0，除初始状态外，head和tail位置重叠时表示数组已满，自动扩容，
 * 从头部添加时，向左递增，越界后从最右侧添加，从尾部添加时，向右侧递增，越界后从最左侧添加，
 * 头部添加时索引先自减再添加，尾部添加时先添加索引再自增，弹出时上述操作相反，
 * 索引与2的n次方减1按位与，可以得到实际的索引（小于0或大于size-1时会自动跳转末尾或开头）
 * 实现方式参考 java.util.ArrayDeque
 */
class ResizingArrayDeque<T> : Deque<T> {
    private var head = 0
    private var tail = 0
    private var array = arrayOfNulls<Any>(4)

    override fun isEmpty(): Boolean {
        return head == tail
    }

    override fun size(): Int = when {
        head == tail -> {
            0
        }
        head < tail -> {
            tail - head
        }
        else -> {
            array.size - (head - tail)
        }
    }

    override fun pushLeft(item: T) {
        head = (head - 1) and (array.size - 1)
        array[head] = item
        checkNeedResize()
    }

    override fun pushRight(item: T) {
        array[tail] = item
        tail = (tail + 1) and (array.size - 1)
        checkNeedResize()
    }

    override fun popLeft(): T {
        if (head == tail) throw NoSuchElementException("Deque is empty")
        val value = array[head]
        head = (head + 1) and (array.size - 1)
        return value as T
    }

    override fun popRight(): T {
        if (head == tail) throw NoSuchElementException("Deque is empty")
        tail = (tail - 1) and (array.size - 1)
        return array[tail] as T
    }

    override fun iterator(): Iterator<T> = object : Iterator<T> {
        var index = head

        override fun hasNext(): Boolean = index != tail

        override fun next(): T {
            if (index == tail) throw NoSuchElementException()
            val value = array[index]
            index = (index + 1) and (array.size - 1)
            return value as T
        }
    }

    fun reverseIterator(): Iterator<T> = object : Iterator<T> {
        var index = tail

        override fun hasNext(): Boolean = index != head

        override fun next(): T {
            if (index == head) throw NoSuchElementException()
            index = (index - 1) and (array.size - 1)
            return array[index] as T
        }

    }

    private fun checkNeedResize() {
        if (head == tail) {
            expansion()
        }
    }

    private fun expansion() {
        require(head == tail) { "Array is not full" }
        val newArray = arrayOfNulls<Any>(array.size * 2)
        for (i in 0 until tail) {
            newArray[i] = array[i]
        }
        for (i in head until array.size) {
            newArray[i + array.size] = array[i]
        }
        head += array.size
        array = newArray
    }
}

fun main() {
    val linkedDeque = DoublyLinkedDeque<String>()
    val arrayDeque = ResizingArrayDeque<String>()
    fun iterate() {
        println("linkedDeque = ${linkedDeque.joinToString()}")
        println("arrayDeque = ${arrayDeque.joinToString()}")
    }
    println("Please input command:")
    println("0: exit, 1: push left, 2: push right, 3: pop left, 4: pop right, 5: size, 6: isEmpty")
    while (true) {
        safeCall {
            when (readInt("command: ")) {
                0 -> return
                1 -> {
                    val value = readString("push left value: ")
                    linkedDeque.pushLeft(value)
                    arrayDeque.pushLeft(value)
                    iterate()
                }
                2 -> {
                    val value = readString("push right: ")
                    linkedDeque.pushRight(value)
                    arrayDeque.pushRight(value)
                    iterate()
                }
                3 -> {
                    println("pop left")
                    linkedDeque.popLeft()
                    arrayDeque.popLeft()
                    iterate()
                }
                4 -> {
                    println("pop right")
                    linkedDeque.popRight()
                    arrayDeque.popRight()
                    iterate()
                }
                5 -> {
                    println("linkedDeque.size() = ${linkedDeque.size()}")
                    println("arrayDeque.size() = ${arrayDeque.size()}")
                }
                6 -> {
                    println("linkedDeque.isEmpty() = ${linkedDeque.isEmpty()}")
                    println("arrayDeque.isEmpty() = ${arrayDeque.isEmpty()}")
                }
            }
        }
    }
}