package capter1.exercise1_1

import extensions.formatDouble
import extensions.inputPrompt
import extensions.random
import extensions.readInt

//使用kotlin重新实现表1.1.10中的代码
//根据给定概率返回对应的索引
//array[i]的值在0~1之间，总和为1，根据array[i]对应的概率返回i的值
//例如array[2]的值为0.5，则每次调用函数，有50%概率返回2
fun ex36a(array: Array<Double>, times: Int): Array<Double> {
    //根据给定概率返回索引
    fun discrete(array: Array<Double>): Int {
        val random = random()
        var sum = 0.0
        for (i in array.indices) {
            sum += array[i]
            if (sum >= random) return i
        }
        return -1
    }

    fun checkParam(): Boolean {
        var sum = 0.0
        array.forEach {
            if (it < 0.0 || it > 1.0) return false
            sum += it
        }
        return sum == 1.0
    }
    require(checkParam()) { "参数错误，数组内每个值必须在[0,1]中，且总和为1" }
    val result = Array(array.size) { 0.0 }
    repeat(times) {
        result[discrete(array)] += 1.0
    }
    for (i in result.indices) {
        result[i] /= times.toDouble()
    }
    return result
}

fun main() {
    inputPrompt()
    val array = arrayOf(0.1, 0.3, 0.05, 0.1, 0.3, 0.15)
    val times = readInt()
    println("array =${array.joinToString { formatDouble(it, 3) }}")
    val result = ex36a(array, times)
    println("result=${result.joinToString { formatDouble(it, 3) }}")

}