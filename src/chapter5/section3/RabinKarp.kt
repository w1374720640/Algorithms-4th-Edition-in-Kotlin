package chapter5.section3

import chapter5.section1.Alphabet
import java.math.BigInteger
import java.util.*

/**
 * Rabin-Karp指纹字符串查找算法
 */
open class RabinKarp(pat: String, alphabet: Alphabet = Alphabet.EXTENDED_ASCII) : StringSearch(pat, alphabet) {
    val Q = longRandomPrime()
    var RM = 1L
        private set

    init {
        repeat(pat.length - 1) {
            RM = (RM * alphabet.R()) % Q
        }
    }

    override fun search(txt: String): Int {
        val M = pat.length
        val N = txt.length
        if (M > N) return N
        val patHash = hash(pat, M)
        var txtHash = hash(txt, M)
        if (patHash == txtHash && check(txt, 0)) return 0
        for (i in 0 until N - M) {
            // 先计算旧hash值排除第一个元素后的hash值
            txtHash = (txtHash + Q - RM * alphabet.toIndex(txt[i]) % Q) % Q
            // 再添加新元素重新计算hash值
            txtHash = (txtHash * alphabet.R() + alphabet.toIndex(txt[i + M])) % Q
            if (txtHash == patHash && check(txt, i + 1)) {
                return i + 1
            }
        }
        return N
    }

    open fun check(txt: String, i: Int): Boolean {
        return true
    }

    /**
     * 计算key[0..M-1]的散列值
     */
    open fun hash(key: String, M: Int): Long {
        require(key.length >= M)
        var hash = 0L
        repeat(M) {
            hash = (hash * alphabet.R() + alphabet.toIndex(key[it])) % Q
        }
        return hash
    }

    /**
     * 练习5.3.33 随机素数
     * 为RabinKarp类（算法5.8）实现longRandomPrime()方法。
     * 提示：随机的n位数字是素数的概率与1/n成正比
     *
     * 解：书上说为了保证蒙特卡洛法的正确性，素数应该大于10²⁰
     * 但是Long的最大值约等于9.2*10¹⁹，不存在大于10²⁰的Long值
     * 官方给出的代码使用BigInteger实现，参考[edu.princeton.cs.algs4.RabinKarp]
     * 生成大素数的原理可以参考：https://bindog.github.io/blog/2014/07/19/how-to-generate-big-primes/
     */
    fun longRandomPrime(): Long {
        return BigInteger.probablePrime(31, Random()).longValueExact()
    }
}

fun main() {
    testStringSearch { RabinKarp(it) }
}