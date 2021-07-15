package chapter1.section1

import extensions.formatStringLength
import extensions.inputPrompt
import extensions.readInt

/**
 * 编写一段代码，将一个正整数N用二进制表示并转换为一个String类型的值s
 */
fun ex9(num: Int): String {
    var s = ""
    repeat(32) {
        // 先左移指定位数，再与1按位与，判断结果是否为0，为0则表示指定位的二进制数为0
        s += (if ((num shr (31 - it) and 1) == 0) "0" else "1")
    }
    return s
}

fun main() {
    inputPrompt()
    val num = readInt()
    println(ex9(num))
    // 将Integer.toBinaryString转换的字符串补齐32位长度
    println(formatStringLength(Integer.toBinaryString(num), 32))
}