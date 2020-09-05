package chapter2.section5

import edu.princeton.cs.algs4.Point2D
import edu.princeton.cs.algs4.StdDraw
import extensions.delayExit
import extensions.formatDouble
import extensions.random

/**
 * 简单多边形
 * 给定平面上的N个点，用它们画出一个多边形
 * 提示：找到y坐标最小的点，在有多个最小y坐标的点时取x坐标最小者，然后将其他点按照以p为原点的辐角大小的顺序依次连接起来
 */
fun ex26_SimplePolygon(array: Array<Point2D>) {
    require(array.size >= 3)
    array.sortWith(Point2DYComparator)
    var firstPoint = array[0]
    for (i in 1 until array.size) {
        if (array[i].y() == firstPoint.y()) {
            if (array[i].x() < firstPoint.x()) {
                firstPoint = array[i]
            }
        } else {
            break
        }
    }
    array.sortWith(Point2DAngleComparator(firstPoint))
    StdDraw.setPenRadius(0.01)
    for (i in 1 until array.size) {
        array[i - 1].drawTo(array[i])
    }
    array[0].drawTo(array.last())
}

fun main() {
    val array = Array(10) { Point2D(random(), random()) }
    println(array.joinToString { "(${formatDouble(it.x(), 2)}, ${formatDouble(it.y(), 2)})" })
    ex26_SimplePolygon(array)
    delayExit()
}