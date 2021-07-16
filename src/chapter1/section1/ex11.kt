package chapter1.section1

import extensions.formatInt
import extensions.formatStringLength
import extensions.randomBoolean

/**
 * 编写一段代码，打印出一个二维布尔数组的内容。其中使用*表示真，空格表示假。打印出行号和列号
 */
fun main() {
    val M = 6 // 行数
    val N = 8 // 列数
    val length = 2 // 每个字符占用的显示长度
    val array = Array(M) { BooleanArray(N) { randomBoolean() } }
    val title = StringBuilder(" ".repeat(length))
    repeat(N) {
        title.append(formatInt(it + 1, length))
    }
    println(title)
    for (i in array.indices) {
        val booleanArray = array[i]
        val content = StringBuilder()
                .append(formatInt(i + 1, length))
        booleanArray.forEach {
            content.append(formatStringLength(if (it) "*" else " ", length))
        }
        println(content)
    }
}