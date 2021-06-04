package chapter5.section5

/**
 * 打印输入流中的比特数
 */
class SizeDump : Dump {
    override fun dump(stdIn: BinaryStdIn) {
        var size = 0L
        while (!stdIn.isEmpty()) {
            stdIn.readChar()
            size += 8
        }
        println("$size bits")
    }
}