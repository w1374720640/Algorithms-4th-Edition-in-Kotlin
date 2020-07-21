package chapter1.section1

import edu.princeton.cs.algs4.StdDraw
import extensions.inputPrompt
import extensions.random
import extensions.readDouble
import extensions.readInt

//将[left,right)分为n段，从标准输入中读取一些列值，判断值落在每段范围内的数量，并画出直方图
//这里用随机函数生成count数量的随机值替代标准输入
fun ex32(n: Int, left: Double, right: Double, count: Int) {
    require(n > 0 && right > left && count > 0)
    val array = IntArray(n)
    val interval = (right - left) / n
    repeat(count) {
        //模拟标准输入流中的一系列Double值
        val value = random(left, right)
        val index = ((value - left) / interval).toInt()
        array[index]++
    }
    println()
    println("array=${array.joinToString()}")
    val max = array.max() ?: 0
    for (i in array.indices) {
        StdDraw.filledRectangle(
                1.0 / n * i + 1.0 / n / 2,
                0.9 / max * array[i] / 2,
                0.9 / n / 2,
                0.9 / max * array[i] / 2
                )
    }
}

fun main() {
    inputPrompt()
    val n = readInt()
    val left = readDouble()
    val right = readDouble()
    val count = readInt()
    ex32(n, left, right, count)
}