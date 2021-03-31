package chapter5.section3

import chapter5.section1.Alphabet
import java.math.BigInteger
import java.util.*
import kotlin.collections.HashSet

/**
 * 如何修改Rabin-Karp算法才能够判定k个模式（假设它们的长度全部相同）中的任意子集出现在文本之中？
 * 解答：计算所有k个模式字符串的散列值并将散列值保存在一个StringSET（请见练习5.2.6）对象中。
 *
 * 解：散列值是Int或Long，不能保存在StringSET中，应该保存到普通SET中
 * 遍历txt时，判断txt的hash值是否包含在SET中
 */
class RabinKarpSearchList(val pats: Array<String>, val alphabet: Alphabet = Alphabet.EXTENDED_ASCII) {
    private val Q = longRandomPrime()
    private var RM = 1L
    private val hashSet = HashSet<Long>()
    private val M: Int

    init {
        require(pats.isNotEmpty())
        M = pats[0].length
        pats.forEach {
            check(it.length == M)
            hashSet.add(hash(it, M))
        }

        repeat(pats[0].length - 1) {
            RM = (RM * alphabet.R()) % Q
        }
    }

    fun search(txt: String): Int {
        val N = txt.length
        if (M > N) return N
        var txtHash = hash(txt, M)
        if (hashSet.contains(txtHash) && check(txt, 0)) return 0
        for (i in 0 until N - M) {
            // 先计算旧hash值排除第一个元素后的hash值
            txtHash = (txtHash + Q - RM * alphabet.toIndex(txt[i]) % Q) % Q
            // 再添加新元素重新计算hash值
            txtHash = (txtHash * alphabet.R() + alphabet.toIndex(txt[i + M])) % Q
            if (hashSet.contains(txtHash) && check(txt, i + 1)) {
                return i + 1
            }
        }
        return N
    }

    private fun check(txt: String, i: Int): Boolean {
        return true
    }

    /**
     * 计算key[0..M-1]的散列值
     */
    private fun hash(key: String, M: Int): Long {
        require(key.length >= M)
        var hash = 0L
        repeat(M) {
            hash = (hash * alphabet.R() + alphabet.toIndex(key[it])) % Q
        }
        return hash
    }

    private fun longRandomPrime(): Long {
        return BigInteger.probablePrime(31, Random()).longValueExact()
    }

}

fun main() {
    val array = arrayOf("ABABAB", "AAAAAA", "BBBBBB")
    val search = RabinKarpSearchList(array)
    check(search.search("ABABABABABAB") == 0)
    check(search.search("AAAAAA") == 0)
    check(search.search("AAAAAAAAAAAA") == 0)
    check(search.search("BBBBBBCD") == 0)
    check(search.search("AAAABABABABBBBBB") == 3)
    check(search.search("AAABBBAAABBB") == 12)
    check(search.search("ABABBBBBBABABAB") == 3)
    check(search.search("") == 0)
    println("check succeed.")
}