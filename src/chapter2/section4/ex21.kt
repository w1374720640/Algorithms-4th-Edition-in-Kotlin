package chapter2.section4

import extensions.random
import java.lang.StringBuilder

/**
 * 使用大顶堆分别实现第一章中的栈、队列和随机队列这几种数据结构
 *
 * 解：用一个结点包裹真实数据和一个索引，结点的相对大小和索引的相对大小相同
 * 栈：新添加的索引比之前的所有索引都大，每次入栈时新值都添加在堆顶，出栈时从堆顶弹出最大值
 * 队列：新添加的索引比之前所有索引都小，每次入队时新值都添加在末尾，出队时从堆顶弹出最大值
 * 随机队列：每次新添加的索引都是随机生成的值
 */
private class Node<T>(val index: Int, val value: T) : Comparable<Node<T>> {
    override fun compareTo(other: Node<T>): Int {
        return index.compareTo(other.index)
    }

    override fun toString(): String {
        return value.toString()
    }
}

/**
 * 用优先队列实现的栈
 */
class PriorityQueueStack<T> : Iterable<T> {
    private val pq = HeapMaxPriorityQueue<Node<T>>()
    private var index = 0

    fun isEmpty(): Boolean {
        return pq.isEmpty()
    }

    fun size(): Int {
        return pq.size()
    }

    fun push(item: T) {
        pq.insert(Node(index++, item))
    }

    fun pop(): T {
        return pq.delMax().value
    }

    fun peek(): T {
        return pq.max().value
    }

    override fun toString(): String {
        return pq.toString()
    }

    override fun iterator(): Iterator<T> {
        return object : Iterator<T> {
            private val iterator = pq.iterator()

            override fun hasNext(): Boolean {
                return iterator.hasNext()
            }

            override fun next(): T {
                return iterator.next().value
            }
        }
    }
}

/**
 * 使用优先队列实现的先进先出队列
 */
class PriorityQueueNormalQueue<T> : Iterable<T> {
    private val pq = HeapMaxPriorityQueue<Node<T>>()
    private var index = Int.MAX_VALUE

    fun enqueue(item: T) {
        pq.insert(Node(index--, item))
    }

    fun peek(): T {
        return pq.max().value
    }

    fun dequeue(): T {
        return pq.delMax().value
    }

    fun isEmpty(): Boolean {
        return pq.isEmpty()
    }

    fun size(): Int {
        return pq.size()
    }

    override fun iterator(): Iterator<T> {
        return object : Iterator<T> {
            private val iterator = pq.iterator()

            override fun hasNext(): Boolean {
                return iterator.hasNext()
            }

            override fun next(): T {
                return iterator.next().value
            }
        }
    }
}

/**
 * 用优先队列实现的随机队列
 */
class PriorityQueueRandomQueue<T> : Iterable<T> {
    private val pq = HeapMaxPriorityQueue<Node<T>>()

    fun enqueue(item: T) {
        pq.insert(Node(random(Int.MAX_VALUE), item))
    }

    fun peek(): T {
        return pq.max().value
    }

    fun dequeue(): T {
        return pq.delMax().value
    }

    fun isEmpty(): Boolean {
        return pq.isEmpty()
    }

    fun size(): Int {
        return pq.size()
    }

    override fun iterator(): Iterator<T> {
        return object : Iterator<T> {
            private val iterator = pq.iterator()

            override fun hasNext(): Boolean {
                return iterator.hasNext()
            }

            override fun next(): T {
                return iterator.next().value
            }
        }
    }
}

fun main() {
    val array = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
    val stack = PriorityQueueStack<Int>()
    val normalQueue = PriorityQueueNormalQueue<Int>()
    val randomQueue = PriorityQueueRandomQueue<Int>()
    array.forEach {
        stack.push(it)
        normalQueue.enqueue(it)
        randomQueue.enqueue(it)
    }
    val stackStringBuilder = StringBuilder()
    val normalQueueStringBuilder = StringBuilder()
    val randomQueueStringBuilder = StringBuilder()
    while (!stack.isEmpty()) {
        stackStringBuilder.append(stack.pop()).append(" ")
    }
    while (!normalQueue.isEmpty()) {
        normalQueueStringBuilder.append(normalQueue.dequeue()).append(" ")
    }
    while (!randomQueue.isEmpty()) {
        randomQueueStringBuilder.append(randomQueue.dequeue()).append(" ")
    }
    println("stack: $stackStringBuilder")
    println("normalQueue: $normalQueueStringBuilder")
    println("randomQueue: $randomQueueStringBuilder")
}