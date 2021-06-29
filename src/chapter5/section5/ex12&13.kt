package chapter5.section5

import chapter5.section1.Alphabet
import extensions.random
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.math.pow

/**
 * 练习5.5.12：假设所有符号出现的概率均为2的负若干次方，描述相应的霍夫曼编码
 * 练习5.5.13：假设所有符号出现的概率均相等，描述相应的霍夫曼编码
 *
 * 解：若所有符号出现的概率均为2的负n次方，则字符集的大小为2^n，每个符号出现的概率相等，
 * 构造的霍夫曼编码树是一个完全二叉树，每个字符压缩后的大小（根结点到叶子结点的路径长度）为n
 * 霍夫曼编码树一共有2^(n+1)-1个结点，2^n个叶子结点
 * 压缩后的霍夫曼编码长度为2^(n+1)-1 + 2^n*8 + 32 + size*n + 补全字节数量
 * 练习5.5.12是练习5.5.13的特例，练习5.5.13生成的树不是完全二叉树，最后一层缺失一部分
 */
fun main() {
    // 以16进制数对应的字符为例，n=4
    val alphabet = Alphabet.HEXADECIMAL
    val n = alphabet.lgR()
    val m = 100
    val p = 10
    var count = 2.0.pow(n + 1).toInt() - 1 + 2.0.pow(n).toInt() * 8 + 32 + m * n
    if (count % 2 != 0) {
        // 对齐字节
        count = (count / 8 + 1) * 8
    }
    println("expect count: $count")

    val huffman = Huffman()
    repeat(p) {
        val s = alphabet.toChars(IntArray(m) { random(alphabet.R()) })
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