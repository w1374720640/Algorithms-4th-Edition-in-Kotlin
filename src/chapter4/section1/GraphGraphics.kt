package chapter4.section1

import chapter2.section3.size
import chapter2.sleep
import chapter3.section4.LinearProbingHashST
import edu.princeton.cs.algs4.Bag
import edu.princeton.cs.algs4.In
import edu.princeton.cs.algs4.Point2D
import edu.princeton.cs.algs4.StdDraw
import extensions.random

/**
 * 绘制Graph数据结构的图形
 * 先用CC类找出连通分量的数量，每个连通分量占据独立的一块区域
 * 在对应区域内为每个分量中的顶点添加一个随机坐标，连接每条边
 *
 * 将矩形的绘图区域按给定比例切分，并保证每个区域都是矩形（尽量小的长宽比）：
 * 1、将每个连通分量按大小降序排序
 * 2、从第一个连通分量开始，将每个连通分量的大小相加，直到大于等于总数的一半
 * 3、将绘图区域按比例分为两半，如果x方向长度大于等于y方向长度，用垂直线分割，否则用水平线分割
 * 4、用递归的方式将前半部分和后半部分在对应的区域内重复步骤2和步骤3，直到每个区域只有一个连通分量
 */
class GraphGraphics(val graph: Graph) {

    /**
     * 可绘制的矩形区域
     */
    class Rect(val left: Double, val right: Double, val top: Double, val bottom: Double) {

        fun width() = right - left

        fun height() = top - bottom
    }

    val cc = CC(graph)
    val count = cc.count() // 连通分量的数量
    private val bags = Array(count) { Bag<Int>() } // 连通分量所有顶点组成的数组
    private val areas = arrayOfNulls<Rect>(count) // 每个连通分量所对应的绘制区域


    init {
        for (v in 0 until graph.V) {
            bags[cc.id(v)].add(v)
        }
        // 按连通分量大小逆序排序
        bags.sortByDescending { it.size() }
        splitArea(0 until count, Rect(0.0, 1.0, 1.0, 0.0))

        // 迭代结束后，所有的连通分量应该都已经被赋值
        areas.forEach {
            check(it != null)
        }
    }

    /**
     * 按连通分量大小，等比例分割可绘制区域
     */
    private fun splitArea(range: IntRange, area: Rect) {
        if (range.isEmpty()) return
        if (range.size() == 1) {
            areas[range.first] = area
            return
        }
        var total = 0
        for (i in range) {
            total += bags[i].size()
        }
        var sum = 0
        for (i in range) {
            sum += bags[i].size()
            // 由于预先按大小排序，所以当范围大于1时，必定会走到这个方法内
            if (sum * 2 >= total) {
                val ratio = sum.toDouble() / total
                val range1 = range.first..i
                val range2 = (i + 1)..range.last
                val area1: Rect
                val area2: Rect
                // 根据可绘制区域宽高比确定裁切方向
                if (area.width() >= area.height()) {
                    area1 = Rect(area.left, area.left + area.width() * ratio, area.top, area.bottom)
                    area2 = Rect(area.left + area.width() * ratio, area.right, area.top, area.bottom)
                } else {
                    area1 = Rect(area.left, area.right, area.top, area.top - area.height() * ratio)
                    area2 = Rect(area.left, area.right, area.top - area.height() * ratio, area.bottom)
                }
                splitArea(range1, area1)
                splitArea(range2, area2)
                break
            }
        }
    }

    private val points = Array(count) { LinearProbingHashST<Int, Point2D>() } // 所有连通分量的顶点坐标
    var showSplitLine = false // 是否显示连通分量之间的分隔线
    var showIndex = false // 是否显示图的顶点名称
    var padding = 0.05 // 每个连通分量绘制区域，边界不可绘制区域所占的百分比
    var splitLineWidth = 0.002 // 连通分量之间分隔线宽度
    var pointRadius = 0.01 // 顶点半径
    var edgeWidth = 0.002 // 边的宽度

    fun draw() {
        StdDraw.clear()
        initAllPoint()
        if (showSplitLine) {
            drawSplitLine()
        }
        drawPoint()
        drawEdge()
        if (showIndex) {
            drawIndex()
        }
    }

    private fun initAllPoint() {
        for (i in bags.indices) {
            val bag = bags[i]
            val area = areas[i]!!
            val st = points[i]
            bag.forEach {
                val x = random(area.left + area.width() * padding, area.right - area.width() * padding)
                val y = random(area.bottom + area.height() * padding, area.top - area.height() * padding)
                st.put(it, Point2D(x, y))
            }
        }
    }

    private fun drawSplitLine() {
        StdDraw.setPenRadius(splitLineWidth)
        StdDraw.setPenColor(StdDraw.LIGHT_GRAY)
        areas.forEach { area ->
            if (area!!.left != 0.0) {
                StdDraw.line(area.left, area.top, area.left, area.bottom)
            }
            if (area.top != 1.0) {
                StdDraw.line(area.left, area.top, area.right, area.top)
            }
            if (area.right != 1.0) {
                StdDraw.line(area.right, area.top, area.right, area.bottom)
            }
            if (area.bottom != 0.0) {
                StdDraw.line(area.left, area.bottom, area.right, area.bottom)
            }
        }
    }

    private fun drawPoint() {
        StdDraw.setPenRadius(pointRadius)
        StdDraw.setPenColor(StdDraw.BLACK)
        points.forEach { st ->
            st.keys().forEach { key ->
                val point2D = st.get(key)!!
                StdDraw.point(point2D.x(), point2D.y())
            }
        }
    }

    private fun drawEdge() {
        StdDraw.setPenRadius(edgeWidth)
        StdDraw.setPenColor(StdDraw.BLACK)
        for (i in bags.indices) {
            val bag = bags[i]
            val st = points[i]
            bag.forEach { v ->
                val pointV = st.get(v)!!
                graph.adj(v).forEach { w ->
                    val pointW = st.get(w)!!
                    StdDraw.line(pointV.x(), pointV.y(), pointW.x(), pointW.y())
                }
            }
        }
    }

    private fun drawIndex() {
        StdDraw.setPenRadius()
        StdDraw.setPenColor(StdDraw.RED)
        points.forEach { st ->
            st.keys().forEach { key ->
                val point2D = st.get(key)!!
                StdDraw.text(point2D.x(), point2D.y() + pointRadius * 2, key.toString())
            }
        }
    }
}

/**
 * 绘制无向图的图形
 * 同一个连通分量内点的位置随机生成，可能会显示重叠，可以重新运行程序生成新的图形
 */
fun drawGraph(graph: Graph,
              showSplitLine: Boolean = false,
              showIndex: Boolean = false,
              splitLineWidth: Double = 0.002,
              pointRadius: Double = 0.01,
              edgeWidth: Double = 0.002) {
    val graphGraphics = GraphGraphics(graph)
    graphGraphics.showSplitLine = showSplitLine
    graphGraphics.showIndex = showIndex
    graphGraphics.splitLineWidth = splitLineWidth
    graphGraphics.pointRadius = pointRadius
    graphGraphics.edgeWidth = edgeWidth
    graphGraphics.draw()
}

private fun showTinyGraph() {
    val path = "./data/tinyG.txt"
    val graph = Graph(In(path))
    drawGraph(graph, pointRadius = 0.02, showIndex = true)
}

private fun showMediumGraph() {
    val path = "./data/mediumG.txt"
    val graph = Graph(In(path))
    drawGraph(graph)
}

private fun showRandomGraph(size: Int, edgeNum: Int) {
    val graph = Graph(size)
    repeat(edgeNum) {
        val v = random(size)
        val w = random(size)
        if (v != w) {
            graph.addEdge(v, w)
        }
    }
    drawGraph(graph)
}

fun main() {
    showTinyGraph()
    sleep(2000)

    showMediumGraph()
    sleep(2000)

    showRandomGraph(100, 50)
}