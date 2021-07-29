package chapter1.section1

import extensions.formatDouble
import extensions.inputPrompt
import extensions.random
import extensions.readInt
import kotlin.math.abs

/**
 * 模拟掷骰子
 * 以下代码能够计算每种两个骰子之和的准确概率分布，dist[i]的值就是两个骰子之和为i的概率。
 * 用实验模拟N次掷骰子，并在计算两个1到6之间的随机整数之和时记录每个值的出现频率以验证它们的概率。
 * N要多大才能够保证你的经验数据和准确数据的吻合程度达到小数点后三位？
 *
 * 解：经测试，当N为一百万时可以保证多次运行的理论值和实际值最大误差小于0.001
 */
fun ex35_DiceSimulation(N: Int) {
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
    ex35_DiceSimulation(readInt("N: "))
}