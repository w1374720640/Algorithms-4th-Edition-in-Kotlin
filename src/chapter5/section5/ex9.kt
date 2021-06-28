package chapter5.section5

import extensions.random

/**
 * 估计游程编码、霍夫曼编码和LZW编码处理长度为N的随机ASCII字符串（任意位置都有独立均等的几率出现任意字符）的压缩比。
 */
fun main() {
    val N = 40
    val repeatTimes = 5
    val stringList = Array(repeatTimes) {
        String(CharArray(N) { random(128).toChar()})
    }
    val compressionList = arrayOf<Compression>(RunLengthEncodingCompression(), Huffman(), LZW())
    ex7(stringList, compressionList)
}