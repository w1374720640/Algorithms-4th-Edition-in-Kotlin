package chapter4.section2

import chapter4.section1.GraphGraphics
import edu.princeton.cs.algs4.In
import edu.princeton.cs.algs4.Point2D
import edu.princeton.cs.algs4.StdDraw
import extensions.random

/**
 * 有向欧几里得图
 * 修改你为4.1.36给出的解答，为平面图设计一份API名为EuclideanDigraph，这样你就能够处理用图形表示的图了。
 *
 * 解：为了绘制方便，线段红色的一端表示箭头的指向，强连通的边两端都是红色的
 * 有向图的连通性看着没有无向图直观，没有像无向图一样添加通用绘制功能
 */
class EuclideanGraph : Digraph {
    constructor(V: Int) : super(V)
    constructor(input: In) : super(input)

    val points = Array(V) { Point2D(random(), random()) }

    fun draw() {
        StdDraw.clear()
        drawPoint()
        drawVertex()
        drawEdge()
    }

    private fun drawPoint() {
        StdDraw.setPenRadius(GraphGraphics.DEFAULT_POINT_RADIUS)
        StdDraw.setPenColor(StdDraw.BLACK)
        points.forEach {
            StdDraw.point(it.x(), it.y())
        }
    }

    private fun drawVertex() {
        StdDraw.setPenRadius()
        StdDraw.setPenColor(StdDraw.RED)
        points.forEachIndexed { v, point ->
            StdDraw.text(point.x(), point.y() + GraphGraphics.DEFAULT_POINT_RADIUS * 2, v.toString())
        }
    }

    private fun drawEdge() {
        StdDraw.setPenRadius(GraphGraphics.DEFAULT_EDGE_WIDTH)
        StdDraw.setPenColor(StdDraw.BLACK)
        // 先绘制黑色基础线段
        adj.forEachIndexed { v, bag ->
            bag.forEach { w ->
                StdDraw.line(points[v].x(), points[v].y(), points[w].x(), points[w].y())
            }
        }
        // 再绘制方向
        StdDraw.setPenColor(StdDraw.RED)
        StdDraw.setPenRadius(GraphGraphics.DEFAULT_EDGE_WIDTH * 2)
        adj.forEachIndexed { v, bag ->
            bag.forEach { w ->
                val splitX = points[v].x() + (points[w].x() - points[v].x()) * 0.8
                val splitY = points[v].y() + (points[w].y() - points[v].y()) * 0.8
                StdDraw.line(splitX, splitY, points[w].x(), points[w].y())
            }
        }
    }
}

fun main() {
    val digraph = EuclideanGraph(In("./data/tinyDG.txt"))
    digraph.draw()
}