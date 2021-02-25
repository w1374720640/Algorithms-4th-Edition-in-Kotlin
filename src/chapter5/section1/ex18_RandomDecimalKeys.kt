package chapter5.section1

import extensions.random

/**
 * 随机小数键
 * 编写一个静态方法randomDecimalKeys，接受整型参数N和W并返回一个含有N个字符串的数组，
 * 每个字符串都是一个含有W位数的小数。
 *
 * 解：这里的小数是指较小的整数，而不是有小数点的浮点数
 */
fun ex18_RandomDecimalKeys(N: Int, W: Int): Array<String> {
    require(W > 0)
    return Array(N) {
        val stringBuilder = StringBuilder()
        repeat(W) {
            stringBuilder.append(random(10))
        }
        stringBuilder.toString()
    }
}

fun main() {
    val N = 10
    val W = 4
    val array = ex18_RandomDecimalKeys(N, W)
    println(array.joinToString(separator = "\n"))
}