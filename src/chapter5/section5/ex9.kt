package chapter5.section5

import chapter5.section1.Alphabet
import extensions.random

/**
 * 估计游程编码、霍夫曼编码和LZW编码处理长度为N的随机ASCII字符串（任意位置都有独立均等的几率出现任意字符）的压缩比。
 */
fun main() {
    val N = 40
    val M = 5
    val alphabet = Alphabet.ASCII
    // 生成M个长度为N的随机ASCII字符串
    val stringList = Array(M) {
        alphabet.toChars(IntArray(N) { random(alphabet.R()) })
    }
    val compressionList = arrayOf<Compression>(RunLengthEncodingCompression(), Huffman(), LZW())
    ex7(stringList, compressionList)
}