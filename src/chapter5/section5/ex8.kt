package chapter5.section5

/**
 * 给出用游程编码、霍夫曼编码和LZW编码压缩字符串ab、abab、ababab、abababab...（含有N个ab的字符串）的结果，以N的函数表示压缩比
 *
 * 解：每个字符占用1字节，将数据压缩后计算压缩比，并打印出压缩后的二进制
 */
fun main() {
    val stringList = arrayOf("ab", "abab", "ababab", "abababab", "abababababababababababababababababababab")
    val compressionList = arrayOf<Compression>(RunLengthEncodingCompression(), Huffman(), LZW())
    ex7(stringList, compressionList)
}