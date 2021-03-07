package chapter5.section2

import chapter3.section5.SET

/**
 * 字符串集合的数据类型的API
 */
interface StringSET : SET<String> {

    /**
     * 如果集合中存在某个以s作为前缀的字符串时返回true
     */
    fun containsPrefix(s: String): Boolean
}