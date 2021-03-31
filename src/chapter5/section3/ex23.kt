package chapter5.section3

import chapter5.section1.Alphabet
import java.math.BigInteger
import java.util.*

/**
 * 编写一个程序，一次读入字符串中的一个字符并立即判断当前字符是否为回文。
 * 提示：使用Rabin-Karp的散列思想。
 *
 * 解：将字符串分为左右两半，依次计算左侧的hash值和右侧的hash值，
 * 计算左侧的hash值时，原hash值先乘以R再加上字符值，再与大素数Q取余
 * 计算右侧的hash值时，计算右侧第i个元素时，hash += xi * R^(i-1)，再与Q取余
 * 最终如果左右两侧的hash值相等则当前字符是回文
 */
fun ex23(s: String, alphabet: Alphabet = Alphabet.EXTENDED_ASCII): Boolean {
    if (s.length < 2) return true
    val R = alphabet.R()
    val Q = BigInteger.probablePrime(31, Random()).longValueExact()
    val mid = s.length / 2
    var leftHash = 0L
    var rightHash = 0L
    var RM = 1L
    for (i in s.indices) {
        if (i < mid) {
            leftHash = (leftHash * R + alphabet.toIndex(s[i])) % Q
        } else if (i > mid || s.length % 2 == 0) {
            rightHash = (rightHash + alphabet.toIndex(s[i]) * RM) % Q
            RM = (RM * R) % Q
        }
    }
    return leftHash == rightHash
}

fun main() {
    check(ex23(""))
    check(ex23("A"))
    check(ex23("AA"))
    check(ex23("ABA"))
    check(ex23("ABCBA"))
    check(ex23("ABCCBA"))
    check(ex23("AAA"))
    check(ex23("AAAA"))
    check(ex23("ABBBBBBBA"))
    check(ex23("ABABABABA"))
    check(ex23("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"))
    check(ex23("ABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABA"))
    check(ex23("ABCDEFGFEDCBAABCDEFGFEDCBAABCDEFGFEDCBAABCDEFGFEDCBAABCDEFGFEDCBA"))

    check(!ex23("AB"))
    check(!ex23("ABC"))
    check(!ex23("ABCA"))
    check(!ex23("ABBBBAA"))
    check(!ex23("AAAAAB"))
    check(!ex23("ABABABAB"))
    check(!ex23("AAAABBBB"))
    check(!ex23("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB"))
    check(!ex23("ABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABAB"))

    println("check succeed.")
}