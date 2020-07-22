package chapter2.section1

import chapter2.getDoubleArray
import edu.princeton.cs.algs4.Point2D
import edu.princeton.cs.algs4.StdDraw
import extensions.delayExit
import extensions.spendTimeMillis

/**
 * 运行时间曲线图
 * 使用StdDraw在不同规模的随机输入下将算法的平均运行时间绘制成一张曲线图
 */
fun runningTimeGraph(maxSize: Int, sortMethod: (Array<Double>) -> Unit, repeatTimes: Int = 10) {
    require(maxSize >= 100) { "The array is too small, the graph does not have reference significance" }
    require(repeatTimes >= 1)

    val pointList = mutableListOf<Point2D>()
    fun drawPointList() {
        require(pointList.isNotEmpty())
        val lastPoint = pointList.last()
        //重置画板
        StdDraw.clear()
        StdDraw.setXscale(0.0, lastPoint.x() * 1.1)
        StdDraw.setYscale(0.0, lastPoint.y() * 1.1 + 0.1)//加0.1是为了防止运行时间为0ms导致的异常
        StdDraw.setPenRadius()
        //绘制x,y轴
        StdDraw.line(lastPoint.x() * 0.05, lastPoint.y() * 0.05, lastPoint.x() * 1.05, lastPoint.y() * 0.05)
        StdDraw.line(lastPoint.x() * 0.05, lastPoint.y() * 0.05, lastPoint.x() * 0.05, lastPoint.y() * 1.05)
        //绘制原点和x,y轴文字
        StdDraw.text(lastPoint.x() * 0.05, lastPoint.y() * 0.02, "(0, 0)")
        StdDraw.text(lastPoint.x() * 1.05, lastPoint.y() * 0.02, "size")
        StdDraw.text(lastPoint.x() * 0.05, lastPoint.y() * 1.08, "time")
        StdDraw.setPenRadius(0.01)
        //绘制所有的点
        pointList.forEach {
            StdDraw.point(it.x() + lastPoint.x() * 0.05, it.y() + lastPoint.y() * 0.05)
        }
    }

    var size = 100
    while (size <= maxSize) {
        val array = getDoubleArray(size)
        var time = 0.0
        //因为需要的是平均时间，所以这里默认运行10次取平均值
        repeat(repeatTimes) {
            time += spendTimeMillis {
                sortMethod(array)
            }
        }
        time /= repeatTimes
        pointList.add(Point2D(size.toDouble(), time))
        drawPointList()
        size *= 2
    }
}

fun main() {
    runningTimeGraph(500_0000, ::shellSort)
    delayExit()
}