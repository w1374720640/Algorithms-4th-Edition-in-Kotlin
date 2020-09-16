package chapter3.section1

/**
 * 一种有序的泛型符号表API
 */
interface OrderedST<K : Comparable<K>, V: Any> : ST<K, V> {
    /**
     * 最小的键
     */
    fun min(): K

    /**
     * 最大的键
     */
    fun max(): K

    /**
     * 小于等于key的最大的键
     */
    fun floor(key: K): K

    /**
     * 大于等于key的最小的键
     */
    fun ceiling(key: K): K

    /**
     * 小于key的键的数量
     */
    fun rank(key: K): Int

    /**
     * 排名为k的键
     */
    fun select(k: Int): K

    /**
     * 删除最小的键
     */
    fun deleteMin()

    /**
     * 删除最大的键
     */
    fun deleteMax()

    /**
     * [low, high]之间键的数量
     */
    fun size(low: K, high: K): Int

    /**
     * [low, high]之间的所有键，已排序
     */
    fun keys(low: K, high: K): Iterable<K>
}