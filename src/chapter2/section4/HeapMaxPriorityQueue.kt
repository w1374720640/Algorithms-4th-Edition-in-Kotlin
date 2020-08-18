package chapter2.section4

import extensions.inputPrompt
import extensions.readInt
import extensions.safeCall

/**
 * 基于堆的优先队列
 * 保证最大值在堆顶，移除数据时先移除最大值，所以可以获取一组数据中最小的M个值，也可以用于升序排序
 */
class HeapMaxPriorityQueue<T : Comparable<T>>(private val maxSize: Int) : MaxPriorityQueue<T> {
    init {
        require(maxSize > 0)
    }

    //不能直接创建泛型数组，使用强制类型转换
    private var priorityQueue: Array<T?> = arrayOfNulls<Comparable<T>>(maxSize + 1) as Array<T?>
    private var size = 0

    override fun insert(value: T) {
        //当数组达到最大长度时，先判断是否小于最大值，不小于直接忽略，小于则删除最大值后添加进去
        if (size >= maxSize) {
            if (value < priorityQueue[1]!!) {
                priorityQueue[1] = value
                sink(1)
            }
        } else {
            priorityQueue[++size] = value
            swim(size)
        }
    }

    override fun max(): T {
        if (isEmpty()) {
            throw NoSuchElementException()
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
        return max
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun size(): Int {
        return size
    }

    fun joinToString(): String {
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
}

fun main() {
    inputPrompt()
    val maxSize = readInt("maxSize: ")
    val priorityQueue = HeapMaxPriorityQueue<Int>(maxSize)
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
                6 -> println(priorityQueue.joinToString())
            }
        }
    }
}