package chapter5.section1

import extensions.random

/**
 * 随机的加利福尼亚州车牌号
 * 编写一个静态方法randomPlatesCA，接受一个整型参数N并返回一个含有N个字符串的数组，
 * 每个字符串都是与本节的示例类似的加利福尼亚州的车牌号。
 */
fun ex19_RandomCALicensePlates(N: Int): Array<String> {
    // 生成随机字符型数字的lambda表达式
    val randomNum = { random('0'.toInt(), '9'.toInt() + 1).toChar() }
    // 生成随机字符型大写字母
    val randomAlphabet = { random('A'.toInt(), 'Z'.toInt() + 1).toChar() }
    // 生成随机字符串，长度为7，第一位数字，第二至第四位大写字母，第五至第七位数字
    val randomString = {
        val charArray = CharArray(7)
        charArray[0] = randomNum()
        for (i in 1..3) {
            charArray[i] = randomAlphabet()
        }
        for (i in 4..6) {
            charArray[i] = randomNum()
        }
        String(charArray)
    }
    return Array(N) { randomString() }
}

fun main() {
    val N = 20
    val array = ex19_RandomCALicensePlates(N)
    println(array.joinToString(separator = "\n"))
}