package chapter5.section2

import chapter3.section1.ST

/**
 * 以字符串为键的符号表API
 */
interface StringST<V : Any> : ST<String, V> {

    /**
     * s的前缀中最长的键
     */
    fun longestPrefixOf(s: String): String?

    /**
     * 所有以s为前缀的键
     */
    fun keysWithPrefix(s: String): Iterable<String>

    /**
     * 所有和s匹配的键（其中"."能够匹配任意字符）
     */
    fun keysThatMatch(s: String): Iterable<String>
}