package chapter5.section1

import chapter3.section4.LinearProbingHashST

/**
 * 指定具体字母的字母表
 */
class SpecificAlphabet(s: String) : Alphabet {
    private val st = LinearProbingHashST<Char, Int>()
    private val reverseST = LinearProbingHashST<Int, Char>()
    private var size = 0

    init {
        for (i in s.indices) {
            // 字母表不能包含重复字符
            check(!st.contains(s[i])) { "The alphabet cannot contain repeated characters '${s[i]}'" }
            st.put(s[i], size)
            reverseST.put(size, s[i])
            size++
        }
        check(st.size() == size && reverseST.size() == size)
    }

    /**
     * 获取字母表中索引位置的字符
     */
    override fun toChar(index: Int): Char {
        require(index < size)
        return reverseST.get(index)!!
    }

    /**
     * 获取c的索引，在0到R-1之间
     */
    override fun toIndex(c: Char): Int {
        require(st.contains(c))
        return st.get(c)!!
    }

    /**
     * c在字母表之中吗
     */
    override fun contains(c: Char): Boolean {
        return st.contains(c)
    }

    /**
     * 基数（字母表中的字符数量）
     */
    override fun R(): Int {
        return size
    }

    /**
     * 表示一个索引所需的比特数
     */
    override fun lgR(): Int {
        // logR应该大于等于R的以2为底的log值
        var logR = 0
        var t = size - 1
        while (t != 0) {
            logR++
            // 右移一位，参考练习1.1.14
            t = t shr 1
        }
        return logR
    }

    /**
     * 将s转换为R进制的整数
     */
    override fun toIndices(s: String): IntArray {
        return IntArray(s.length) {
            toIndex(s[it])
        }
    }

    /**
     * 将R进制的整数转换为基于该字母表的字符串
     */
    override fun toChars(indices: IntArray): String {
        return String(CharArray(indices.size) {
            toChar(indices[it])
        })
    }
}

fun main() {
    val alphabet = SpecificAlphabet("abcdefg")
    println(alphabet.toChar(2))
    println(alphabet.toIndex('d'))
    println(alphabet.contains('f'))
    println(alphabet.R())
    println(alphabet.lgR())
    println(alphabet.toChars(intArrayOf(6, 5, 4, 3, 2, 1, 0)))
    println(alphabet.toIndices("ccccfga").joinToString())
}