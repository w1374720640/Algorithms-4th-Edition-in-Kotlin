package chapter1.section1

import edu.princeton.cs.algs4.Point2D
import edu.princeton.cs.algs4.StdDraw
import extensions.inputPrompt
import extensions.randomBoolean
import extensions.readDouble
import extensions.readInt
import java.awt.Color
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * 随机连接
 * 编写一段程序，从命令行接受一个整数N和double值p（0到1之间）作为参数，
 * 在一个圆上画出大小为0.05且间距相等的N个点，然后将每对点按照概率p用灰线连接。
 */
fun ex31_RandomConnections(N: Int, p: Double) {
    require(N > 0 && p >= 0.0 && p <= 1.0)
    //根据角度值计算x的坐标
    fun getXPosition(centerX: Double, radius: Double, angle: Double): Double {
        return centerX + radius * cos(angle * PI / 180)
    }

    //根据角度值计算y的坐标
    fun getYPosition(centerY: Double, radius: Double, angle: Double): Double {
        return centerY + radius * sin(angle * PI / 180)
    }

    val centerX = 0.5
    val centerY = 0.5
    val radius = 0.4
    val points = Array(N) { Point2D(0.0, 0.0) }
    for (i in 0 until N) {
        points[i] = Point2D(
                getXPosition(centerX, radius, 360.0 / N * i),
                getYPosition(centerY, radius, 360.0 / N * i)
        )
    }
    StdDraw.setPenColor(Color.LIGHT_GRAY)
    StdDraw.circle(centerX, centerY, radius)
    StdDraw.setPenColor(Color.BLACK)
    StdDraw.setPenRadius(0.01)
    points.forEach {
        it.draw()
    }
    StdDraw.setPenColor(Color.GRAY)
    StdDraw.setPenRadius(0.002)
    for (i in 0 until N - 1) {
        for (j in i + 1 until N) {
            if (randomBoolean(p)) {
                StdDraw.line(points[i].x(), points[i].y(), points[j].x(), points[j].y())
            }
        }
    }
}

fun main() {
    inputPrompt()
    ex31_RandomConnections(readInt("N: "), readDouble("p: "))
}