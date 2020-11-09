package chapter3.section5

/**
 * 有序集合
 */
interface OrderedSET<K : Comparable<K>> : SET<K> {
    fun min(): K

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

    fun deleteMin()

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