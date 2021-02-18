package chapter5.section1

import chapter3.section4.LinearProbingHashST

/**
 * 字母表的API
 */
class Alphabet(s: String) {

    companion object {
        val BINARY = Alphabet("01")
        val DNA = Alphabet("ACTG")
        val OCTAL = Alphabet("01234567")
        val DECIMAL = Alphabet("0123456789")
        val HEXADECIMAL = Alphabet("0123456789ABCDEF")
        val PROTEIN = Alphabet("ACDEFGHIKLMNPQRSTVWY")
        val LOWERCASE = Alphabet("abcdefghijklmnopqrstuvwxyz")
        val UPPERCASE = Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
        val BASE64 = Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/")
    }

    private val st = LinearProbingHashST<Char, Int>()
    private val reverseST = LinearProbingHashST<Int, Char>()
    private var size = 0

    init {
        for (i in s.indices) {
            if (!st.contains(s[i])) {
                st.put(s[i], size)
                reverseST.put(size, s[i])
                size++
            }
        }
        check(st.size() == size && reverseST.size() == size)
    }

    /**
     * 获取字母表中索引位置的字符
     */
    fun toChar(index: Int): Char {
        require(index < size)
        return reverseST.get(index)!!
    }

    /**
     * 获取c的索引，在0到R-1之间
     */
    fun toIndex(c: Char): Int {
        require(st.contains(c))
        return st.get(c)!!
    }

    /**
     * c在字母表之中吗
     */
    fun contains(c: Char): Boolean {
        return st.contains(c)
    }

    /**
     * 基数（字母表中的字符数量）
     */
    fun R(): Int {
        return size
    }

    /**
     * 表示一个索引所需的比特数
     */
    fun lgR(): Int {
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
    fun toIndices(s: String): IntArray {
        val array = IntArray(s.length)
        for (i in s.indices) {
            require(contains(s[i]))
            array[i] = st.get(s[i])!!
        }
        return array
    }

    /**
     * 将R进制的整数转换为基于该字母表的字符串
     */
    fun toChars(indices: IntArray): String {
        val array = CharArray(indices.size)
        for (i in indices.indices) {
            require(reverseST.contains(indices[i]))
            array[i] = reverseST.get(indices[i])!!
        }
        return String(array)
    }
}

fun main() {
    val alphabet = Alphabet("abcdefg")
    println(alphabet.toChar(2))
    println(alphabet.toIndex('d'))
    println(alphabet.contains('f'))
    println(alphabet.R())
    println(alphabet.lgR())
    println(alphabet.toChars(intArrayOf(6, 5, 4, 3, 2, 1, 0)))
    println(alphabet.toIndices("ccccfga").joinToString())
}