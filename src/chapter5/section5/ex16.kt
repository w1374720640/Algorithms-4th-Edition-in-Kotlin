package chapter5.section5

/**
 * 以下输入经过LZW编码后的结果是什么？
 * a. T O B E O R N O T T O B E
 * b. Y A B B A D A B B A D A B B A D O O
 * c. A A A A A A A A A A A A A A A A A A A A A
 */
fun main() {
    val array = arrayOf(
            "TOBEORNOTTOBE",
            "YABBADABBADABBADOO",
            "AAAAAAAAAAAAAAAAAAAAA"
    )
    val lzw = LZW()
    // 以12比特为一组，一行显示4组
    val dump = BinaryDump(12, 4)
    array.forEach { s ->
        println("s: $s")
        compressAndDumpString(s, lzw, dump)
        println()
    }
}