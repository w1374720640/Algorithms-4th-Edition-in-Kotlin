package chapter2.section4

import chapter1.section3.*
import extensions.random
import extensions.randomBoolean
import extensions.setSeed

/**
 * 用一下数据结构实现优先队列，支持插入元素和删除最大元素的操作
 * 无序数组、有序数组、无序链表和有序链表
 * 将你的4种实现中的每种操作在最坏情况下的运行时间上下限制成一张表格
 */

/**
 * 基于无序数组的最大优先队列
 * 最坏情况（排除扩容、缩容的影响）：
 * insert: 1
 * max: N
 */
class UnorderedArrayMaxPriorityQueue<T : Comparable<T>>(initialSize: Int) : MaxPriorityQueue<T> {

    init {
        require(initialSize >= 0)
    }

    constructor() : this(4)

    private var array: Array<T?> = arrayOfNulls<Comparable<T>>(initialSize) as Array<T?>
    private var size: Int = 0

    override fun insert(value: T) {
        if (needExpansion()) {
            expansion()
        }
        array[size++] = value
    }

    override fun max(): T {
        if (isEmpty()) throw NoSuchElementException()
        var max = array[0]!!
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
        //最大值和最后一位交换，然后将最后一位置空
        if (maxIndex != size - 1) {
            array[maxIndex] = array[size - 1]
        }
        array[--size] = null

        if (needShrink()) {
            shrink()
        }
        return max
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun size(): Int {
        return size
    }

    private fun needExpansion() = array.size == size

    private fun needShrink() = array.size > 4 && array.size >= size * 4

    private fun expansion() {
        var newSize = size * 2
        if (newSize < 4) {
            newSize = 4
        }
        val newArray = arrayOfNulls<Comparable<T>>(newSize) as Array<T?>
        repeat(size) {
            newArray[it] = array[it]
        }
        array = newArray
    }

    private fun shrink() {
        val newArray = arrayOfNulls<Comparable<T>>(array.size / 2) as Array<T?>
        repeat(size) {
            newArray[it] = array[it]
        }
        array = newArray
    }
}

/**
 * 基于有序数组的最大优先队列
 * 最坏情况（排除扩容、缩容的影响）：
 * insert: N
 * max: 1
 */
class OrderedArrayMaxPriorityQueue<T : Comparable<T>>(initialSize: Int) : MaxPriorityQueue<T> {
    init {
        require(initialSize >= 0)
    }

    constructor() : this(4)

    private var array: Array<T?> = arrayOfNulls<Comparable<T>>(initialSize) as Array<T?>
    private var size: Int = 0

    override fun insert(value: T) {
        if (needExpansion()) {
            expansion()
        }
        array[size++] = value
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
        if (needShrink()) {
            shrink()
        }
        return max
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun size(): Int {
        return size
    }

    private fun needExpansion() = array.size == size

    private fun needShrink() = array.size > 4 && array.size >= size * 4

    private fun expansion() {
        var newSize = size * 2
        if (newSize < 4) {
            newSize = 4
        }
        val newArray = arrayOfNulls<Comparable<T>>(newSize) as Array<T?>
        repeat(size) {
            newArray[it] = array[it]
        }
        array = newArray
    }

    private fun shrink() {
        val newArray = arrayOfNulls<Comparable<T>>(array.size / 2) as Array<T?>
        repeat(size) {
            newArray[it] = array[it]
        }
        array = newArray
    }
}

/**
 * 基于无序链表的最大优先队列
 * 最坏情况：
 * insert: 1
 * max: N
 */
class UnorderedLinkedMaxPriorityQueue<T : Comparable<T>> : MaxPriorityQueue<T> {
    private val list = DoublyLinkedList<T>()

    override fun insert(value: T) {
        list.addTail(value)
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
 * 最坏情况：
 * insert: N
 * max: 1
 */
class OrderedLinkedMaxPriorityQueue<T : Comparable<T>> : MaxPriorityQueue<T> {
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

/**
 * 对不同方式实现的优先队列进行测试
 */
fun maxPriorityQueueTest(priorityQueue: MaxPriorityQueue<Int>) {
    //通过设置随机数种子来保证每次方法调用获取的随机数顺序相同
    setSeed(0)
    repeat(1000) {
        if (randomBoolean(0.8)) {
            priorityQueue.insert(random(Int.MAX_VALUE))
        } else {
            if (!priorityQueue.isEmpty()) {
                priorityQueue.delMax()
            }
        }
    }
    val builder = StringBuilder()
    repeat(10) {
        builder.append(priorityQueue.delMax()).append(" ")
    }
    println("size=${priorityQueue.size() + 10} max10=${builder}")
}

fun main() {
    maxPriorityQueueTest(HeapMaxPriorityQueue())
    maxPriorityQueueTest(UnorderedArrayMaxPriorityQueue())
    maxPriorityQueueTest(OrderedArrayMaxPriorityQueue())
    maxPriorityQueueTest(UnorderedLinkedMaxPriorityQueue())
    maxPriorityQueueTest(OrderedLinkedMaxPriorityQueue())
}