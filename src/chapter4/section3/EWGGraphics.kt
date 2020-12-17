package chapter4.section3

import chapter2.sleep
import chapter3.section4.LinearProbingHashST
import chapter4.section1.ex41_RandomEuclideanGraphs
import edu.princeton.cs.algs4.Point2D
import edu.princeton.cs.algs4.StdDraw
import extensions.random
import kotlin.math.ceil
import kotlin.math.sqrt

/**
 * 绘制加权无向图[EWG]及其最小生成树，并且可以绘制树的增长过程
 * 要求无向图为连通图
 * 参考[chapter4.section1.GraphGraphics]的实现
 */
class EWGGraphics(val graph: EWG) {
    private val st = LinearProbingHashST<Int, Point2D>()

    init {
        val cc = EdgeWeightedGraphCC(graph)
        require(cc.count() == 1) { "All vertices should be connected." }
        initAllPoint()
    }

    /**
     * 不使用随机生成的坐标，为每个顶点设置一个新坐标
     * 从文件中读取指定加权无向图时只能使用随机坐标，边的长度和权重无关
     * 生成随机无向图时可以指定边的权重等于边的长度
     */
    fun setPoints(points: Array<Point2D>) {
        points.forEachIndexed { index, point2D ->
            st.put(index, point2D)
        }
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
 * 获取一个边长等于权重且可视性良好的加权无向图及每个顶点的坐标
 *
 * 根据练习4.1.41生成一个几乎必然连通的无向图，每个点都有一个对应的坐标
 * 将每个边的权重设置为边的长度，生成一个加权无向图
 */
fun getRandomEWG(V: Int = 100, d: Double = 0.2): Pair<EdgeWeightedGraph, Array<Point2D>> {
    val euclideanGraph = ex41_RandomEuclideanGraphs(V, d)
    val edgeWeightedGraph = EdgeWeightedGraph(euclideanGraph.V)
    for (v in 0 until euclideanGraph.V) {
        euclideanGraph.adj(v).forEach { w ->
            if (v < w) {
                val pointV = euclideanGraph.points[v]
                val pointW = euclideanGraph.points[w]
                val weight = pointV.distanceTo(pointW)
                val edge = Edge(v, w, weight)
                edgeWeightedGraph.addEdge(edge)
            }
        }
    }
    return edgeWeightedGraph to euclideanGraph.points
}

/**
 * 绘制加权无向图及其最小生成树，并且[createMST]参数非空的情况下可以绘制树的增长过程
 */
fun drawEWGGraph(graph: EWG,
                 points: Array<Point2D>? = null,
                 showIndex: Boolean = EWGGraphics.DEFAULT_SHOW_INDEX,
                 pointRadius: Double = EWGGraphics.DEFAULT_POINT_RADIUS,
                 edgeWidth: Double = EWGGraphics.DEFAULT_EDGE_WIDTH,
                 delay: Long = 1000L,
                 createMST: ((EWG) -> MST)? = null) {
    val graphics = EWGGraphics(graph)
    points?.let { graphics.setPoints(it) }
    graphics.showIndex = showIndex
    graphics.pointRadius = pointRadius
    graphics.edgeWidth = edgeWidth
    graphics.draw()
    if (createMST != null) {
        val mst = createMST(graph)
        mst.edges().forEach {
            sleep(delay)
            graphics.drawMSTEdge(it)
        }
    }
}

fun drawRandomEWG(createMST: ((EWG) -> MST)) {
    val randomGraph = getRandomEWG()
    drawEWGGraph(randomGraph.first, points = randomGraph.second, showIndex = false, delay = 100) {
        createMST(it)
    }
}

fun main() {
    drawRandomEWG { PrimMST(it) }
    sleep(2000)
    drawRandomEWG { KruskalMST(it) }
}