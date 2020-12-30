package chapter2.section4

/**
 * 关联索引的最小优先队列
 */
interface IndexMinPriorityQueue<T : Comparable<T>> : Iterable<T> {

    operator fun get(k: Int): T?

    /**
     * 书上的是insert函数，替换为set函数
     */
    operator fun set(k: Int, item: T)

    fun contains(k: Int): Boolean

    fun delete(k: Int)

    fun min(): T

    fun minIndex(): Int

    fun delMin(): Pair<T, Int>

    fun isEmpty(): Boolean

    fun size(): Int
}