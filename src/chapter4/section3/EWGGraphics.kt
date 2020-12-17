package chapter4.section3

import chapter2.sleep
import chapter3.section4.LinearProbingHashST
import edu.princeton.cs.algs4.Point2D
import edu.princeton.cs.algs4.StdDraw
import extensions.random
import kotlin.math.ceil
import kotlin.math.sqrt

/**
 * 绘制无向图[Graph]的图形
 * 先用[BreadthFirstCC]类找出连通分量的数量，每个连通分量占据独立的一块区域
 * 在对应区域内为每个分量中的顶点添加一个随机坐标，连接每条边
 *
 * 将矩形的绘图区域按给定比例切分，并保证每个区域都是矩形（尽量小的长宽比）：
 * 1、将每个连通分量按大小降序排序
 * 2、从第一个连通分量开始，将每个连通分量的大小相加，直到大于等于总数的一半
 * 3、将绘图区域按比例分为两半，如果宽度大于等于高度，用垂直线分割，否则用水平线分割
 * 4、用递归的方式将前半部分和后半部分在对应的区域内重复步骤2和步骤3，直到每个区域只有一个连通分量
 */
class EWGGraphics(val graph: EdgeWeightedGraph) {
    private val st = LinearProbingHashST<Int, Point2D>()

    init {
        val cc = EdgeWeightedGraphCC(graph)
        require(cc.count() == 1) { "All vertices should be connected." }
        initAllPoint()
    }

    companion object {
        const val DEFAULT_SHOW_INDEX = true
        const val DEFAULT_PADDING = 0.1
        const val DEFAULT_POINT_RADIUS = 0.01
        const val DEFAULT_EDGE_WIDTH = 0.002
    }

    var showIndex = DEFAULT_SHOW_INDEX // 是否显示图的顶点名称
    var padding = DEFAULT_PADDING // 每个连通分量绘制区域，边界不可绘制区域所占的百分比
    var pointRadius = DEFAULT_POINT_RADIUS // 顶点半径
    var edgeWidth = DEFAULT_EDGE_WIDTH // 边的宽度

    fun draw() {
        StdDraw.clear()
        drawPoint()
        drawEdge()
        if (showIndex) {
            drawIndex()
        }
    }

    fun drawMSTEdge(edge: Edge) {
        StdDraw.setPenRadius(edgeWidth * 2)
        StdDraw.setPenColor(StdDraw.BLACK)
        val v = edge.either()
        val w = edge.other(v)
        val pointV = st.get(v)!!
        val pointW = st.get(w)!!
        StdDraw.line(pointV.x(), pointV.y(), pointW.x(), pointW.y())
    }

    private fun initAllPoint() {
        // 为了让一个连通分量中的顶点尽可能均匀分布，同时不同顶点的x、y坐标不相等（相等时边的图形可能会重叠）
        // 将每个绘制区域划分为 radix * radix 个相等的区域，每个点在有限范围内取随机值
        val radix = getRadix(graph.V)
        val width = 1.0 / radix
        val height = 1.0 / radix
        for (v in 0 until graph.V) {
            val left = 0.0 + width * (v % radix)
            val x = random(left + width * padding, left + width * (1 - 2 * padding))
            val bottom = 0.0 + height * (v / radix)
            val y = random(bottom + height * padding, bottom + height * (1 - 2 * padding))
            st.put(v, Point2D(x, y))
        }
    }

    /**
     * 获取大于等于根号num的最小整数
     */
    private fun getRadix(num: Int): Int {
        return ceil(sqrt(num.toDouble())).toInt()
    }


    private fun drawPoint() {
        StdDraw.setPenRadius(pointRadius)
        StdDraw.setPenColor(StdDraw.BLACK)
        st.keys().forEach { key ->
            val point2D = st.get(key)!!
            StdDraw.point(point2D.x(), point2D.y())
        }
    }

    private fun drawEdge() {
        StdDraw.setPenRadius(edgeWidth)
        StdDraw.setPenColor(StdDraw.LIGHT_GRAY)
        graph.edges().forEach {
            val v = it.either()
            val w = it.other(v)
            val pointV = st.get(v)!!
            val pointW = st.get(w)!!
            StdDraw.line(pointV.x(), pointV.y(), pointW.x(), pointW.y())
        }
    }

    private fun drawIndex() {
        StdDraw.setPenRadius()
        StdDraw.setPenColor(StdDraw.RED)
        for (i in 0 until graph.V) {
            val point2D = st.get(i)!!
            StdDraw.text(point2D.x(), point2D.y() + pointRadius * 2, i.toString())
        }
    }
}

/**
 * 绘制加权无向图及其最小生成树，并且可以绘制查找过程
 * [createMST]用于创建一个最小生成树，接收一个回调函数，返回一个MST对象，回调函数又接收一个边作为参数
 */
fun drawEWGGraph(graph: EdgeWeightedGraph,
                 showIndex: Boolean = EWGGraphics.DEFAULT_SHOW_INDEX,
                 pointRadius: Double = EWGGraphics.DEFAULT_POINT_RADIUS,
                 edgeWidth: Double = EWGGraphics.DEFAULT_EDGE_WIDTH,
                 delay: Long = 1000L,
                 createMST: (() -> MST)? = null): EWGGraphics {
    val graphics = EWGGraphics(graph)
    graphics.showIndex = showIndex
    graphics.pointRadius = pointRadius
    graphics.edgeWidth = edgeWidth
    graphics.draw()
    if (createMST != null) {
        val mst = createMST()
        mst.edges().forEach {
            sleep(delay)
            graphics.drawMSTEdge(it)
        }
    }
    return graphics
}

fun main() {
    val tinyGraph = getTinyWeightedGraph()
    val mediumGraph = getMediumWeightedGraph()
    drawEWGGraph(tinyGraph) {
        PrimMST(tinyGraph)
    }
    sleep(2000)
    drawEWGGraph(mediumGraph, showIndex = false, delay = 100) {
        LazyPrimMST(mediumGraph)
    }
    sleep(2000)
    drawEWGGraph(mediumGraph, showIndex = false, delay = 100) {
        KruskalMST(mediumGraph)
    }
}