package chapter3.section2

import edu.princeton.cs.algs4.StdDraw
import extensions.random
import java.awt.Color
import kotlin.math.log2

/**
 * 平均查找耗时
 * 用实验研究和计算在一颗由N个随机结点构造的二叉树中到达任意结点的平均路径长度（内部路径长度除以N再加1）的平均差和标准差
 * 对于100到10000之间的每个N重复实验1000遍
 * 将结果绘制成和图3.2.14相似的一张Tufte图，并画上函数1.39lgN-1.85的曲线（请见练习3.2.35和练习3.2.39）
 *
 * 解：这里只画出图形，平均差和标准差参考练习3.2.39，N和重复次数均适当缩小，不然耗时太长
 */
fun main() {
    val min = 10
    val max = 500
    val repeatTimes = 50

    val pointList = Array(max - min + 1) { DoubleArray(repeatTimes) }
    for (N in min..max) {
        val array = pointList[N - min]
        repeat(repeatTimes) {
            val st = BinaryTreeST<Int, Int>()
            repeat(N) {
                st.put(random(Int.MAX_VALUE), 0)
            }
            array[it] = st.avgComparesDouble()
        }
    }

    var maxCompares = 0.0
    pointList.forEach { array ->
        array.forEach {
            if (it > maxCompares) {
                maxCompares = it
            }
        }
    }

    val ex35 = { N: Int ->
        1.39 * log2(N.toDouble()) - 1.85
    }

    if (ex35(max) > maxCompares) {
        maxCompares = ex35(max)
    }

    StdDraw.setXscale(0.0, max * 1.05)
    StdDraw.setYscale(0.0, maxCompares * 1.05)
    for (N in min until max) {
        StdDraw.line(N.toDouble(), ex35(N), N.toDouble() + 1, ex35(N + 1))
    }
    StdDraw.setPenColor(Color.LIGHT_GRAY)
    for (i in pointList.indices) {
        pointList[i].forEach {
            StdDraw.point(i + min.toDouble(), it)
        }
    }
}