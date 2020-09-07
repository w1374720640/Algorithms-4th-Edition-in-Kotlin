package chapter2.section4

/**
 * 最大优先队列
 */
interface MaxPriorityQueue<T : Comparable<T>> : Iterable<T> {
    fun insert(value: T)

    fun max(): T

    fun delMax(): T

    fun isEmpty(): Boolean

    fun size(): Int

    fun indexOf(item: T): Int {
        var i = 0
        val iterator = iterator()
        while (iterator.hasNext()) {
            val value = iterator.next()
            if (value == item) {
                return i
            }
            i++
        }
        return -1
    }

    fun contains(item: T): Boolean {
        return indexOf(item) >= 0
    }
}