package chapter1.section2

import edu.princeton.cs.algs4.Accumulator
import extensions.inputPrompt
import extensions.readAllDoubles
import kotlin.math.sqrt

/**
 * 计算样本的方差、标准差（不记录每条数据）
 * 标准差计算公式简化过程参考  https://zh.wikipedia.org/wiki/%E6%A8%99%E6%BA%96%E5%B7%AE
 * 书中的简化公式没看懂，参考edu.princeton.cs.algs4.Accumulator类
 */
class CustomAccumulator {
    var count = 0
    //平均值
    var mean = 0.0
    //所有样本的平方和
    var sumOfSquare = 0.0

    //添加数据
    fun add(value: Double) {
        count++
        mean += (value - mean) / count
        sumOfSquare += value * value
    }


    /**
     * 获取样本方差
     * 如果是获取总体方差，需要把除数count-1改为count
     */
    fun getVariance(): Double {
        return (sumOfSquare - count * mean * mean) / (count - 1)
    }

    //获取样本标准差
    fun getStandardDeviation(): Double {
        return sqrt(getVariance())
    }
}

fun main() {
    inputPrompt()
    //对比自己实现的计算标准差方法和书中实现的标准差计算方法返回值是否相同
    val customAccumulator = CustomAccumulator()
    val accumulator = Accumulator()
    val array = readAllDoubles()
    array.forEach {
        customAccumulator.add(it)
        accumulator.addDataValue(it)
    }
    println(customAccumulator.count)
    println(accumulator.count())
    println(customAccumulator.mean)
    println(accumulator.mean())
    println(customAccumulator.getStandardDeviation())
    println(accumulator.stddev())
}