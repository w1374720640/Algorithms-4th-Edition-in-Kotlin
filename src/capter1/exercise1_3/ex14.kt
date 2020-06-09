package capter1.exercise1_3

import extensions.inputPrompt
import extensions.readAllStrings

/**
 * 基于数组的自动缩容/扩容的队列
 * 当移除数据时，只改变队列头部的索引位置，使用 (first + size) % array.size 确定下一条数据的位置
 */
class ResizingArrayQueue<T> : Iterable<T> {
    private var array: Array<Any?> = arrayOfNulls(2)
    private var size = 0
    private var first = 0

    fun enqueue(value: T) {
        if (isFull()) {
            expansion()
        }
        val index = (first + size) % array.size
        array[index] = value
        size++
    }

    fun dequeue(): T {
        if (isEmpty()) {
            throw NoSuchElementException("Queue underflow")
        }
        val value = array[first]
        size--
        ++first
        first %= array.size
        if (lessThanHalf()) {
            shrink()
        }
        return value as T
    }

    fun peek(): T {
        if (isEmpty()) {
            throw NoSuchElementException("Queue underflow")
        }
        return array[first] as T
    }

    fun size() = size

    fun isEmpty() = size == 0

    fun realArraySize() = array.size

    private fun isFull() = array.size == size

    private fun lessThanHalf() = size < array.size / 2

    /**
     * 自动扩容
     */
    private fun expansion() {
        if (isEmpty()) {
            array = arrayOfNulls(2)
            first = 0
            size = 0
            return
        }
        val newArray = arrayOfNulls<Any?>(array.size * 2)
        var index = 0
        repeat(size) {
            newArray[index++] = array[(first + it) % array.size]
        }
        array = newArray
        first = 0
    }

    /**
     * 自动缩容
     */
    private fun shrink() {
        if (array.size <= 2) return
        val newArray = arrayOfNulls<Any?>(array.size / 2)
        var index = 0
        repeat(size) {
            newArray[index++] = array[(first + it) % array.size]
        }
        array = newArray
        first = 0
    }

    override fun iterator(): Iterator<T> {
        return ResizingArrayQueueIterator()
    }

    private inner class ResizingArrayQueueIterator : Iterator<T> {
        var index = 0

        override fun hasNext(): Boolean {
            return index < size
        }

        override fun next(): T {
            if (!hasNext()) throw NoSuchElementException()
            return array[(first + index++) % array.size] as T
        }
    }
}

fun main() {
    inputPrompt()
    val queue = ResizingArrayQueue<String>()
    val inputArray = readAllStrings()
    inputArray.forEach {
        queue.enqueue(it)
        println("queue.size=${queue.size()} realArraySize=${queue.realArraySize()} queue=${queue.joinToString()}")
    }
    while (!queue.isEmpty()) {
        queue.dequeue()
        println("queue.size=${queue.size()} realArraySize=${queue.realArraySize()} queue=${queue.joinToString()}")
    }

}

