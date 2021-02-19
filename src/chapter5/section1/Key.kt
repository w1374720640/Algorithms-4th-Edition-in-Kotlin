package chapter5.section1

/**
 * 键索引计数法中的键
 */
interface Key : Comparable<Key> {

    /**
     * 键的取值范围为[1,R-1]
     */
    fun key(): Int

    override fun compareTo(other: Key): Int {
        return key().compareTo(other.key())
    }
}