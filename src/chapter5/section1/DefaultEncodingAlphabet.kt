package chapter5.section1

/**
 * 不指定具体字符，只需要指定字符数量，字符使用默认编码
 */
class DefaultEncodingAlphabet(private val R: Int) : Alphabet {
    private val alphabet = CharArray(R) { it.toChar() }
    private val reverseAlphabet = IntArray(R) { it }

    constructor() : this(256)

    override fun toChar(index: Int): Char {
        require(index in 0 until R)
        return alphabet[index]
    }

    override fun toIndex(c: Char): Int {
        require(c.toInt() in 0 until R)
        return reverseAlphabet[c.toInt()]
    }

    override fun contains(c: Char): Boolean {
        return c.toInt() < R
    }

    override fun R(): Int {
        return R
    }

    override fun lgR(): Int {
        // logR应该大于等于R的以2为底的log值
        var logR = 0
        var t = R - 1
        while (t != 0) {
            logR++
            // 右移一位，参考练习1.1.14
            t = t shr 1
        }
        return logR
    }

    override fun toIndices(s: String): IntArray {
        return IntArray(s.length) {
            toIndex(s[it])
        }
    }

    override fun toChars(indices: IntArray): String {
        return String(CharArray(indices.size) {
            toChar(indices[it])
        })
    }
}

fun main() {
    val alphabet = DefaultEncodingAlphabet()
    println(alphabet.toChar(65))
    println(alphabet.toIndex('A'))
    println(alphabet.contains('f'))
    println(alphabet.R())
    println(alphabet.lgR())
    println(alphabet.toChars(intArrayOf(65, 66, 67, 68, 48, 49, 50, 51)))
    println(alphabet.toIndices("abcdefg").joinToString())
}