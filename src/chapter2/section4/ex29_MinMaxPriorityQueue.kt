package chapter2.section4

import extensions.readInt
import extensions.safeCall

/**
 * 同时面向最大和最小元素的优先队列
 * 设计一个数据类型，支持如下操作：
 * 插入元素、删除最大元素、删除最小元素（所需时间均为对数级别）
 * 查找最大元素、查找最小元素（所需时间均为常数级别）
 * 提示：用两个堆
 *
 * 解：创建一个大顶堆和一个小顶堆
 * 创建一个新的结点，包含原始数据和在大顶堆、小顶堆中的位置，结点存储于堆中
 * 插入元素时，同时插入大顶堆和小顶堆
 * 删除最大元素时，从大顶堆中删除，并找到该结点在小顶堆中的位置，与最后一位交换，并判断是否需要上浮或下沉
 * 删除最小元素时，从小顶堆中删除，其他同上，所需时间为~lgN
 * 查找最大元素时返回大顶堆内容，查找最小元素时返回小顶堆内容，所需时间为常数
 */
class HeapMaxAndMinPriorityQueue<T : Comparable<T>> : MaxPriorityQueue<T>, MinPriorityQueue<T> {
    private val maxPQ = DelByIndexMaxPriorityQueue()
    private val minPQ = DelByIndexMinPriorityQueue()

    private class Node<E : Comparable<E>>(val item: E, var maxPQIndex: Int, var minPQIndex: Int) : Comparable<Node<E>> {
        override fun compareTo(other: Node<E>): Int {
            return item.compareTo(other.item)
        }
    }

    /**
     * 可以根据索引删除结点，通过delMax()方法删除最大值时，会同步从小顶堆中删除对应结点
     */
    private inner class DelByIndexMaxPriorityQueue : HeapMaxPriorityQueue<Node<T>>() {

        override fun delMax(): Node<T> {
            if (isEmpty()) throw NoSuchElementException("Priority Queue is empty!")
            val max: Node<T> = super.delMax()
            minPQ.delIndex(max.minPQIndex)
            return max
        }

        fun delIndex(index: Int) {
            if (index > size) throw NoSuchElementException()
            swap(index, size)
            this[size] = null
            size--
            if (index > size) return
            //交换数据后，要么上浮，要么下沉，要么不变，不可能同时上浮下沉，先判断是否需要上浮可以节省一次判断
            if (this[index]!!.item >= this[index / 2]!!.item) {
                swim(index)
            } else {
                sink(index)
            }
            if (needShrink()) {
                shrink()
            }
        }

        override fun swap(i: Int, j: Int) {
            super.swap(i, j)
            this[i]?.maxPQIndex = i
            this[j]?.maxPQIndex = j
        }
    }

    /**
     * 可以根据索引删除结点，通过delMin()方法删除最大值时，会同步从大顶堆中删除对应结点
     */
    private inner class DelByIndexMinPriorityQueue : HeapMinPriorityQueue<Node<T>>() {

        override fun delMin(): Node<T> {
            if (isEmpty()) throw NoSuchElementException("Priority Queue is empty!")
            val min = super.delMin()
            maxPQ.delIndex(min.maxPQIndex)
            return min
        }

        fun delIndex(index: Int) {
            if (index > size) throw NoSuchElementException()
            swap(index, size)
            this[size] = null
            size--
            if (index > size) return
            if (this[index]!!.item <= this[index / 2]!!.item) {
                swim(index)
            } else {
                sink(index)
            }
            if (needShrink()) {
                shrink()
            }
        }

        override fun swap(i: Int, j: Int) {
            super.swap(i, j)
            this[i]?.minPQIndex = i
            this[j]?.minPQIndex = j
        }
    }


    override fun insert(value: T) {
        val node = Node(value, maxPQ.size() + 1, maxPQ.size() + 1)
        maxPQ.insert(node)
        minPQ.insert(node)
    }

    override fun max(): T {
        return maxPQ.max().item
    }

    override fun delMax(): T {
        return maxPQ.delMax().item
    }

    override fun min(): T {
        return minPQ.min().item
    }

    override fun delMin(): T {
        return minPQ.delMin().item
    }

    override fun isEmpty(): Boolean {
        return maxPQ.isEmpty()
    }

    override fun size(): Int {
        return maxPQ.size()
    }

    override fun iterator(): Iterator<T> {
        return object : Iterator<T> {
            val maxPQIterator = maxPQ.iterator()

            override fun hasNext(): Boolean {
                return maxPQIterator.hasNext()
            }

            override fun next(): T {
                return maxPQIterator.next().item
            }
        }
    }

    /**
     * MaxPriorityQueue和MinPriorityQueue都有indexOf和contains方法，必须要指定调用哪个父类的方法才行
     * 两个接口中的默认实现相同，都是通过迭代器实现的，调用哪个都行
     */
    override fun indexOf(item: T): Int {
        return super<MaxPriorityQueue>.indexOf(item)
    }

    override fun contains(item: T): Boolean {
        return super<MaxPriorityQueue>.contains(item)
    }
}

fun main() {
    val priorityQueue = HeapMaxAndMinPriorityQueue<Int>()
    println("Please input command:")
    println("0: exit, 1: insert, 2: max, 3: delMax, 4: min, 5: delMin, 6: isEmpty, 7: size, 8: joinToString")
    while (true) {
        safeCall {
            when (readInt("command: ")) {
                0 -> return
                1 -> {
                    val value = readInt("insert value: ")
                    priorityQueue.insert(value)
                }
                2 -> println(priorityQueue.max())
                3 -> println(priorityQueue.delMax())
                4 -> println(priorityQueue.min())
                5 -> println(priorityQueue.delMin())
                6 -> println("isEmpty=${priorityQueue.isEmpty()}")
                7 -> println("size=${priorityQueue.size()}")
                8 -> println(priorityQueue.joinToString())
            }
        }
    }
}
