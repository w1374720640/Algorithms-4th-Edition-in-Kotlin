package chapter5.section3

import chapter5.section1.Alphabet
import chapter5.section1.SpecificAlphabet

/**
 * 在Knuth-Morris-Pratt算法中，给出模式A A A A A A A A A的dfa[][]数组，按照正文中的样式画出DFA
 */
fun ex2(pat: String, alphabet: Alphabet) {
    val dfa = KMP(pat, alphabet).dfa
    println("  ${pat.toCharArray().joinToString(separator = " ")}")
    for (i in 0 until alphabet.R()) {
        println("${alphabet.toChar(i)} ${dfa[i].joinToString(separator = " ")}")
    }
}

fun main() {
    val pat = "AAAAAAAAA"
    val alphabet = SpecificAlphabet("ABC")
    ex2(pat, alphabet)
}