package chapter2.section4

import chapter1.section3.*
import edu.princeton.cs.algs4.In
import java.lang.StringBuilder

/**
 * 用一下数据结构实现优先队列，支持插入元素和删除最大元素的操作
 * 无序数组、有序数组、无序链表和有序链表
 * 将你的4种实现中的每种操作在最坏情况下的运行时间上下限制成一张表格
 */

/**
 * 基于无序数组的最大优先队列
 */
class UnorderedArrayMaxPriorityQueue<T : Comparable<T>>(val maxSize: Int) : MaxPriorityQueue<T> {
    init {
        require(maxSize > 0)
    }

    private val array: Array<T?> = arrayOfNulls<Comparable<T>>(maxSize) as Array<T?>
    private var size: Int = 0

    override fun insert(value: T) {
        if (size >= maxSize) {
            var max = array[0]!!
            var maxIndex = 0
            for (i in 1 until size) {
                if (array[i]!! > max) {
                    max = array[i]!!
                    maxIndex = i
                }
            }
            if (value < max) {
                array[maxIndex] = value
            }
        } else {
            array[size++] = value
        }
    }

    override fun max(): T {
        if (isEmpty()) throw NoSuchElementException()
        var max: T = array[0]!!
        for (i in 1 until size) {
            if (array[i]!! > max) {
                max = array[i]!!
            }
        }
        return max
    }

    override fun delMax(): T {
        if (isEmpty()) throw NoSuchElementException()
        var max = array[0]!!
        var maxIndex = 0
        for (i in 1 until size) {
            if (array[i]!! > max) {
                max = array[i]!!
                maxIndex = i
            }
        }
        if (maxIndex != size - 1) {
            array[maxIndex] = array[size - 1]
        }
        array[--size] = null
        return max
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun size(): Int {
        return size
    }

}

/**
 * 基于有序数组的最大优先队列
 */
class OrderedArrayMaxPriorityQueue<T : Comparable<T>>(val maxSize: Int) : MaxPriorityQueue<T> {
    init {
        require(maxSize > 0)
    }

    private val array: Array<T?> = arrayOfNulls<Comparable<T>>(maxSize) as Array<T?>
    private var size: Int = 0

    override fun insert(value: T) {
        if (size == maxSize) {
            if (value < max()) {
                array[size - 1] = value
            } else {
                return
            }
        } else {
            array[size++] = value
        }
        for (i in size - 1 downTo 1) {
            if (array[i]!! < array[i - 1]!!) {
                val temp = array[i - 1]
                array[i - 1] = array[i]
                array[i] = temp
            } else {
                break
            }
        }
    }

    override fun max(): T {
        if (isEmpty()) throw NoSuchElementException()
        return array[size - 1]!!
    }

    override fun delMax(): T {
        if (isEmpty()) throw NoSuchElementException()
        val max = max()
        array[--size] = null
        return max
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun size(): Int {
        return size
    }

}

/**
 * 基于无序链表的最大优先队列
 */
class UnorderedLinkedMaxPriorityQueue<T : Comparable<T>>(val maxSize: Int) : MaxPriorityQueue<T> {
    init {
        require(maxSize > 0)
    }

    private val list = DoublyLinkedList<T>()

    override fun insert(value: T) {
        if (list.size() >= maxSize) {
            var maxNode = list.first!!
            var node = list.first
            while (node?.next != null) {
                if (node.next!!.item > maxNode.item) {
                    maxNode = node.next!!
                }
                node = node.next
            }
            if (value < maxNode.item) {
                maxNode.item = value
            } else {
                return
            }
        } else {
            list.addTail(value)
        }
    }

    override fun max(): T {
        if (isEmpty()) throw NoSuchElementException()
        var maxNode = list.first!!
        var node = list.first
        while (node?.next != null) {
            if (node.next!!.item > maxNode.item) {
                maxNode = node.next!!
            }
            node = node.next
        }
        return maxNode.item
    }

    override fun delMax(): T {
        if (isEmpty()) throw NoSuchElementException()
        var maxNode = list.first!!
        var node = list.first
        while (node?.next != null) {
            if (node.next!!.item > maxNode.item) {
                maxNode = node.next!!
            }
            node = node.next
        }
        val maxValue = maxNode.item
        if (maxNode == list.first && maxNode == list.last) {
            list.first = null
            list.last = null
        } else if (maxNode == list.first) {
            maxNode.next?.previous = null
            list.first = maxNode.next
        } else if (maxNode == list.last) {
            maxNode.previous?.next = null
            list.last = maxNode.previous
        } else {
            maxNode.previous?.next = maxNode.next
            maxNode.next?.previous = maxNode.previous
        }
        return maxValue
    }

    override fun isEmpty(): Boolean {
        return list.isEmpty()
    }

    override fun size(): Int {
        return list.size()
    }

}

/**
 * 基于有序链表的最大优先队列
 */
class OrderedLinkedMaxPriorityQueue<T : Comparable<T>>(val maxSize: Int) : MaxPriorityQueue<T> {
    init {
        require(maxSize > 0)
    }

    private val list = DoublyLinkedList<T>()

    override fun insert(value: T) {
        if (list.isEmpty()) {
            list.addTail(value)
        } else if (value <= list.first!!.item) {
            list.addHeader(value)
        } else if (value >= list.last!!.item) {
            list.addTail(value)
        } else {
            var node = list.first
            while (node?.next != null) {
                if (value <= node.next!!.item) {
                    val insertNode = DoubleNode(value, node, node.next)
                    node.next = insertNode
                    insertNode.next?.previous = insertNode
                    break
                }
                node = node.next
            }
        }
        if (list.size() > maxSize) {
            list.deleteTail()
        }
    }

    override fun max(): T {
        if (list.isEmpty()) throw NoSuchElementException()
        return list.last!!.item
    }

    override fun delMax(): T {
        if (list.isEmpty()) throw NoSuchElementException()
        return list.deleteTail()
    }

    override fun isEmpty(): Boolean {
        return list.isEmpty()
    }

    override fun size(): Int {
        return list.size()
    }

}

fun maxPriorityQueueTest(priorityQueue: MaxPriorityQueue<Int>) {
    val input = In("./data/1Kints.txt")
    while (!input.isEmpty) {
        priorityQueue.insert(input.readInt())
    }
    val builder = StringBuilder()
    while (!priorityQueue.isEmpty()) {
        builder.append(priorityQueue.delMax()).append(" ")
    }
    println("priorityQueue=${builder}")
}

fun main() {
    maxPriorityQueueTest(HeapMaxPriorityQueue(10))
    maxPriorityQueueTest(UnorderedArrayMaxPriorityQueue(10))
    maxPriorityQueueTest(OrderedArrayMaxPriorityQueue(10))
    maxPriorityQueueTest(UnorderedLinkedMaxPriorityQueue(10))
    maxPriorityQueueTest(OrderedLinkedMaxPriorityQueue(10))
}