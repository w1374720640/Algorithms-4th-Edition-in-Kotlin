package chapter2.section4

interface MaxPriorityQueue<T : Comparable<T>> {
    fun insert(value: T)

    fun max(): T

    fun delMax(): T

    fun isEmpty(): Boolean

    fun size(): Int
}