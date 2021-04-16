package chapter5.section3

import chapter5.section1.Alphabet
import extensions.random

/**
 * 随机文本的Boyer-Moore算法
 * 对于Boyer-Moore算法完成上一道练习。
 */
fun ex38_BoyerMooreForRandomText(M: Int, N: Int, T: Int): Int {
    // 使用小写字母作为字符集（使用较小的字符集匹配成功的概率更高）
    val alphabet = Alphabet.LOWERCASE
    var num = 0L
    repeat(T) {
        val txt = String(CharArray(N) { alphabet.toChar(random(alphabet.R())) })
        val pat = String(CharArray(M) { alphabet.toChar(random(alphabet.R())) })
        val boyerMoore = RecordCompareNumBoyerMoore(pat, alphabet)
        boyerMoore.search(txt)
        num += boyerMoore.compareNum
    }
    return (num / T).toInt()
}

class RecordCompareNumBoyerMoore(pat: String, alphabet: Alphabet = Alphabet.EXTENDED_ASCII) : BoyerMoore(pat, alphabet) {
    var compareNum = 0
        private set

    override fun search(txt: String): Int {
        compareNum = 0
        val N = txt.length
        val M = pat.length
        var i = 0
        var j = M - 1
        while (i <= N - M && j >= 0) {
            compareNum++
            if (txt[i + j] == pat[j]) {
                j--
            } else {
                var k = j - right[alphabet.toIndex(pat[j])]
                if (k <= 0) {
                    k = 1
                }
                i += k
                j = M - 1
            }
        }
        return if (j < 0) i else N
    }
}

fun main() {
    testStringSearch { RecordCompareNumBoyerMoore(it) }
    val M = 10
    var N = 100
    val T = 10
    repeat(15) {
        val num = ex38_BoyerMooreForRandomText(M, N, T)
        println("M=$M N=$N num=$num")
        N *= 2
    }
}