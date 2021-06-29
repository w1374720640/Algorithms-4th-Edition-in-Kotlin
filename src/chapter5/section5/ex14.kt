package chapter5.section5

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/**
 * 假设需要编码的所有字符的出现概率均不相同。此时的霍夫曼编码树是唯一的吗？
 *
 * 解：是唯一的，因为霍夫曼编码会先统计所有字符出现的频率再向小顶堆中插入数据，和字符出现的顺序无关，
 * 即使合并后的子树和某个字符的概率相同，但由于每次操作的顺序相同，结果也相同
 */
fun main() {
    // 每个字符串中a出现的概率为50%，b出现的概率为30%，c出现的概率为20%，比较不同的排列中，霍夫曼编码的前缀是否相同
    val array = arrayOf(
            "aaaaabbbcc",
            "aaaaaccbbb",
            "bbbaaaaacc",
            "ccaaaaabbb",
            "bbbccaaaaa",
            "ccbbbaaaaa",
            "acbbaabaac"
    )
    val huffman = Huffman()
    array.forEach { s ->
        println("s: $s")
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