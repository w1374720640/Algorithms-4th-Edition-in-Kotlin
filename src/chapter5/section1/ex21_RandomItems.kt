package chapter5.section1

import extensions.random

/**
 * 随机元素
 * 写一个静态方法randomItems，接受整型参数N并返回一个含有N个字符串的数组，
 * 每个字符串的长度均在15到30之间且由三部分组成：
 * 第一部分含有4个字符，来自于10个固定的字符串；
 * 第二部分含有10个字符，来自于50个固定的字符串；
 * 第三个部分含有1个字符，来自于2个固定的字符串；
 * 第四部分长15个字节，值为长度在4到15之间且向左对齐的随机字符串。
 */
fun ex21_RandomItems(N: Int): Array<String> {
    val part1 = ex20_RandomFixedLengthWords(10, 4)
    val part2 = ex20_RandomFixedLengthWords(50, 10)
    val part3 = ex20_RandomFixedLengthWords(2, 1)
    val randomString = {
        val stringBuilder = StringBuilder()
                .append(part1[random(part1.size)])
                .append(part2[random(part2.size)])
                .append(part3[random(part3.size)])
        val w = random(4, 16)
        val part4 = ex20_RandomFixedLengthWords(1, w)[0]
        stringBuilder.append(part4)
        stringBuilder.toString()
    }
    return Array(N) { randomString() }
}

fun main() {
    val N = 10
    val array = ex21_RandomItems(N)
    println(array.joinToString(separator = "\n"))
}