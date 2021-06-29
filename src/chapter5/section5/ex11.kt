package chapter5.section5

import extensions.randomBoolean
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/**
 * 如果所有字符均来自一个只有两个字符的字母表，该字符串的霍夫曼编码将会是什么？
 * 给出这样的一个长度为N的字符串，使得霍夫曼编码得到的结果最长。
 *
 * 解：得到的霍夫曼编码树一个根结点直接连接两个叶子结点，两个字符分别对应0和1，
 * 霍夫曼编码结果前缀（编码树）长度相同（1+1+8+1+8=19），再连接32位的字符数量，再连接字符数量的0或1，最后补全一个字节
 * 只包含两个字符长度为N的字符串，它的霍夫曼编码长度相同，和字符顺序无关
 */
fun main() {
    val N = 40
    val M = 10
    var count = 19 + 32 + N
    if (count % 8 != 0) {
        // 计算字节对齐后的位数
        count = (count / 8 + 1) * 8
    }
    println("count=$count")

    val huffman = Huffman()
    repeat(M) {
        val s = String(CharArray(N) { if (randomBoolean()) 'a' else 'b' })
        println("origin string: $s")

        val inputStream = ByteArrayInputStream(s.toByteArray())
        val outputStream = ByteArrayOutputStream()
        huffman.compress(BinaryStdIn(inputStream), BinaryStdOut(outputStream))

        // 将压缩的输出流转换为打印的输入流
        val dumpInputStream = convertOutputStreamToInputStream(outputStream)
        val dump = BinaryDump(8, 8)
        dump.dump(BinaryStdIn(dumpInputStream))
        println()
    }
}