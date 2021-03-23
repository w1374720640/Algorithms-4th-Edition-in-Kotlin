package chapter5.section3

import chapter5.section1.Alphabet

/**
 * 子字符串查找算法
 * [pat]表示需要查找的模板字符串，[alphabet]表示字母表
 */
abstract class StringSearch(val pat: String, val alphabet: Alphabet) {

    init {
        require(pat.isNotEmpty())
    }

    /**
     * 在[txt]文本中以[pat]为模板查找子字符串
     * 返回子字符串的第一个字符索引（未找到返回[txt]的长度）
     */
    abstract fun search(txt: String): Int
}