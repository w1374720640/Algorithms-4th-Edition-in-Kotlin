package chapter2.section5

import chapter2.sleep
import edu.princeton.cs.algs4.Point2D
import edu.princeton.cs.algs4.StdDraw
import extensions.delayExit
import extensions.formatDouble
import extensions.random

/**
 * 简单多边形
 * 给定平面上的N个点，用它们画出一个多边形
 * 提示：找到y坐标最小的点，在有多个最小y坐标的点时取x坐标最小者，然后将其他点按照以p为原点的辐角大小的顺序依次连接起来
 *
 * 解：以y坐标最小的点为极坐标原点，其他任意一点的辐角（极坐标角度值）都是正数，两点连线和x轴正方向夹角最小的点辐角最小
 * 所以最终的排序结果p点在第一位（辐角为0），随着数组索引增大，其他点和p点连线与x轴正方向夹角逐渐增大，按逆时针顺序连线
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
        sleep(1000)
    }
    array[0].drawTo(array.last())
}

fun main() {
    val array = Array(10) { Point2D(random(), random()) }
    println(array.joinToString { "(${formatDouble(it.x(), 2)}, ${formatDouble(it.y(), 2)})" })
    ex26_SimplePolygon(array)
    delayExit()
}