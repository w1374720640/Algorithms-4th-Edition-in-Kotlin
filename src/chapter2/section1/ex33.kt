package chapter2.section1

import chapter2.getDoubleArray
import edu.princeton.cs.algs4.Point2D
import edu.princeton.cs.algs4.StdDraw
import extensions.inputPrompt
import extensions.readInt
import extensions.spendTimeMillis

/**
 * 分布图
 * 在无穷循环中，调用sort方法对指定大小的数组排序，用StdDraw画出每次运行的时间，得到一张运行时间的分布图
 */
fun distribution(size: Int, sortMethod: (Array<Double>) -> Unit) {
    val pointList = mutableListOf<Point2D>()
    var maxPointY = 0.0
    fun drawPoint(point: Point2D) {
        pointList.add(point)
        if (point.y() > maxPointY) {
            maxPointY = point.y()
            //最大值发生变化，需要重新初始化画板
            StdDraw.clear()
            StdDraw.setXscale(0.0, size * 1.1)
            StdDraw.setYscale(0.0, maxPointY * 1.1 + 0.1)//加0.1是为了防止运行时间为0ms导致的异常
            StdDraw.setPenRadius()
            //绘制x,y轴
            StdDraw.line(size * 0.05, maxPointY * 0.05, size * 1.05, maxPointY * 0.05)
            StdDraw.line(size * 0.05, maxPointY * 0.05, size * 0.05, maxPointY * 1.05)
            //绘制原点和x,y轴文字
            StdDraw.text(size * 0.05, maxPointY * 0.02, "(0, 0)")
            StdDraw.text(size * 1.05, maxPointY * 0.02, "size")
            StdDraw.text(size * 0.05, maxPointY * 1.08, "time")
            StdDraw.setPenRadius(0.01)
            //绘制所有的点
            pointList.forEach {
                StdDraw.point(it.x() + size * 0.05, it.y() + maxPointY * 0.05)
            }
        } else {
            //最大值没发生变化，只绘制新增加的点
            StdDraw.point(point.x() + size * 0.05, point.y() + maxPointY * 0.05)
        }
    }

    while (true) {
        val array = getDoubleArray(size)
        val time = spendTimeMillis {
            sortMethod(array)
        }
        drawPoint(Point2D(size.toDouble(), time.toDouble()))
    }
}

fun main() {
    inputPrompt()
    val size = readInt("size: ")
    distribution(size, ::selectSort)
}