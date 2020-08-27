package chapter2.section4

import edu.princeton.cs.algs4.Point2D
import edu.princeton.cs.algs4.StdDraw
import extensions.delayExit
import extensions.random
import extensions.spendTimeMillis

/**
 * 编写一个性能测试用例，用插入元素操作填满一个优先队列，
 * 然后用删除最大元素操作删去一半元素，再用插入元素操作填满优先队列，再用删除最大元素操作删去所有元素。
 * 用一列随机的长短不同的元素多次重复以上过程，测量每次运行的时间，打印平均用时或是将其绘制成图标
 */
fun ex36_PerformanceDriverI(size: Int, times: Int): Long {
    val pq = HeapMaxPriorityQueue<Double>(size)
    return spendTimeMillis {
        repeat(times) {
            repeat(size) {
                pq.insert(random())
            }
            repeat(size / 2) {
                pq.delMax()
            }
            repeat(size / 2) {
                pq.insert(random())
            }
            repeat(size) {
                pq.delMax()
            }
        }
    }
}

fun drawPointList(list: List<Point2D>) {
    if (list.isEmpty()) return
    StdDraw.clear()
    StdDraw.setPenRadius(0.01)
    var minX = list[0].x()
    var minY = list[0].y()
    var maxX = minX
    var maxY = minY
    for (i in 1 until list.size) {
        val point = list[i]
        if (point.x() < minX) minX = point.x()
        if (point.x() > maxX) maxX = point.x()
        if (point.y() < minY) minY = point.y()
        if (point.y() > maxY) maxY = point.y()
    }
    if (minX == maxX) {
        maxX++
    }
    if (minY == maxY) {
        maxY++
    }
    //上下左右各留5%的空白
    StdDraw.setXscale(minX - (maxX - minX) / 20, maxX + (maxX - minX) / 20)
    StdDraw.setYscale(minY - (maxY - minY) / 20, maxY + (maxY - minY) / 20)
    list.forEach { point ->
        point.draw()
    }
}

fun main() {
    var size = 100
    val times = 100
    val list = mutableListOf<Point2D>()
    repeat(10) {
        val spendTime = ex36_PerformanceDriverI(size, times)
        println("size=$size times=$times spendTime=$spendTime ms")
        val point = Point2D(size.toDouble(), spendTime.toDouble())
        list.add(point)
        drawPointList(list)
        size *= 2
    }
    delayExit()
}