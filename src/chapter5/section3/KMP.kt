package chapter5.section3

import chapter5.section1.Alphabet

/**
 * Knuth-Morris-Pratt字符串查找算法
 */
class KMP(pat: String, alphabet: Alphabet = Alphabet.EXTENDED_ASCII) : StringSearch(pat, alphabet) {
    val dfa = Array(alphabet.R()) { IntArray(pat.length) }

    init {
        dfa[alphabet.toIndex(pat[0])][0] = 1
        var x = 0
        for (j in 1 until pat.length) {
            for (c in 0 until alphabet.R()) {
                dfa[c][j] = dfa[c][x]
            }
            dfa[alphabet.toIndex(pat[j])][j] = j + 1
            x = dfa[alphabet.toIndex(pat[j])][x]
        }
    }

    override fun search(txt: String): Int {
        var i = 0
        var j = 0
        while (i < txt.length && j < pat.length) {
            j = dfa[alphabet.toIndex(txt[i++])][j]
        }
        return if (j == pat.length) i - j else i
    }
}

fun main() {
    testStringSearch { KMP(it) }
}