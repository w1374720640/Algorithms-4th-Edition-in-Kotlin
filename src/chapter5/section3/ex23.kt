package chapter5.section3

import chapter5.section1.Alphabet
import java.math.BigInteger
import java.util.*
import kotlin.collections.ArrayList

/**
 * 编写一个程序，一次读入字符串中的一个字符并立即判断当前字符是否为回文。
 * 提示：使用Rabin-Karp的散列思想。
 *
 * 解：回文是指正读和反读都一样的字符串，例如：ABCBA、ABCCBA
 * 题目要求依次读取字符串的每一个字符，同时立即判断出从起点到当前位置的字符是否构成回文
 * 使用一个ArrayList保存每次读取的Char字符，两个变量保存左半部分和右半部分的hash值，
 * 左右两侧的长度相等，n=N/2，当N为奇数时正中间的字符不参与左右两侧hash值的计算，
 * 左侧hash值的计算公式：leftHash=x[0] + x[1]*R^1 + x[2]*R^2 +...+ x[n-1]*R^(n-1)   （和通常的hash函数不同）
 * 当长度为偶数时，右侧hash值的计算公式：rightHash=x[n]*R^(n-1) + x[n+1]*R^(n-2) +...+ x[N-2]*R + x[N-1]
 * 当长度为奇数时，右侧hash值的计算公式：rightHash=x[n+1]*R^(n-1) + x[n+2]*R^(n-2) +...+ x[N-2]*R + x[N-1]
 * 只有当左右两侧hash值相等时，从起点到当前位置的字符才构成回文
 * 当读取一个新字符时，将字符加入List中，判断List中的元素数量，
 * 如果是奇数，左右长度n不变，leftHash不变，rightHash=(rightHash-x[n]*R^(n-1))*R+x[N-1]
 * 如果是偶数，左右长度n加一，leftHash=leftHash+x[n-1]*R^(n-1)，rightHash=rightHash*R+x[N-1]
 * 一次判断的时间复杂度为O(1)
 */
class Palindrome(private val alphabet: Alphabet = Alphabet.EXTENDED_ASCII) {
    private val R = alphabet.R()
    private val Q = BigInteger.probablePrime(31, Random()).longValueExact()
    private val list = ArrayList<Char>()
    private var leftHash = 0L
    private var rightHash = 0L
    private var rn = 1 // R的n-1次方

    fun checkPalindrome(char: Char): Boolean {
        list.add(char)
        return when (list.size) {
            1 -> true
            2 -> {
                leftHash = alphabet.toIndex(list[0]).toLong()
                rightHash = alphabet.toIndex(char).toLong()
                leftHash == rightHash
            }
            else -> {
                val n = list.size / 2
                if (list.size % 2 == 0) {
                    rn *= R
                    leftHash = (leftHash + alphabet.toIndex(list[n - 1]) * rn) % Q
                    rightHash = (rightHash * R + alphabet.toIndex(char)) % Q
                } else {
                    rightHash = ((rightHash + Q - alphabet.toIndex(list[n]) * rn) * R + alphabet.toIndex(char)) % Q
                }
                leftHash == rightHash
            }
        }
    }
}

private fun test(txt: String, result: BooleanArray) {
    require(txt.length == result.size)
    val palindrome = Palindrome()
    for (i in txt.indices) {
        check(palindrome.checkPalindrome(txt[i]) == result[i]) { "check failed. txt=$txt i=$i" }
    }
}

fun main() {
    test("AAAAA", booleanArrayOf(true, true, true, true, true))
    test("ABCBA", booleanArrayOf(true, false, false, false, true))
    test("ABABABABA", booleanArrayOf(true, false, true, false, true, false, true, false, true))
    test("AAABBAAAA", booleanArrayOf(true, true, true, false, false, false, false, true, false))
    println("check succeed.")
}