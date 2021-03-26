package chapter5.section3

import chapter5.section1.Alphabet
import chapter5.section1.SpecificAlphabet

/**
 * 在Knuth-Morris-Pratt算法中，给出模式A A A A A A A A A的dfa[][]数组，按照正文中的样式画出DFA
 */
fun ex2(pat: String, alphabet: Alphabet): Array<IntArray> {
    require(pat.isNotEmpty())
    val dfa = Array(alphabet.R()) { IntArray(pat.length) }
    dfa[alphabet.toIndex(pat[0])][0] = 1
    var x = 0
    for (j in 1 until pat.length) {
        for (c in 0 until alphabet.R()) {
            dfa[c][j] = dfa[c][x]
        }
        dfa[alphabet.toIndex(pat[j])][j] = j + 1
        x = dfa[alphabet.toIndex(pat[j])][x]
    }
    return dfa
}

fun main() {
    val pat = "AAAAAAAAA"
    val alphabet = SpecificAlphabet("ABC")
    val dfa = ex2(pat, alphabet)
    println("  ${pat.toCharArray().joinToString(separator = " ")}")
    for (i in 0 until alphabet.R()) {
        println("${alphabet.toChar(i)} ${dfa[i].joinToString(separator = " ")}")
    }
}