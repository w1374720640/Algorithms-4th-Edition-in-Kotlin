package chapter1.exercise1_4

import edu.princeton.cs.algs4.In
import extensions.formatDouble
import extensions.spendTimeMillis
import kotlin.math.pow

/**
 * 判断程序运行时间大致的增长数量级，每次数组长度加倍
 * 默认从./data/1Mints.txt文件中读取数组，然后截取需要的长度
 */
fun timeRatio(path: String = "./data/1Mints.txt", times: Int = 11, start: Int = 1000, action: (IntArray) -> Long) {
    var preTime = 0L
    val array = In(path).readAllInts()
    repeat(times) {
        val newSize = start * (2.0.pow(it)).toInt()
        val arrayCopy = array.copyOf(if (newSize > array.size) array.size else newSize)
        var result = 0L
        val time = spendTimeMillis {
            result = action(arrayCopy)
        }
        val ratio = if (preTime == 0L) "/" else formatDouble(time.toDouble() / preTime, 2)
        println("size = ${arrayCopy.size}  result = $result  time = ${time}ms  ratio = $ratio")
        preTime = time
        if (newSize > array.size) return
    }
}
