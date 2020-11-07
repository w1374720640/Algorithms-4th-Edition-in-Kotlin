package chapter3.section4

import extensions.formatDouble
import extensions.random
import extensions.spendTimeMillis

/**
 * 散列的成本
 * 用各种常见的数据类型进行实验以得到hash()方法和compareTo()方法的耗时比的经验数据
 */
fun main() {
    val size = 100_0000
    val times = 100
    println("size=$size  times=$times")
    val intArray = IntArray(size) { random(Int.MAX_VALUE) }
    val intHashTime = spendTimeMillis {
        repeat(times) {
            intArray.forEach { it.hashCode() }
        }
    }
    val intCompareTime = spendTimeMillis {
        repeat(times) {
            val compareValue = random(Int.MAX_VALUE)
            intArray.forEach { it.compareTo(compareValue) }
        }
    }
    println("intHashTime=$intHashTime ms  intCompareTime=$intCompareTime ms  ratio=${formatDouble(intHashTime.toDouble() / intCompareTime, 2)}")

    val doubleArray = DoubleArray(size) { random() }
    val doubleHashTime = spendTimeMillis {
        repeat(times) {
            doubleArray.forEach { it.hashCode() }
        }
    }
    val doubleCompareTime = spendTimeMillis {
        repeat(times) {
            val compareValue = random()
            doubleArray.forEach { it.compareTo(compareValue) }
        }
    }
    println("doubleHashTime=$doubleHashTime ms  doubleCompareTime=$doubleCompareTime ms  ratio=${formatDouble(doubleHashTime.toDouble() / doubleCompareTime, 2)}")

    val stringArray = Array(size) { random(Int.MAX_VALUE).toString() }
    val stringHashTime = spendTimeMillis {
        repeat(times) {
            stringArray.forEach { it.hashCode() }
        }
    }
    val stringCompareTime = spendTimeMillis {
        repeat(times) {
            val compareValue = random(Int.MAX_VALUE).toString()
            stringArray.forEach { it.compareTo(compareValue) }
        }
    }
    println("stringHashTime=$stringHashTime ms  stringCompareTime=$stringCompareTime ms  ratio=${formatDouble(stringHashTime.toDouble() / stringCompareTime, 2)}")
}