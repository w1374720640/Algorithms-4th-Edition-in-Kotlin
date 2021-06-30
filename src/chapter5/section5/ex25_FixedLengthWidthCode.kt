package chapter5.section5

import chapter3.section5.LinearProbingHashSET
import chapter5.section1.SpecificAlphabet

/**
 * 定长定宽的编码
 * 实现一个使用定长编码的RLE类来压缩不同字符较少的ASCII字节流，将编码输出为比特流的一部分。
 * 在compress()方法用一个alpha字符串保存输入中所有不同的字母，用它得到一个Alphabet对象以供compress()方法使用。
 * 将alpha字符串（8位编码再加上它的长度）添加到压缩后的比特流的开头。
 * 修改expand()方法，在展开之前先读取它的字母表。
 *
 * 解：类似于[AlphabetCompression]，区别在于AlphabetCompression在构造函数中接收一个预定义的字母表，
 * RLE在压缩时先遍历一遍字节流，构造出字母表，再将字母表写入输出流中，最后用字母表压缩字节流，解压时先构造字母表再解压
 * 压缩后的比特流格式：字符集大小+压缩后每个字符的比特数+每个字符的原始编码+输入字节流的字符数量+输入流中每个字符压缩后的编码+对齐字节
 */
class RLE : Compression {
    override fun compress(stdIn: BinaryStdIn, stdOut: BinaryStdOut) {
        val s = stdIn.readString()
        if (s.isEmpty()) {
            stdIn.close()
            stdOut.close()
            return
        }
        val set = LinearProbingHashSET<Char>()
        s.forEach {
            set.add(it)
        }
        val array = CharArray(set.size())
        val iterator = set.iterator()
        var i = 0
        while (iterator.hasNext()) {
            array[i++] = iterator.next()
        }
        check(i == array.size)

        // 使用字符串构造字母表时必须确保字符串中的所有字符不重复
        val alphabet = SpecificAlphabet(String(array))
        val bit = alphabet.lgR()
        // 字符集的大小不可能超过256，所以只需要写入低8位，bit同理
        stdOut.write(alphabet.R(), 8)
        stdOut.write(bit, 8)
        array.forEach {
            stdOut.write(it)
        }
        stdOut.write(s.length)
        s.forEach {
            stdOut.write(alphabet.toIndex(it), bit)
        }
        stdIn.close()
        stdOut.close()
    }

    override fun expand(stdIn: BinaryStdIn, stdOut: BinaryStdOut) {
        if (stdIn.isEmpty()) {
            stdIn.close()
            stdOut.close()
            return
        }
        val R = stdIn.readInt(8)
        val bit = stdIn.readInt(8)
        val array = CharArray(R)
        for (i in array.indices) {
            array[i] = stdIn.readChar()
        }
        val length = stdIn.readInt()
        repeat(length) {
            stdOut.write(array[stdIn.readInt(bit)])
        }
        stdIn.close()
        stdOut.close()
    }
}

fun main() {
    val path = "./data/tinyTale.txt"
    val compressPath = "./out/output/tinyTale_compress.RLE"
    val expandPath = "./out/output/tinyTale_expand_RLE.txt"
    testCompression(path, compressPath, expandPath, 64, 5.0) { RLE() }
}