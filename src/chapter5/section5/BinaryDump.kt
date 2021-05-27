package chapter5.section5

/**
 * 将输入流中的比特按照0和1打印出来
 *
 * 两个构造函数，用于确定每行显示的比特数量，可以按组分，如每组8位，每行3组，也可以直接设置每行显示的最大数量（不分组）
 */
class BinaryDump(private val groupSize: Int, private val groupCount: Int) : Dump {
    init {
        require(groupSize > 0 && groupCount > 0)
    }

    constructor(lineCount: Int) : this(lineCount, 1)

    override fun dump(stdIn: BinaryStdIn) {
        val stringBuilder = StringBuilder()
        var size = 0 // 一个分组中已填充的数量
        var count = 0 // 已填满的分组数量
        var bits = 0 // 总比特数
        while (!stdIn.isEmpty()) {
            val b = stdIn.readBoolean()
            stringBuilder.append(if (b) '1' else '0')
            bits++
            size++
            if (size == groupSize) {
                size = 0
                count++
                if (count == groupCount) {
                    stringBuilder.append('\n')
                    count = 0
                } else {
                    stringBuilder.append(' ')
                }
            }
        }
        if (count != 0) {
            // 如果输入流结束时没有填满最后一行，手动换行
            stringBuilder.append('\n')
        }
        print(stringBuilder.toString())
        println("$bits bits")
        stdIn.close()
    }
}

fun main() {
    val dump = BinaryDump(8, 4)
    val stdIn = BinaryStdIn("./data/abra.txt")
//    val stdIn = BinaryStdIn()
    dump.dump(stdIn)
}