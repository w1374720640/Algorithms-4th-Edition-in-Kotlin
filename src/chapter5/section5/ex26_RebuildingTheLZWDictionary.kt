package chapter5.section5

import chapter5.section2.TST

/**
 * 重建LZW字典
 * 修改LZW算法，当字典饱和时将其清空。这种方式适合某些应用程序，因为它能更好地适应输入中的字符变化。
 */
class RebuildingLZW : Compression {
    private val R = 256
    private val L = 4096
    private val W = 12

    override fun compress(stdIn: BinaryStdIn, stdOut: BinaryStdOut) {
        var count = R + 1
        var tst = TST<Int>()
        repeat(R) {
            tst.put(it.toChar().toString(), it)
        }
        var s = stdIn.readString()
        while (s.isNotEmpty()) {
            val longestPrefix = tst.longestPrefixOf(s)!!
            stdOut.write(tst.get(longestPrefix)!!, W)
            if (s.length > longestPrefix.length) {
                tst.put(s.substring(0, longestPrefix.length + 1), count++)
            }
            // 重建字典
            if (count == L) {
                tst = TST<Int>()
                repeat(R) {
                    tst.put(it.toChar().toString(), it)
                }
                count = R + 1
            }
            s = s.substring(longestPrefix.length)
        }
        stdOut.write(R, W)
        stdIn.close()
        stdOut.close()
    }

    override fun expand(stdIn: BinaryStdIn, stdOut: BinaryStdOut) {
        var st = Array(L) { "" }
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

            // 这里需要判断这次编码和预读取的编码是否相同
            val newString = s + if (count == nextCode) {
                s[0]
            } else {
                st[nextCode][0]
            }
            st[count++] = newString

            // 重建字典
            if (count == L) {
                st = Array(L) { "" }
                repeat(R) {
                    st[it] = it.toChar().toString()
                }
                count = R + 1
            }
            code = nextCode
        }
        stdIn.close()
        stdOut.close()
    }
}

fun main() {
    val path = "./data/medTale.txt"
    val compressPath = "./out/output/medTale_compress.RebuildingLZW"
    val expandPath = "./out/output/medTale_expand.txt"
    testCompression(path, compressPath, expandPath, 512, 1.0) { RebuildingLZW() }

    // 比较LZW压缩算法和RebuildingLZW压缩算法在压缩tale.txt文件时的压缩率
    val talePath = "./data/tale.txt"
    val taleLZWCompressPath = "./out/output/tale_compress.LZW"
    val taleRebuildingLZWCompressPath = "./out/output/tale_compress.RebuildingLZW"
    print("origin size= ")
    SizeDump().dump(BinaryStdIn(talePath))
    LZW().compress(BinaryStdIn(talePath), BinaryStdOut(taleLZWCompressPath))
    print("LZW compress size= ")
    SizeDump().dump(BinaryStdIn(taleLZWCompressPath))
    RebuildingLZW().compress(BinaryStdIn(talePath), BinaryStdOut(taleRebuildingLZWCompressPath))
    print("RebuildingLZW compress size= ")
    SizeDump().dump(BinaryStdIn(taleRebuildingLZWCompressPath))
}