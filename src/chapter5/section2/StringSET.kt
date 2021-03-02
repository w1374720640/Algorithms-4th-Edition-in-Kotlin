package chapter5.section2

/**
 * 字符串集合的数据类型的API
 */
interface StringSET {

    /**
     * 将key添加到集合中
     */
    fun add(key: String)

    /**
     * 从集合中删除key
     */
    fun delete(key: String)

    /**
     * key是否存在于集合中
     */
    fun contains(key: String): Boolean

    /**
     * 集合是否为空
     */
    fun isEmpty(): Boolean

    /**
     * 集合中的键的数量
     */
    fun size(): Int
}