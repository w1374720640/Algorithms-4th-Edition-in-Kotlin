package chapter5.section1

/**
 * 字母表的API
 */
interface Alphabet {

    companion object {
        val BINARY = SpecificAlphabet("01")
        val DNA = SpecificAlphabet("ACTG")
        val OCTAL = SpecificAlphabet("01234567")
        val DECIMAL = SpecificAlphabet("0123456789")
        val HEXADECIMAL = SpecificAlphabet("0123456789ABCDEF")
        val PROTEIN = SpecificAlphabet("ACDEFGHIKLMNPQRSTVWY")
        val LOWERCASE = SpecificAlphabet("abcdefghijklmnopqrstuvwxyz")
        val UPPERCASE = SpecificAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
        val BASE64 = SpecificAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/")
        val ASCII = DefaultEncodingAlphabet(128)
        val EXTENDED_ASCII = DefaultEncodingAlphabet(256)
        val UNICODE16 = DefaultEncodingAlphabet(65536)
    }

    /**
     * 获取字母表中索引位置的字符
     */
    fun toChar(index: Int): Char

    /**
     * 获取c的索引，在0到R-1之间
     */
    fun toIndex(c: Char): Int

    /**
     * c在字母表之中吗
     */
    fun contains(c: Char): Boolean

    /**
     * 基数（字母表中的字符数量）
     */
    fun R(): Int

    /**
     * 表示一个索引所需的比特数
     */
    fun lgR(): Int

    /**
     * 将s转换为R进制的整数
     */
    fun toIndices(s: String): IntArray

    /**
     * 将R进制的整数转换为基于该字母表的字符串
     */
    fun toChars(indices: IntArray): String
}