package chapter5.section3

import chapter5.section1.SpecificAlphabet

/**
 * 在Knuth-Morris-Pratt算法中，给出模式A B R A C A D A B R A的dfa[][]数组，按照正文中的样式画出DFA
 */
fun main() {
    val pat = "ABRACADABRA"
    val alphabet = SpecificAlphabet("ABCDR")
    val dfa = ex2(pat, alphabet)
    println("  ${pat.toCharArray().joinToString(separator = " ")}")
    for (i in 0 until alphabet.R()) {
        println("${alphabet.toChar(i)} ${dfa[i].joinToString(separator = " ")}")
    }
}