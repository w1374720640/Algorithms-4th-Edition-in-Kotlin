package chapter1.section1

import extensions.inputPrompt
import extensions.random
import extensions.readInt

/**
 * 随机匹配
 * 编写一个使用BinarySearch的程序，它从命令行接受一个整型参数T，并会分别针对N=10³、10⁴、10⁵和10⁶将以下实验运行T遍：
 * 生成两个大小为N的随机6位正整数数组并找出同时存在于两个数组中的整数的数量。
 * 打印一个表格，对于每个N，给出T次实验中该数量的平均值。
 */
fun ex39_RandomMatches(T: Int) {
    fun setArrayToRandomValue(array: IntArray) {
        for (i in array.indices) {
            array[i] = random(100_0000)
        }
    }

    var N = 10 * 10
    for (i in 1..4) {
        N *= 10
        var total = 0
        repeat(T) {
            val array1 = IntArray(N)
            val array2 = IntArray(N)
            setArrayToRandomValue(array1)
            setArrayToRandomValue(array2)
            array1.sort()
            array2.sort()
            array1.forEach {
                if (binarySearch(it, array2) != -1) total++
            }
        }
        println("N=${N} T=${T} average=${total / T}")
    }
}

fun main() {
    inputPrompt()
    val T = readInt("T: ")
    ex39_RandomMatches(T)
}