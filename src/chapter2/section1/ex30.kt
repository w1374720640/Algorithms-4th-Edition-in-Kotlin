package chapter2.section1

import chapter2.ArrayInitialState
import chapter2.getDoubleArray
import chapter2.less
import chapter2.swap
import extensions.formatDouble
import extensions.spendTimeMillis

/**
 * 通过实验，找到一个t，使得对于大小为10⁶的任意数组，
 * 使用递增序列1、t、t²、t³、t⁴、t⁵...的希尔排序运行时间最短。
 * 给出你能找到的三个最佳t值以及相应的递增序列
 */
fun ex30(t: Double): Long {
    require(t > 1)
    val array = getDoubleArray(100_0000)
    return spendTimeMillis {
        var H = 1.0
        while (H < array.size / 2) H *= t
        var h = H.toInt()
        while (h >= 1) {
            for (i in h until array.size) {
                for (j in i downTo h step h) {
                    if (!array.less(j, j - h)) break
                    array.swap(j, j - h)
                }
            }
            H /= t
            h = H.toInt()
        }
    }
}

/**
 * 通过逐步缩小范围，提高精度，修改重复次数后得到的结果
 * 找到一个值 t=6.301
 * 递增序列为 1 6 39 250 1576 9932 62583 394335 2484710
 */
fun main() {
    //想要消除单次测量误差，可以多次排序取平均值
    val repeat = 1
    var t = 1.1
    var fastestTime = Long.MAX_VALUE
    while (t < 10) {
        var time = 0L
        repeat(repeat) {
            time += ex30(t)
        }
        time /= repeat
        if (time < fastestTime) {
            fastestTime = time
            println("fastest=${formatDouble(t, 3)}  fastestTime=$fastestTime")
        }
        t += 0.1
    }
}