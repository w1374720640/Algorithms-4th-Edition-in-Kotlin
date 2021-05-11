package chapter5.section4

/**
 * 用正则表达式描述以下二进制字符串的集合
 * a.含有至少3个连续的1
 * b.含有子字符串110
 * c.含有子字符串1101100
 * d.不含有子字符串110
 *
 * 解：需要注意二进制字符串完全由0、1组成
 */
fun main() {
    // 含有至少3个连续的1
    val nfa1 = NFA("[01]*111[01]*")
    check(nfa1.recognizes("111000"))
    check(nfa1.recognizes("0001111"))
    check(nfa1.recognizes("0101110010"))
    check(!nfa1.recognizes("0101101"))
    check(!nfa1.recognizes("11011"))

    // 含有子字符串110
    val nfa2 = NFA("[01]*110[01]*")
    check(nfa2.recognizes("1101110"))
    check(nfa2.recognizes("1011011"))
    check(nfa2.recognizes("101010110"))
    check(!nfa2.recognizes("10101011"))
    check(!nfa2.recognizes("010101"))
    check(!nfa2.recognizes("00000"))

    // 含有子字符串1101100
    val nfa3 = NFA("[01]*1101100[01]*")
    check(nfa3.recognizes("0101011011001111"))
    check(nfa3.recognizes("11011001110"))
    check(nfa3.recognizes("111111101100"))
    check(!nfa3.recognizes("111010001100"))
    check(!nfa3.recognizes("110110"))

    // 不含有子字符串110
    val nfa4 = NFA("(0|10)*1*")
    check(nfa4.recognizes("01010111"))
    check(nfa4.recognizes("111111"))
    check(nfa4.recognizes("1000100"))
    check(!nfa4.recognizes("010110101"))
    check(!nfa4.recognizes("110"))
    check(!nfa4.recognizes("010101111000"))

    println("check succeed.")
}