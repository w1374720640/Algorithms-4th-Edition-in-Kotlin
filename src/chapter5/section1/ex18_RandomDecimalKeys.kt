package chapter5.section1

import extensions.formatDouble
import extensions.random

/**
 * 随机小数键
 * 编写一个静态方法randomDecimalKeys，接受整型参数N和W并返回一个含有N个字符串的数组，
 * 每个字符串都是一个含有W位数的小数。
 */
fun ex18_RandomDecimalKeys(N: Int, W: Int): Array<String> {
    require(W > 0)
    // 这里的实现中，random()函数会产生[0, 1)之间的随机数，W为小数点后的位数，
    return Array(N) { formatDouble(random(), W) }
}

fun main() {
    val N = 10
    val W = 4
    val array = ex18_RandomDecimalKeys(N, W)
    println(array.joinToString(separator = "\n"))
}