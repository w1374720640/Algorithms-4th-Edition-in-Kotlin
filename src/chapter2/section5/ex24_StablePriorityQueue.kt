package chapter2.section5

import chapter2.section4.HeapMaxPriorityQueue
import chapter2.section4.MaxPriorityQueue
import extensions.random
import java.util.concurrent.atomic.AtomicInteger

/**
 * 稳定的优先队列
 * 实现一个稳定的优先队列（将重复的元素按照它们被插入的顺序返回）
 *
 * 解：用一个类包裹原始数据，每个类包含一个自增索引，索引也参与大小对比，这样每个包裹类的大小都是不同的，结果是稳定的
 */
class StableMaxPriorityQueue<T : Comparable<T>> : MaxPriorityQueue<T> {
    class Wrapper<E : Comparable<E>>(val item: E) : Comparable<Wrapper<E>> {
        val index = atomicInteger.incrementAndGet()

        companion object {
            private val atomicInteger = AtomicInteger()
        }

        override fun compareTo(other: Wrapper<E>): Int {
            val itemCompareResult = item.compareTo(other.item)
            if (itemCompareResult != 0) return itemCompareResult
            return index.compareTo(other.index)
        }
    }

    private val pq = HeapMaxPriorityQueue<Wrapper<T>>()

    override fun insert(value: T) {
        pq.insert(Wrapper(value))
    }

    override fun max(): T {
        return pq.max().item
    }

    override fun delMax(): T {
        val max = max()
        pq.delMax()
        return max
    }

    override fun isEmpty(): Boolean {
        return pq.isEmpty()
    }

    override fun size(): Int {
        return pq.size()
    }

    override fun iterator(): Iterator<T> {
        return object : Iterator<T> {
            val wrapperIterator = pq.iterator()

            override fun hasNext(): Boolean {
                return wrapperIterator.hasNext()
            }

            override fun next(): T {
                return wrapperIterator.next().item
            }
        }
    }
}

fun main() {
    // 注意这里不能使用getDoubleArray(10, ArrayInitialState.REPEAT)的方式获取数组
    // Double在作为参数传递的过程中会重新创建新的对象
    val originArray = Array(10) { random(5).toString() }

    //ex17_CheckStability()方法原本用于检查排序函数是否稳定，用优先队列实现一个排序函数
    val result1 = ex17_CheckStability(originArray.copyOf()) { array ->
        val pq = HeapMaxPriorityQueue<String>()
        array.forEach { pq.insert(it) }
        //最大优先队列弹出最大值放到数组末尾
        for (i in array.size - 1 downTo 0) {
            array[i] = pq.delMax()
        }
    }
    println("HeapMaxPriorityQueue: Stability=$result1")

    val result2 = ex17_CheckStability(originArray.copyOf()) { array ->
        val pq = StableMaxPriorityQueue<String>()
        array.forEach { pq.insert(it) }
        for (i in array.size - 1 downTo 0) {
            array[i] = pq.delMax()
        }
    }
    println("StableMaxPriorityQueue: Stability=$result2")
}