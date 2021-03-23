package chapter5.section3

import chapter5.section1.Alphabet

/**
 * 暴力子字符串查找算法
 */
class BruteForceSearch(pat: String, alphabet: Alphabet = Alphabet.EXTENDED_ASCII) : StringSearch(pat, alphabet) {

    override fun search(txt: String): Int {
        var i = 0
        var j = 0
        while (i < txt.length && j < pat.length) {
            if (txt[i] == pat[j]) {
                i++
                j++
            } else {
                i = i - j + 1
                j = 0
            }
        }
        return if (j == pat.length) i - j else i
    }
}

fun main() {
    testStringSearch { BruteForceSearch(it) }
}