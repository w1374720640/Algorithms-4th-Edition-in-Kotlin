package chapter5.section5

import chapter2.sleep
import chapter5.section2.TST

class LZW : Compression {
    private val R = 256
    private val L = 4096
    private val W = 12

    override fun compress(stdIn: BinaryStdIn, stdOut: BinaryStdOut) {
        var count = R + 1
        val tst = TST<Int>()
        repeat(R) {
            tst.put(it.toChar().toString(), it)
        }
        var s = stdIn.readString()
        while (s.isNotEmpty()) {
            val longestPrefix = tst.longestPrefixOf(s)!!
            stdOut.write(tst.get(longestPrefix)!!, W)
            if (s.length > longestPrefix.length && count < L) {
                tst.put(s.substring(0, longestPrefix.length + 1), count++)
            }
            s = s.substring(longestPrefix.length)
        }
        // 写入结束字符
        stdOut.write(R, W)
        stdIn.close()
        stdOut.close()
    }

    override fun expand(stdIn: BinaryStdIn, stdOut: BinaryStdOut) {
        val st = Array(L) { "" }
        repeat(R) {
            st[it] = it.toChar().toString()
        }
        var code = stdIn.readInt(W)
        var count = R + 1
        while (code != R) {
            val s = st[code]
            stdOut.write(s)
            val nextCode = stdIn.readInt(W)
            if (nextCode == R) break
            if (count < L) {
                // 这里需要判断这次编码和预读取的编码是否相同
                val newString = s + if (count == nextCode) {
                    s[0]
                } else {
                    st[nextCode][0]
                }
                st[count++] = newString
            }
            code = nextCode
        }
        stdIn.close()
        stdOut.close()
    }
}

fun main() {
    val path = "./data/medTale.txt"
    val compressPath = "./out/output/medTale_compress.LZW"
    val expandPath = "./out/output/medTale_expand.txt"
    testCompression(path, compressPath, expandPath, 512, 1.0) { LZW() }

    sleep(3000)
    val qpath = "./data/q64x96.bin"
    val qcompressPath = "./out/output/q64x96_compress.LZW"
    val qexpandPath = "./out/output/q64x96_expand.bin"
    testCompression(qpath, qcompressPath, qexpandPath, 64, 4.0) { LZW() }

    // 比较huffman压缩算法和LZW压缩算法在压缩tale.txt文件时的压缩率
    val talePath = "./data/tale.txt"
    val taleHuffmanCompressPath = "./out/output/tale_compress.huffman"
    val taleLZWCompressPath = "./out/output/tale_compress.LZW"
    Huffman().compress(BinaryStdIn(talePath), BinaryStdOut(taleHuffmanCompressPath))
    print("huffman compress size= ")
    SizeDump().dump(BinaryStdIn(taleHuffmanCompressPath))
    // 由于大量的字符串分割操作，导致压缩速度很慢
    LZW().compress(BinaryStdIn(talePath), BinaryStdOut(taleLZWCompressPath))
    print("LZW compress size=     ")
    SizeDump().dump(BinaryStdIn(taleLZWCompressPath))
}