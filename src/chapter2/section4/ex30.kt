package chapter2.section4

import extensions.readInt
import extensions.safeCall

/**
 * 动态中位数查找
 * 设计一个数据类型，支持在对数时间内插入元素，常数时间内找到中位数并在对数时间内删除中位数
 * 提示：用一个面向最大元素的堆再用一个面向最小元素的堆
 *
 * 解：创建一个大顶堆和一个小顶堆，大顶堆保存较小的一半数据，小顶堆保存较大的一半数据
 * 插入的数据先和大顶堆最大值比较，比最大值大时插入小顶堆，比最大值小时插入大顶堆
 * 保持大顶堆的大小等于小顶堆（总数为偶数）或比小顶堆大一（总数为奇数）
 * 当某个堆的大小超长时，从该堆中移除最大/最小值，加入另一个堆中
 * 大顶堆的最大值和小顶堆的最小值是处于最中间的两个数
 * 插入元素最坏情况需要先插入再移除再插入3lg(N/2) ~lgN
 * 查找中位数时，先判断两个堆的大小再决定是取大顶堆的最大值还是取两个堆顶的平均值
 * 删除中位数最坏情况，先删除中位数，再从另一个堆弹出一个元素，再插入另一个堆3lg(N/2) ~lgN
 */
class MedianHeap<T : Comparable<T>> {
    private val maxPQ = HeapMaxPriorityQueue<T>()
    private val minPQ = HeapMinPriorityQueue<T>()

    fun insert(item: T) {
        if (maxPQ.isEmpty() || maxPQ.max() >= item) {
            maxPQ.insert(item)
        } else {
            minPQ.insert(item)
        }
        adjust()
    }

    /**
     * 因为泛型T只能比较大小不能计算平均值，所以当总数为偶数时仍然取maxPQ中的最大值为中位数
     */
    fun median(): T {
        if (isEmpty()) throw NoSuchElementException("Heap is empty!")
        return maxPQ.max()
    }

    fun delMedian(): T {
        if (isEmpty()) throw NoSuchElementException("Heap is empty!")
        val max = maxPQ.delMax()
        adjust()
        return max
    }

    fun isEmpty(): Boolean {
        return maxPQ.isEmpty() && minPQ.isEmpty()
    }

    fun size(): Int {
        return maxPQ.size() + minPQ.size()
    }

    override fun toString(): String {
        return "minPQ=${minPQ.joinToString()}\nmaxPQ=${maxPQ.joinToString()}"
    }

    private fun adjust() {
        when {
            maxPQ.size() > minPQ.size() + 1 -> {
                minPQ.insert(maxPQ.delMax())
            }
            maxPQ.size() < minPQ.size() -> {
                maxPQ.insert(minPQ.delMin())
            }
            else -> {}
        }
    }
}

fun main() {
    val heap = MedianHeap<Int>()
    println("Please input command:")
    println("0: exit, 1: insert, 2: median, 3: delMedian, 4: isEmpty, 5: size, 6: toString")
    while (true) {
        safeCall {
            when (readInt("command: ")) {
                0 -> return
                1 -> {
                    val value = readInt("insert value: ")
                    heap.insert(value)
                }
                2 -> println(heap.median())
                3 -> println(heap.delMedian())
                4 -> println("isEmpty=${heap.isEmpty()}")
                5 -> println("size=${heap.size()}")
                6 -> println(heap.toString())
            }
        }
    }
}