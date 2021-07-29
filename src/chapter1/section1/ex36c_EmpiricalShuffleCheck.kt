package chapter1.section1

import extensions.formatDouble
import extensions.inputPrompt
import extensions.readInt

/**
 * 使用ex36_b函数将大小为M的数组打乱N次，每次打乱前将数组初始化为a[i]=i，
 * 打印M*M的表格，i行j列表示i在打乱后落到j位置的次数
 */
fun ex36c_EmpiricalShuffleCheck(M: Int, N: Int): Array<Array<Int>> {
    val result = Array(M) { Array(M) { 0 } }
    val array = IntArray(M)
    repeat(N) {
        for (i in array.indices) {
            array[i] = i
        }
        ex36b_EmpiricalShuffleCheck(array).forEachIndexed { index, value ->
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
    val result = ex36c_EmpiricalShuffleCheck(M, N)
    result.forEach {
        println(it.joinToString())
    }
}