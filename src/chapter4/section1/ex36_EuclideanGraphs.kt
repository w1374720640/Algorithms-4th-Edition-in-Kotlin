package chapter4.section1

import chapter2.sleep
import edu.princeton.cs.algs4.In
import edu.princeton.cs.algs4.Point2D
import edu.princeton.cs.algs4.StdDraw
import extensions.random

/**
 * 欧几里得图
 * 为平面上的图设计并实现一份叫做EuclideanGraph的API，其中图所有顶点均有坐标。
 * 实现一个show()方法，用StdDraw将图绘出。
 *
 * 解：和通用无向图绘制程序[GraphGraphics]的区别：
 * GraphGraphics先查找所有的连通分量，将可绘制区域按每个连通分量的大小分割，每个点在有限范围内取随机值
 * 欧几里得图不区分不同的连通分量，所有值在完整的可绘制区域取随机值
 */
class EuclideanGraph : Graph {
    constructor(V: Int) : super(V)
    constructor(input: In) : super(input)

    val points = Array(V) { Point2D(random(), random()) }

    fun draw() {
        StdDraw.clear()
        drawPoint()
        drawEdge()
    }

    private fun drawPoint() {
        StdDraw.setPenRadius(GraphGraphics.DEFAULT_POINT_RADIUS)
        StdDraw.setPenColor(StdDraw.BLACK)
        points.forEach {
            StdDraw.point(it.x(), it.y())
        }
    }

    private fun drawEdge() {
        // 绘制前先去重
        val edgeSet = getEdgeSet()
        StdDraw.setPenRadius(GraphGraphics.DEFAULT_EDGE_WIDTH)
        StdDraw.setPenColor(StdDraw.BLACK)
        edgeSet.keys().forEach {
            StdDraw.line(points[it.small].x(), points[it.small].y(), points[it.large].x(), points[it.large].y())
        }
    }
}

fun main() {
    // 分别用GraphGraphics和EuclideanGraph绘制的相同结构的图
    val graph = Graph(In("./data/tinyG.txt"))
    drawGraph(graph)
    sleep(3000)

    val euclideanGraph = EuclideanGraph(In("./data/tinyG.txt"))
    euclideanGraph.draw()
}