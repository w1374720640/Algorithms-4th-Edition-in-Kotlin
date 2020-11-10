package chapter3.section5

import chapter3.section1.ST

/**
 * 键可以重复的符号表
 */
interface DuplicateKeysST<K : Any, V : Any> : ST<K, V> {
    fun getAllValues(key: K): Iterable<V>?
}