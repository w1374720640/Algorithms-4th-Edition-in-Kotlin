package chapter5.section3

import chapter5.section1.Alphabet

/**
 * 为RabinKarp类（算法5.8）的check()方法中添加代码，将它变为使用拉斯维加斯算法的版本
 * （检查给定位置的文本和模式字符串是否匹配）
 *
 * 解：原文中的check方法只有一个参数i，没有txt参数，无法判断给定位置的文本和模式字符串是否匹配
 * 检查从i开始的pat.length个元素是否匹配
 */
class LasVegasRabinKarp(pat: String, alphabet: Alphabet = Alphabet.EXTENDED_ASCII): RabinKarp(pat, alphabet) {
    override fun check(txt: String, i: Int): Boolean {
        var j = i
        var k = 0
        repeat(pat.length) {
            if (pat[k++] != txt[j++]) return false
        }
        return true
    }
}

fun main() {
    testStringSearch { LasVegasRabinKarp(it) }
}