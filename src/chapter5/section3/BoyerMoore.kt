package chapter5.section3

import chapter5.section1.Alphabet

/**
 * Boyer-Moore字符串匹配算法（启发式地处理不匹配的字符）
 */
class BoyerMoore(pat: String, alphabet: Alphabet = Alphabet.EXTENDED_ASCII) : StringSearch(pat, alphabet) {
    private val right = IntArray(alphabet.R()) { -1 }

    init {
        for (j in pat.indices) {
            right[alphabet.toIndex(pat[j])] = j
        }
    }

    override fun search(txt: String): Int {
        val N = txt.length
        val M = pat.length
        var i = 0
        var j = M - 1
        while (i <= N - M && j >= 0) {
            if (txt[i + j] == pat[j]) {
                j--
            } else {
                var k = j - right[alphabet.toIndex(pat[j])]
                if (k <= 0) {
                    k = 1
                }
                i += k
                j = M - 1
            }
        }
        return if (j < 0) i else N
    }
}

fun main() {
    testStringSearch { BoyerMoore(it) }
}