package chapter5.section3

import chapter5.section1.Alphabet
import extensions.randomBoolean

/**
 * 随机文本
 * 编写一个程序，接受整型参数M和N，生成一个长度为N的随机二进制文本字符串，
 * 计算该字符串的最后M位在整个字符串中出现次数。
 * 注意：不同的M值适用的方法可能不同。
 */
fun ex35_RandomText(M: Int, N: Int): Int {
    require(N >= M)
    val txt = String(CharArray(N) { if (randomBoolean()) '0' else '1' })
    val pat = txt.substring(N - M)
    if (M < 10) {
        // 直接用暴力查找
        val bruteForceSearch = BruteForceSearch(pat)
        return bruteForceSearch.count(txt)
    } else {
        // 使用KMP算法，因为是二进制文本字符串，使用更小的字符集提高效率
        val kmp = KMP(pat, Alphabet.BINARY)
        return kmp.count(txt)
    }
}

fun main() {
    val M = 10
    var N = 20
    repeat(20) {
        val count = ex35_RandomText(M, N)
        println("M=$M N=$N count=$count")
        N *= 2
    }
}