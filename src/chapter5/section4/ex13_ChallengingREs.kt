package chapter5.section4

/**
 * 有难度的正则表达式
 * 使用二值字母表的正则表达式描述以下字符串的集合
 * a.除了11和111的所有字符串
 * b.奇数位数字为1的所有字符串
 * c.至少含有两个0和至多含有一个1的所有字符串
 * d.不存在连续两个1的所有字符串
 *
 * 解：二值字母表就是二进制字符串
 */
fun main() {
    // 除了11和111的所有字符串
    val nfa1 = NFA("(1|(1*01*|1111[01]*)*)")
    check(nfa1.recognizes("0"))
    check(nfa1.recognizes("1"))
    check(nfa1.recognizes("10"))
    check(nfa1.recognizes("100"))
    check(nfa1.recognizes("110"))
    check(nfa1.recognizes("11111"))
    check(nfa1.recognizes("01011"))
    check(nfa1.recognizes("00000"))
    check(!nfa1.recognizes("11"))
    check(!nfa1.recognizes("111"))

    // 奇数位数字为1的所有字符串
    val nfa2 = NFA("(1[01])*1?")
    check(nfa2.recognizes("1"))
    check(nfa2.recognizes("101"))
    check(nfa2.recognizes("111"))
    check(nfa2.recognizes("101110101"))
    check(!nfa2.recognizes("0"))
    check(!nfa2.recognizes("10001"))
    check(!nfa2.recognizes("10101101"))
    check(!nfa2.recognizes("0010"))

    // 至少含有两个0且至多含有一个1的所有字符串
    val nfa3 = NFA("(1?00+|0+1?0+|00+1?)")
    check(nfa3.recognizes("0001"))
    check(nfa3.recognizes("01000"))
    check(nfa3.recognizes("00000"))
    check(nfa3.recognizes("100"))
    check(nfa3.recognizes("00"))
    check(!nfa3.recognizes("0"))
    check(!nfa3.recognizes("01"))
    check(!nfa3.recognizes("011"))
    check(!nfa3.recognizes("10001"))
    check(!nfa3.recognizes("0010111"))

    // 不存在连续两个1的所有字符串
    val nfa4 = NFA("1?(01|0)*")
    check(nfa4.recognizes("01"))
    check(nfa4.recognizes("0000"))
    check(nfa4.recognizes("1010001"))
    check(nfa4.recognizes("0010100"))
    check(!nfa4.recognizes("11"))
    check(!nfa4.recognizes("100110"))
    check(!nfa4.recognizes("0010011"))

    println("check succeed.")
}