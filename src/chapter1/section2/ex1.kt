package chapter1.section2

import edu.princeton.cs.algs4.Point2D
import edu.princeton.cs.algs4.StdDraw
import extensions.formatDouble
import extensions.inputPrompt
import extensions.random
import extensions.readInt

/**
 * 编写一个Point2D的用例，从命令行接受一个整数N。
 * 在单位正方形中生成N个随机点，然后计算两个点之间的最近距离
 */
fun main() {
    inputPrompt()
    val N = readInt("N: ")
    val pointList = mutableListOf<Point2D>()
    repeat(N) {
        pointList.add(Point2D(random(), random()))
    }
    var minDistance = 2.0
    var index1 = -1
    var index2 = -1
    for (i in 0 until pointList.size - 1) {
        for (j in i + 1 until pointList.size) {
            val distance = pointList[i].distanceTo(pointList[j])
            if (distance < minDistance) {
                minDistance = distance
                index1 = i
                index2 = j
            }
        }
    }
    StdDraw.setPenRadius(0.01)
    pointList.forEach { it.draw() }
    StdDraw.setPenRadius(0.005)
    if (index1 >= 0 && index2 >= 0) {
        pointList[index1].drawTo(pointList[index2])
    }

    println("minDistance=${minDistance}")
    println("point1=[${formatDouble(pointList[index1].x(), 4)},${formatDouble(pointList[index1].y(), 4)}]")
    println("point2=[${formatDouble(pointList[index2].x(), 4)},${formatDouble(pointList[index2].y(), 4)}]")
}