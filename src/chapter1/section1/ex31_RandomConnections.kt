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

//在圆上画N个点，再将每对点按概率p用灰线连接
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
    val points = Array(N) { Array(2) { 0.0 } }
    for (i in 0 until N) {
        points[i][0] = getXPosition(centerX, radius, 360.0 / N * i)
        points[i][1] = getYPosition(centerY, radius, 360.0 / N * i)
    }
    StdDraw.setPenColor(Color.BLACK)
    StdDraw.setPenRadius(0.03)
    points.forEach {
        Point2D(it[0], it[1]).draw()
    }
    StdDraw.setPenColor(Color.GRAY)
    StdDraw.setPenRadius(0.005)
    for (i in 0 until N - 1) {
        for (j in i + 1 until N) {
            if (randomBoolean(p)) {
                StdDraw.line(points[i][0], points[i][1], points[j][0], points[j][1])
            }
        }
    }
}

fun main() {
    inputPrompt()
    ex31_RandomConnections(readInt(), readDouble())
}