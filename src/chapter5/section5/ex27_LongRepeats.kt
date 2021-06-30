package chapter5.section5

import chapter5.section1.Alphabet
import extensions.formatDouble
import extensions.random

/**
 * 较长的重复
 * 估计游程编码、霍夫曼编码和LZW编码处理长度为2N的一条字符串的压缩率，
 * 该字符串由长度为N的一条随机ASCII字符串（请见练习5.5.9）重复而成。
 */
fun main() {
    val N = 100
    val alphabet = Alphabet.ASCII
    val s = alphabet.toChars(IntArray(N) { random(alphabet.R()) }).repeat(2)
    println("origin string: $s")
    val compressionList = arrayOf<Compression>(RunLengthEncodingCompression(), Huffman(), LZW())
    val dump = SizeDump()
    for (i in compressionList.indices) {
        val compression = compressionList[i]
        println("name: ${compression::class.simpleName}")
        val compressSize = compressAndDumpString(s, compression, dump)
        // 使用压缩后的字节数除以压缩前的字节数就是压缩率，可能大于1，表示没有压缩反而增大了体积
        val ratio = compressSize.toDouble() / s.length
        println("ratio=${formatDouble(ratio, 2)}")
        println()
    }
}