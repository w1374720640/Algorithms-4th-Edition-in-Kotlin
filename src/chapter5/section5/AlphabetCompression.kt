package chapter5.section5

import chapter5.section1.Alphabet

/**
 * 使用较小的字符集替代默认的Char字符集来压缩数据
 */
class AlphabetCompression(private val alphabet: Alphabet) : Compression {
    // 字符集每位字符所需的最小比特数
    private val bit = alphabet.lgR()

    override fun compress(stdIn: BinaryStdIn, stdOut: BinaryStdOut) {
        // 因为要先获取总长度，所以需要先读取所有数据到内存中
        val s = stdIn.readString()
        val N = s.length
        // 需要在开头先写入所有的字节数量
        stdOut.write(N)
        s.forEach {
            stdOut.write(alphabet.toIndex(it), bit)
        }
        stdIn.close()
        stdOut.close()
    }

    override fun expand(stdIn: BinaryStdIn, stdOut: BinaryStdOut) {
        val N = stdIn.readInt()
        repeat(N) {
            val char = alphabet.toChar(stdIn.readInt(bit))
            stdOut.write(char)
        }
        stdIn.close()
        stdOut.close()
    }
}

fun main() {
    val path = "./data/genomeVirus.txt"
    val compressPath = "./out/output/genomeVirus_compress.txt"
    val expandPath = "./out/output/genomeVirus_expand.txt"
    testCompression(path, compressPath, expandPath, 512, 1.0) { AlphabetCompression(Alphabet.DNA) }
}