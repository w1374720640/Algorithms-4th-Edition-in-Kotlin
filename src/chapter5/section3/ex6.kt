package chapter5.section3

import chapter5.section1.Alphabet
import chapter5.section1.SpecificAlphabet

/**
 * 给出算法5.7的构造函数计算模式A B R A C A D A B R A所得到的right[]数组。
 */
fun ex7(pat: String, alphabet: Alphabet) {
    val right = BoyerMoore(pat, alphabet).right
    for (i in right.indices) {
        println("${alphabet.toChar(i)} ${right[i]}")
    }
}

fun main() {
    val pat = "ABRACADABRA"
    val alphabet = SpecificAlphabet("ABCDR")
    ex7(pat, alphabet)
}