package chapter1.exercise1_1

import extensions.formatDouble
import extensions.inputPrompt
import extensions.random
import extensions.readInt
import kotlin.math.abs

//计算掷骰子时，两个骰子之和理论上的概率分布
//并模拟投掷N次骰子，比较实际概率和理论概率的区别，当N多大时，理论值和实际值误差小于0.001
//经测试，当N为一百万时可以保证多次运行的理论值和实际值最大误差小于0.001
fun ex35(N: Int) {
    val sides = 6
    //理论上的概率分布
    val theoreticalValue = Array(sides * 2 + 1) { 0.0 }
    for (i in 1..sides) {
        for (j in 1..sides) {
            theoreticalValue[i + j] += 1.0
        }
    }
    for (i in 2 until theoreticalValue.size) {
        theoreticalValue[i] /= 36.0
    }
    println("theoreticalValue=${theoreticalValue.joinToString { formatDouble(it, 4) }}")
    println()

    //实际概率
    val actualValue = Array(sides * 2 + 1) { 0.0 }
    repeat(N) {
        val a = random(sides) + 1
        val b = random(sides) + 1
        actualValue[a + b] += 1.0
    }
    for (i in 2 until actualValue.size) {
        actualValue[i] /= N.toDouble()
    }
    println("actualValue     =${actualValue.joinToString { formatDouble(it, 4) }}")
    println()

    //最大误差
    var maximumError = 0.0
    for (i in 2 until theoreticalValue.size) {
        val diff = abs(theoreticalValue[i] - actualValue[i])
        if (diff > maximumError) maximumError = diff
    }
    println("maximumError=${formatDouble(maximumError, 5)}")
}

fun main() {
    inputPrompt()
    ex35(readInt())
}