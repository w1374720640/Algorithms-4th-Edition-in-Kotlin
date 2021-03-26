package chapter5.section3

import chapter5.section1.Alphabet

/**
 * 开发一个暴力子字符串查找算法的实现BruteForceRL，从右向左匹配模式字符串（算法5.7的简化版本）。
 *
 * 解：参考[BoyerMoore]类，但不使用right[]数组来优化算法，每次不匹配都向右移动一个字符
 */
class BruteForceRL(pat: String, alphabet: Alphabet = Alphabet.EXTENDED_ASCII) : StringSearch(pat, alphabet) {

    override fun search(txt: String): Int {
        val N = txt.length
        val M = pat.length
        var i = 0
        var j = M - 1
        while (i <= N - M && j >= 0) {
            if (txt[i + j] == pat[j]) {
                j--
            } else {
                i++
                j = M - 1
            }
        }
        return if (j < 0) i else N
    }

}

fun main() {
    testStringSearch { BruteForceRL(it) }
}