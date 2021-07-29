package chapter1.section1

import edu.princeton.cs.algs4.StdDraw
import extensions.inputPrompt
import extensions.random
import extensions.readDouble
import extensions.readInt

/**
 * 直方图
 * 假设标准输入流中含有一系列double值。
 * 编写一段程序，从命令行接受一个整数N和两个double值l和r。
 * 将(l, r)分为N段并使用StdDraw画出输入流中的值落入每段的数量的直方图。
 *
 * 解：这里用随机函数生成count数量的随机值替代标准输入
 */
fun ex32_Histogram(n: Int, left: Double, right: Double, count: Int) {
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
    val max = array.maxOrNull() ?: 0
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
    val n = readInt("n: ")
    val left = readDouble("left: ")
    val right = readDouble("right: ")
    val count = readInt("count: ")
    ex32_Histogram(n, left, right, count)
}