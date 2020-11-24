package chapter4.section1

import extensions.formatDouble
import extensions.inputPrompt
import extensions.readDouble
import extensions.readInt
import kotlin.math.PI
import kotlin.math.log2
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * 随机欧几里得图
 * 编写一个EuclideanGraph的用例（请见练习4.1.36）RandomEuclideanGraphs，
 * 用随机在平面上生成V个点的方式生成随机图，然后将每个点和在以该点为中心，半径为d的圆内的其他点相连。
 * 注意：如果d大于阈值sqrt(lgV/(PI*V))，那么得到的图几乎必然是连通的，否则得到的图几乎必然是不连通的。
 */
fun ex41_RandomEuclideanGraphs(V: Int, d: Double): EuclideanGraph {
    val graph = EuclideanGraph(V)
    val points = graph.points
    for (v in 0 until V) {
        for (w in v + 1 until V) {
            if ((points[v].x() - points[w].x()).pow(2) + (points[v].y() - points[w].y()).pow(2) < d * d) {
                graph.addEdge(v, w)
            }
        }
    }
    return graph
}

fun main() {
    inputPrompt()
    val V = readInt("V: ")
    val d = readDouble("d: ")
    val threshold = sqrt(log2(V.toDouble()) / (PI * V))
    println("threshold: ${formatDouble(threshold, 4)}")
    if (d > threshold) {
        println("almost certainly be connected")
    } else {
        println("almost certainly disconnected")
    }
    val graph = ex41_RandomEuclideanGraphs(V, d)
    graph.draw()
}