package chapter2.section4

import extensions.readInt
import extensions.safeCall

/**
 * 基于堆的优先队列
 * 保证最大值在堆顶，移除数据时先移除最大值，所以可以获取一组数据中最小的M个值，也可以用于升序排序
 */
class HeapMaxPriorityQueue<T : Comparable<T>>(initialSize: Int) : MaxPriorityQueue<T> {
    private var priorityQueue: Array<T?> = arrayOfNulls<Comparable<T>>(initialSize + 1) as Array<T?>
    private var size = 0

    init {
        require(initialSize >= 0)
    }

    //默认初始大小为4
    constructor() : this(4)

    //练习2.4.19，接受一个数组作为参数的构造函数，使用自底向上的方法构造堆
    constructor(array: Array<T>) : this(array.size + 1) {
        array.forEachIndexed { index, value ->
            priorityQueue[index + 1] = value
        }
        size = array.size
        var k = array.size / 2
        while (k > 0) {
            sink(k)
            k--
        }
    }

    override fun insert(value: T) {
        if (needExpansion()) {
            expansion()
        }
        priorityQueue[++size] = value
        swim(size)
    }

    override fun max(): T {
        if (isEmpty()) {
            throw NoSuchElementException("Priority Queue is empty!")
        }
        return priorityQueue[1]!!
    }

    override fun delMax(): T {
        val max = max()
        swap(1, size)
        priorityQueue[size] = null
        size--
        if (size > 0) {
            sink(1)
        }
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

    /**
     * 使用迭代器返回的顺序和使用delMax()方法返回的顺序不同
     */
    override fun iterator(): Iterator<T> {
        return object : Iterator<T> {
            private var index = 0

            override fun hasNext(): Boolean {
                return size > index
            }

            override fun next(): T {
                return priorityQueue[++index]!!
            }
        }
    }

    override fun toString(): String {
        return priorityQueue.copyOfRange(1, size + 1).joinToString()
    }

    /**
     * 上浮
     */
    private fun swim(k: Int) {
        var i = k
        while (i > 1 && less(i / 2, i)) {
            swap(i / 2, i)
            i /= 2
        }
    }

    /**
     * 下沉
     */
    private fun sink(k: Int) {
        var i = k
        while (2 * i <= size) {
            var j = i * 2
            if (j < size && less(j, j + 1)) {
                j++
            }
            if (less(i, j)) {
                swap(i, j)
                i = j
            } else {
                break
            }
        }
    }

    private fun swap(i: Int, j: Int) {
        if (i == j) return
        val temp = priorityQueue[i]
        priorityQueue[i] = priorityQueue[j]
        priorityQueue[j] = temp
    }

    private fun less(i: Int, j: Int): Boolean {
        if (i == j) return false
        val value1 = priorityQueue[i]
        val value2 = priorityQueue[j]
        if (value1 == null || value2 == null) return false
        return value1 < value2
    }

    private fun needExpansion() = priorityQueue.size - 1 == size

    private fun needShrink() = priorityQueue.size > 5 && priorityQueue.size - 1 >= size * 4

    /**
     * 当数组占满时容量扩大一倍
     */
    private fun expansion() {
        var newSize = size * 2 + 1
        if (newSize < 5) {
            newSize = 5
        }
        val newArray = arrayOfNulls<Comparable<T>>(newSize) as Array<T?>
        repeat(size) {
            newArray[it + 1] = priorityQueue[it + 1]
        }
        priorityQueue = newArray
    }

    /**
     * 当数组使用空间小于四分之一时缩小一倍
     */
    private fun shrink() {
        val newArray = arrayOfNulls<Comparable<T>>((priorityQueue.size - 1) / 2 + 1) as Array<T?>
        repeat(size) {
            newArray[it + 1] = priorityQueue[it + 1]
        }
        priorityQueue = newArray
    }
}

fun main() {
    val priorityQueue = HeapMaxPriorityQueue<Int>()
    println("Please input command:")
    println("0: exit, 1: insert, 2: max, 3: delMax, 4: isEmpty, 5: size, 6: joinToString")
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
                4 -> println("isEmpty=${priorityQueue.isEmpty()}")
                5 -> println("size=${priorityQueue.size()}")
                6 -> println(priorityQueue.toString())
            }
        }
    }
}