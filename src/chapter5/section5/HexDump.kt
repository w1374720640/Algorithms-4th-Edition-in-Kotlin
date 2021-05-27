package chapter5.section5

/**
 * 将数据组织成8位字节，并将它打印为各表示4位的两个十六进制数
 */
class HexDump(private val groupCount: Int) : Dump {

    init {
        require(groupCount > 0)
    }

    override fun dump(stdIn: BinaryStdIn) {
        // 将十进制数转换成十六进制时，以十进制为索引从数组中取值
        val array = arrayOf('0','1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
        val stringBuilder = StringBuilder()
        val flag = 0xF
        var count = 0
        var bits = 0
        while (!stdIn.isEmpty()) {
            val i = stdIn.readChar().toInt()
            stringBuilder.append(array[(i ushr 4) and flag])
                    .append(array[i and flag])
            count++
            bits += 8
            if (count == groupCount) {
                stringBuilder.append('\n')
                count = 0
            } else {
                stringBuilder.append(' ')
            }
        }
        if (count != 0) {
            stringBuilder.append('\n')
        }
        print(stringBuilder)
        println("$bits bits")
        stdIn.close()
    }
}

fun main() {
    val dump = HexDump( 4)
    val stdIn = BinaryStdIn("./data/abra.txt")
//    val stdIn = BinaryStdIn()
    dump.dump(stdIn)
}