package chapter2.section1

import chapter2.getDoubleArray
import chapter2.less
import chapter2.swap
import extensions.formatDouble
import extensions.spendTimeMillis

/**
 * 几何级数递增序列。
 * 通过实验找到一个t，使得对于大小为10⁶的任意随机数组，
 * 使用递增序列1、t、t²、t³、t⁴、t⁵...的希尔排序运行时间最短。
 * 给出你能找到的三个最佳t值以及相应的递增序列。
 *
 * 解：因为题目中使用了向下取整符号，所以可以认为参数t可能为小数
 */
fun ex30_GeometricIncrements(t: Double): Long {
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
 * 通过逐步缩小范围、提高精度、多次实验取平均值后得到的结果
 * 找到一个值 t=6.301 (最佳就应该只有一个，而不是三个）
 * 递增序列为 1 6 39 250 1576 9932 62583 394335 2484710
 */
fun main() {
    //想要消除单次测量误差，可以多次排序取平均值
    val repeat = 1
    var t = 1.1
    var fastestTime = Long.MAX_VALUE
    //t的判断范围为[1.1,10)，步长为0.1，可以逐步缩小范围和增加精度
    while (t < 10) {
        var time = 0L
        repeat(repeat) {
            time += ex30_GeometricIncrements(t)
        }
        time /= repeat
        if (time < fastestTime) {
            fastestTime = time
            println("fastest=${formatDouble(t, 3)}  fastestTime=$fastestTime")
            var H = 1.0
            do {
                print("${H.toInt()} ")
                H *= t
            } while (H < 100_0000 / 2)
            println()
        }
        t += 0.1
    }
}