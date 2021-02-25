package chapter5.section1

import extensions.random

/**
 * 随机定长单词
 * 编写一个静态方法randomFixedLengthWords，接受整型参数N和W并返回一个含有N个字符串的数组，
 * 每个字符串都基于英文字母表且长度为W。
 */
fun ex20_RandomFixedLengthWords(N: Int, W: Int): Array<String> {
    // 英文字母表选择小写英文字母
    val alphabet = Alphabet.LOWERCASE
    // 根据给定字母表随机生成字符
    val randomAlphabet = {
        alphabet.toChar(random(alphabet.R()))
    }
    val randomString = {
        String(CharArray(W) {
            randomAlphabet()
        })
    }
    return Array(N) { randomString() }
}

fun main() {
    val N = 10
    val W = 8
    val array = ex20_RandomFixedLengthWords(N, W)
    println(array.joinToString(separator = "\n"))
}