package chapter3.section5

/**
 * 无序集合
 */
interface SET<K : Any> {
    fun add(key: K)

    fun delete(key: K)

    fun contains(key: K): Boolean

    fun isEmpty(): Boolean

    fun size(): Int

    fun keys(): Iterable<K>
}