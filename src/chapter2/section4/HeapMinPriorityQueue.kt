package chapter2.section4

import extensions.readInt
import extensions.safeCall

/**
 * 基于堆的优先队列
 * 保证最小值在堆顶，移除数据时先移除最大值，所以可以获取一组数据中最小的M个值，也可以用于升序排序
 */
open class HeapMinPriorityQueue<T : Comparable<T>>(initialSize: Int) : MinPriorityQueue<T> {
    private var priorityQueue: Array<T?> = arrayOfNulls<Comparable<T>>(initialSize + 1) as Array<T?>
    protected var size = 0

    init {
        require(initialSize >= 0)
    }

    constructor() : this(4)

    override fun insert(value: T) {
        if (needExpansion()) {
            expansion()
        }
        priorityQueue[++size] = value
        swim(size)
    }

    override fun min(): T {
        if (isEmpty()) {
            throw NoSuchElementException("Priority Queue is empty!")
        }
        return priorityQueue[1]!!
    }

    override fun delMin(): T {
        val min = min()
        swap(1, size)
        priorityQueue[size] = null
        size--
        if (size > 0) {
            sink(1)
        }
        if (needShrink()) {
            shrink()
        }
        return min
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

    /**
     * 快速获取指定位置对象
     */
    operator fun get(index: Int): T? {
        return priorityQueue[index]
    }

    /**
     * 仅供子类扩展功能时使用，不直接放开priorityQueue对象权限是为了防止泛型数组类型转换异常
     */
    protected operator fun set(index: Int, value: T?) {
        priorityQueue[index] = value
    }

    /**
     * 上浮
     */
    protected open fun swim(k: Int) {
        var i = k
        while (i > 1 && less(i, i / 2)) {
            swap(i / 2, i)
            i /= 2
        }
    }

    /**
     * 下沉
     */
    protected open fun sink(k: Int) {
        var i = k
        while (2 * i <= size) {
            var j = i * 2
            if (j < size && less(j + 1, j)) {
                j++
            }
            if (less(j, i)) {
                swap(i, j)
                i = j
            } else {
                break
            }
        }
    }

    protected open fun swap(i: Int, j: Int) {
        if (i == j) return
        val temp = priorityQueue[i]
        priorityQueue[i] = priorityQueue[j]
        priorityQueue[j] = temp
    }

    protected open fun less(i: Int, j: Int): Boolean {
        if (i == j) return false
        val value1 = priorityQueue[i]
        val value2 = priorityQueue[j]
        if (value1 == null || value2 == null) return false
        return value1 < value2
    }

    protected open fun needExpansion() = priorityQueue.size - 1 == size

    protected open fun needShrink() = priorityQueue.size > 5 && priorityQueue.size - 1 >= size * 4

    /**
     * 当数组占满时容量扩大一倍
     */
    protected open fun expansion() {
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
    protected open fun shrink() {
        val newArray = arrayOfNulls<Comparable<T>>((priorityQueue.size - 1) / 2 + 1) as Array<T?>
        repeat(size) {
            newArray[it + 1] = priorityQueue[it + 1]
        }
        priorityQueue = newArray
    }
}

fun main() {
    val priorityQueue = HeapMinPriorityQueue<Int>()
    println("Please input command:")
    println("0: exit, 1: insert, 2: min, 3: delMin, 4: isEmpty, 5: size, 6: joinToString")
    while (true) {
        safeCall {
            when (readInt("command: ")) {
                0 -> return
                1 -> {
                    val value = readInt("insert value: ")
                    priorityQueue.insert(value)
                }
                2 -> println(priorityQueue.min())
                3 -> println(priorityQueue.delMin())
                4 -> println("isEmpty=${priorityQueue.isEmpty()}")
                5 -> println("size=${priorityQueue.size()}")
                6 -> println(priorityQueue.joinToString())
            }
        }
    }
}