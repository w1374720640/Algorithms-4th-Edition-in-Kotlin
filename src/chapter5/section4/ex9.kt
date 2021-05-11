package chapter5.section4

/**
 * 用一个正则表达式描述至少含有两个0但不含有任何连续的0的二进制字符串
 */
fun main() {
    val nfa = NFA("(101|1)*0(101|1)+0(101|1)*")
    check(nfa.recognizes("1010"))
    check(nfa.recognizes("0101"))
    check(nfa.recognizes("10101011110"))
    check(nfa.recognizes("01011101011"))
    check(!nfa.recognizes("001111"))
    check(!nfa.recognizes("01010100"))
    check(!nfa.recognizes("10000111"))
    check(!nfa.recognizes("101011100101"))
    println("check succeed.")
}