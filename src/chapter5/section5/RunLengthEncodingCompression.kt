package chapter5.section5

/**
 * 游程编码压缩算法
 */
class RunLengthEncodingCompression : Compression {
    private val maxCount = 255
    private val bit = 8

    override fun compress(stdIn: BinaryStdIn, stdOut: BinaryStdOut) {
        var count = 0
        // 默认第一个值为0
        var nextValue = false
        while (!stdIn.isEmpty()) {
            val b = stdIn.readBoolean()
            if (b == nextValue) {
                if (count == maxCount) {
                    stdOut.write(count, bit)
                    count = 0
                    stdOut.write(0, bit)
                }
            } else {
                stdOut.write(count, bit)
                count = 0
                nextValue = !nextValue
            }
            count++
        }
        stdOut.write(count, bit)
        stdIn.close()
        stdOut.close()
    }

    override fun expand(stdIn: BinaryStdIn, stdOut: BinaryStdOut) {
        var nextValue = false
        // 只有当bit为8的倍数时才能正常判断非空，因为不会填充空白数据
        while (!stdIn.isEmpty()) {
            val count = stdIn.readInt(bit)
            if (count != 0) {
                repeat(count) {
                    stdOut.write(nextValue)
                }
            }
            nextValue = !nextValue
        }
        stdIn.close()
        stdOut.close()
    }
}

fun main() {
    val path = "./data/q64x96.bin"
    val compressPath = "./out/output/q64x96_compress.bin"
    val expandPath = "./out/output/q64x96_expand.bin"

    testCompression(path, compressPath, expandPath, 64, 2.0) { RunLengthEncodingCompression() }
}