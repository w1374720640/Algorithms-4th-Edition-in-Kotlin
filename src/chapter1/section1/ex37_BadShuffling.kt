package chapter1.section1

import extensions.formatDouble
import extensions.inputPrompt
import extensions.random
import extensions.readInt

/**
 * 糟糕的打乱
 * 假设在我们的乱序代码中你选择的是一个0到N-1而非i到N-1之间的随机数。
 * 证明得到的结果并非均匀的分布在N!中可能性之间。
 * 用上一题中的测试检验这个版本。
 */
fun ex37_BadShuffling(array: IntArray): IntArray {
    for (i in 0 until array.size - 1) {
        val j = random(array.size)
        val temp = array[i]
        array[i] = array[j]
        array[j] = temp
    }
    return array
}

fun ex37_BadShufflingCheck(M: Int, N: Int): Array<Array<Int>> {
    val result = Array(M) { Array(M) { 0 } }
    val array = IntArray(M)
    repeat(N) {
        for (i in array.indices) {
            array[i] = i
        }
        ex37_BadShuffling(array).forEachIndexed { index, value ->
            result[value][index]++
        }
    }
    return result
}

fun main() {
    inputPrompt()
    val M = readInt("M: ")
    val N = readInt("N: ")
    println("N/M=${formatDouble(N.toDouble() / M, 2)}")
    val result = ex37_BadShufflingCheck(M, N)
    result.forEach {
        println(it.joinToString())
    }
}