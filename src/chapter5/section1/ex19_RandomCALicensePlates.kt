package chapter5.section1

import extensions.random

/**
 * 随机的加利福尼亚州车牌号
 * 编写一个静态方法randomPlatesCA，接受一个整型参数N并返回一个含有N个字符串的数组，
 * 每个字符串都是与本节的示例类似的加利福尼亚州的车牌号。
 */
fun ex19_RandomCALicensePlates(N: Int): Array<String> {
    return Array(N) {
        val stringBuilder = StringBuilder()
        stringBuilder.append(random(10))
        repeat(3) {
            // 生成随机大写字母
            stringBuilder.append(random('A'.toInt(), 'Z'.toInt() + 1).toChar())
        }
        repeat(3) {
            stringBuilder.append(random(10))
        }
        stringBuilder.toString()
    }
}

fun main() {
    val N = 20
    val array = ex19_RandomCALicensePlates(N)
    println(array.joinToString(separator = "\n"))
}