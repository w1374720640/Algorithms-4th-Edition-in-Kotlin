package chapter2.section4

/**
 * 找出最小元素
 * 在MaxPQ中加入一个min()方法，你的实现所需的时间和空间都应该是常数
 *
 * 解：MaxPQ只能删除最大元素，所以最小的元素被加入后，除非队列为空，否则不可能被移除
 * 在插入数据时记录最小值，删除最大元素时队列为空将最小值置空
 */
class HeapMaxPriorityQueueWithMin<T : Comparable<T>> : HeapMaxPriorityQueue<T>() {
    protected var min: T? = null

    override fun insert(value: T) {
        if (min == null || value < min!!) {
            min = value
        }
        super.insert(value)
    }

    override fun delMax(): T {
        val max = super.delMax()
        if (max == min && size == 0) {
            min = null
        }
        return max
    }

    fun min(): T {
        if (isEmpty()) {
            throw NoSuchElementException("Priority Queue is empty!")
        }
        return min!!
    }
}

fun main() {
    val priorityQueue = HeapMaxPriorityQueueWithMin<Int>()
    repeat(1000) {
        priorityQueue.insert(it)
        if (priorityQueue.min() != 0) {
            println("check failed!")
            return
        }
    }
    repeat(1000) {
        if (priorityQueue.min() != 0) {
            println("check failed!")
            return
        }
        priorityQueue.delMax()
    }
    println("check succeed!")
}