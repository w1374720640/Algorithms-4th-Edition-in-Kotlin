package chapter5.section3

import chapter5.section1.SpecificAlphabet

/**
 * 为以下模式字符串画出KMP算法的DFA
 * a.AAAAAAB
 * b.AACAAAB
 * c.ABABABAB
 * d.ABAABAAABAAAB
 * e.ABAABCABAABCB
 */
fun main() {
    val alphabet = SpecificAlphabet("ABC")
    ex2("AAAAAAB", alphabet)
    println()
    ex2("AACAAAB", alphabet)
    println()
    ex2("ABABABAB", alphabet)
    println()
    ex2("ABAABAAABAAAB", alphabet)
    println()
    ex2("ABAABCABAABCB", alphabet)
}