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