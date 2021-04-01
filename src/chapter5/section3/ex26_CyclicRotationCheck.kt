package chapter5.section3

import java.math.BigInteger
import java.util.*

/**
 * 回环变位
 * 编写一个程序，对于给定的两个字符串，检查它们是否互为对方的回环变位。
 * 例如example和ampleex。
 *
 * 解：练习1.2.6也是求回环变位的问题，那里使用了拼接字符串然后使用系统的indexOf()方法判断，
 * java语言中indexOf()方法实现是暴力查找算法，这里使用RabinKarp算法的思想来进行优化。
 */
fun ex26_CyclicRotationCheck(a: String, b: String): Boolean {
    if (a.length != b.length) return false
    val M = a.length
    val Q = BigInteger.probablePrime(31, Random()).longValueExact()
    val R = 65536 // 字符串默认字符集大小为65536
    var hashA = 0L
    var hashB = 0L
    for (i in 0 until M) {
        hashA = (hashA * R + a[i].toLong()) % Q
        hashB = (hashB * R + b[i].toLong()) % Q
    }
    if (hashA == hashB) return true
    var RM = 1L
    repeat(M - 1) {
        RM = (RM * R) % Q
    }
    for (i in 0 until M - 1) {
        hashB = (hashB + Q - RM * b[i].toLong() % Q) % Q
        hashB = (hashB * R + b[i].toLong()) % Q
        if (hashA == hashB) return true
    }
    return false
}

fun main() {
    check(ex26_CyclicRotationCheck("example", "ampleex"))
    check(ex26_CyclicRotationCheck("ACTGACG", "TGACGAC"))
    check(ex26_CyclicRotationCheck("ABBBBBB", "BBBABBB"))
    check(ex26_CyclicRotationCheck("A", "A"))
    check(ex26_CyclicRotationCheck("AB", "AB"))
    check(ex26_CyclicRotationCheck("AAA", "AAA"))
    check(!ex26_CyclicRotationCheck("AAAAA", "AAA"))
    check(!ex26_CyclicRotationCheck("ABC", "BAC"))
    check(!ex26_CyclicRotationCheck("AAABB", "ABABA"))
    println("check succeed.")
}