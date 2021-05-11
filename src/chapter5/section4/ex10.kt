package chapter5.section4

/**
 * 用正则表达式描述以下二进制字符串的集合
 * a.至少含有3个字符，且第三个字符为0
 * b.字符串中的0的个数为3的倍数
 * c.起止字符相同
 * d.长度为奇数
 * e.首字母为0且长度为奇数，或者首字母为1且长度为偶数
 * f.长度在1到3之间
 */
fun main() {
    // 至少含有3个字符，且第三个字符为0
    val nfa1 = NFA("[01][01]0[01]*")
    check(nfa1.recognizes("110"))
    check(nfa1.recognizes("10000110101"))
    check(nfa1.recognizes("00011"))
    check(!nfa1.recognizes("00"))
    check(!nfa1.recognizes("11"))
    check(!nfa1.recognizes("11111"))
    check(!nfa1.recognizes("01101"))

    // 字符串中的0的个数为3的倍数
    val nfa2 = NFA("(01*01*0|1)*")
    check(nfa2.recognizes("000000"))
    check(nfa2.recognizes("111111"))
    check(nfa2.recognizes("10101110"))
    check(nfa2.recognizes("0100010011"))
    check(!nfa2.recognizes("100"))
    check(!nfa2.recognizes("100011110"))
    check(!nfa2.recognizes("0000"))

    // 起止字符相同
    val nfa3 = NFA("(0[01]*0|1[01]*1)")
    check(nfa3.recognizes("00"))
    check(nfa3.recognizes("11"))
    check(nfa3.recognizes("01101110"))
    check(nfa3.recognizes("111000101"))
    check(!nfa3.recognizes("000111"))
    check(!nfa3.recognizes("010101"))
    check(!nfa3.recognizes("10000"))
    check(!nfa3.recognizes("1011110"))

    // 长度为奇数
    val nfa4 = NFA("[01]([01][01])*")
    check(nfa4.recognizes("000"))
    check(nfa4.recognizes("11111"))
    check(nfa4.recognizes("01001"))
    check(nfa4.recognizes("11100"))
    check(!nfa4.recognizes("00"))
    check(!nfa4.recognizes("11"))
    check(!nfa4.recognizes("1010"))
    check(!nfa4.recognizes("1111"))
    check(!nfa4.recognizes("1001"))

    // 首字母为0且长度为奇数，或者首字母为1且长度为偶数
    val nfa5 = NFA("(0|(10|11))([01][01])*")
    check(nfa5.recognizes("01111"))
    check(nfa5.recognizes("00000"))
    check(nfa5.recognizes("0110110"))
    check(nfa5.recognizes("1000"))
    check(nfa5.recognizes("101011"))
    check(!nfa5.recognizes("0000"))
    check(!nfa5.recognizes("0111"))
    check(!nfa5.recognizes("000110"))
    check(!nfa5.recognizes("111"))
    check(!nfa5.recognizes("10001"))

    // 长度在1到3之间 NFA的实现不支持指定重复次数，否则可以使用[01]{1-3}
    val nfa6 = NFA("[01][01]?[01]?")
    check(nfa6.recognizes("1"))
    check(nfa6.recognizes("00"))
    check(nfa6.recognizes("101"))
    check(nfa6.recognizes("001"))
    check(!nfa6.recognizes(""))
    check(!nfa6.recognizes("1101"))
    check(!nfa6.recognizes("00000"))
    check(!nfa6.recognizes("001010"))

    println("check succeed.")
}