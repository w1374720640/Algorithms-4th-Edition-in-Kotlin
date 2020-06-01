package capter1.exercise1_1

import extensions.formatDouble
import extensions.inputPrompt
import extensions.readInt

//使用ex36_b函数将大小为M的数组打乱N次，每次打乱前将数组初始化为a[i]=i，
//打印M*M的表格，i行j列表示i在打乱后落到j位置的次数
fun ex36c(M: Int, N: Int): Array<Array<Int>> {
    val result = Array(M) { Array(M) { 0 } }
    val array = IntArray(M)
    repeat(N) {
        for (i in array.indices) {
            array[i] = i
        }
        ex36b(array).forEachIndexed { index, value ->
            result[value][index]++
        }
    }
    return result
}

fun main() {
    inputPrompt()
    val m = readInt()
    val n = readInt()
    println("N/M=${formatDouble(n.toDouble() / m, 2)}")
    val result = ex36c(m, n)
    result.forEach {
        println(it.joinToString())
    }
}