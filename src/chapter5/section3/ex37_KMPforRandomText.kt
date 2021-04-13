package chapter5.section3

import chapter5.section1.Alphabet
import extensions.random

/**
 * 随机文本的KMP算法
 * 编写一个用例，接受整型参数M、N和T并运行以下实验T遍：
 * 随机生成一个长度位M的模式字符串和一段长度为N的文本，记录使用KMP算法在文本中查找该模式时比较字符的次数。
 * 修改KMP类的实现来记录比较次数并打印出重复T次之后的平均比较次数。
 */
fun ex37_KMPforRandomText(M: Int, N: Int, T: Int): Int {
    // 使用小写字母作为字符集（使用较小的字符集匹配成功的概率更高）
    val alphabet = Alphabet.LOWERCASE
    var num = 0L
    repeat(T) {
        val txt = String(CharArray(N) { alphabet.toChar(random(alphabet.R())) })
        val pat = String(CharArray(M) { alphabet.toChar(random(alphabet.R())) })
        val kmp = RecordCompareNumKMP(pat, alphabet)
        kmp.search(txt)
        num += kmp.compareNum
    }
    return (num / T).toInt()
}

class RecordCompareNumKMP(pat: String, alphabet: Alphabet = Alphabet.EXTENDED_ASCII) : KMP(pat, alphabet) {
    var compareNum = 0
        private set

    override fun search(txt: String): Int {
        compareNum = 0
        var i = 0
        var j = 0
        while (i < txt.length && j < pat.length) {
            j = dfa[alphabet.toIndex(txt[i++])][j]
            compareNum++
        }
        return if (j == pat.length) i - j else i
    }
}

fun main() {
    val M = 10
    var N = 100
    val T = 10
    repeat(15) {
        val num = ex37_KMPforRandomText(M, N, T)
        println("M=$M N=$N num=$num")
        N *= 2
    }
}