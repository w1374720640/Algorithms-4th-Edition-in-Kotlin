package chapter3.section1

/**
 * 一种简单的泛型符号表API
 * 使用K: Any保证K不为null，V同样不能为null
 */
interface ST<K: Any, V: Any> {
    /**
     * 将键值对存入表中（若值为空则将键从表中删除）
     */
    fun put(key: K, value: V)

    /**
     * 获取键key对应的值（若键key不存在则返回null）
     */
    fun get(key: K): V?

    /**
     * 从表中删去键key（及其对应的值）
     */
    fun delete(key: K)

    /**
     * 键key在表中是否有对应的值
     */
    fun contains(key: K): Boolean

    /**
     * 表是否为空
     */
    fun isEmpty(): Boolean

    /**
     * 表中的键值对数量
     */
    fun size(): Int

    /**
     * 表中的所有键的集合
     */
    fun keys(): Iterable<K>
}