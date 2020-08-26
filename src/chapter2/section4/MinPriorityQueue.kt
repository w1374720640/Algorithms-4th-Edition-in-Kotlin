package chapter2.section4

/**
 * 最小优先队列
 */
interface MinPriorityQueue<T : Comparable<T>> : Iterable<T> {
    fun insert(value: T)

    fun min(): T

    fun delMin(): T

    fun isEmpty(): Boolean

    fun size(): Int
}