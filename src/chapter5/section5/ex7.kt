package chapter5.section5

import extensions.formatDouble
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/**
 * 给出用游程编码、霍夫曼编码和LZW编码压缩字符串a、aa、aaa、aaaa...（含有N个a的字符串）的结果，以N的函数表示压缩比
 *
 * 解：每个字符占用1字节，将数据压缩后计算压缩比，并打印出压缩后的二进制
 */
fun ex7(stringList: Array<String>, compressionList: Array<Compression>) {
    val dump = BinaryDump(8, 8)
    for (i in compressionList.indices) {
        val compression = compressionList[i]
        println("name: ${compression::class.simpleName}")

        for (j in stringList.indices) {
            val s = stringList[j]
            println("origin string: $s")

            val compressSize = compressAndDumpString(s, compression, dump)
            // 使用压缩后的字节数除以压缩前的字节数就是压缩率，可能大于1，表示没有压缩反而增大了体积
            val ratio = compressSize.toDouble() / s.length
            println("ratio=${formatDouble(ratio, 2)}")
            println()
        }
        println("----------------------------------------------------------------")
        println()
    }
}

/**
 * 使用指定的压缩算法压缩字符串，并打印出压缩结果
 * 返回压缩后的字节数
 */
fun compressAndDumpString(s: String, compression: Compression, dump: Dump): Int {
    val inputStream = ByteArrayInputStream(s.toByteArray())
    val outputStream = ByteArrayOutputStream()
    compression.compress(BinaryStdIn(inputStream), BinaryStdOut(outputStream))

    // 将压缩的输出流转换为打印的输入流
    val dumpInputStream = convertOutputStreamToInputStream(outputStream)
    dump.dump(BinaryStdIn(dumpInputStream))
    return outputStream.size()
}

fun main() {
    val stringList = arrayOf("a", "aa", "aaa", "aaaa", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
    val compressionList = arrayOf<Compression>(RunLengthEncodingCompression(), Huffman(), LZW())
    ex7(stringList, compressionList)
}