package chapter1.section3

/**
 * 双向队列的API
 */
interface Deque<T> : Iterable<T> {

    fun isEmpty(): Boolean

    fun size(): Int

    fun pushLeft(item: T)

    fun pushRight(item: T)

    fun popLeft(): T

    fun popRight(): T
}