package chapter4.section4

import chapter2.section4.HeapIndexMinPriorityQueue
import chapter2.sleep
import chapter4.section1.GraphGraphics
import edu.princeton.cs.algs4.*
import extensions.random
import extensions.setSeed
import kotlin.math.PI
import kotlin.math.log2
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * 欧几里得图中的最短路径
 * 已知图中的顶点均在平面上，修改API以提高Dijkstra算法的性能。
 *
 * 解：这里我们认为欧几里得图边的权重近似于两个顶点之间的距离（可以不严格相等，类似与地图两点之间的路径）
 * 图可以有环，但边的权重为正数（距离只能为正数）
 * 参考练习2.5.32中的A*寻路算法[chapter2.section5.AStartPathFind]
 * 和普通Dijkstra算法的区别：
 * 欧几里得图可以计算两点之间的直线距离，以直线距离作为评估函数确定优先查找的方向，
 * 如果起点s在图中央，终点t在图右侧，那么使用A*寻路算法会先向右侧遍历，
 * 直到右侧顶点v的预估成本（s到v的实际距离+v到t的直线距离）大于左侧某一顶点到终点的预估成本，
 * 而普通Dijkstra算法会均匀的向四周查找
 *
 * 就一对起始点的路径查找来说，使用A*寻路算法的欧几里得图效率更高，
 * 但如果想计算从起点到所有顶点的最短路径，普通Dijkstra算法效率更高
 */
class EuclideanEdgeWeightedDigraph(val V: Int, ratio: Double = 1.1) {
    private val adj = Array(V) { Bag<DirectedEdge>() }
    private val points = Array(V) { Point2D(random(), random()) }
    var E = 0
        private set
    var weight = 0.0
        private set

    init {
        // 如果两点之间的距离大于阈值，则欧几里得图几乎必定相连，参考练习4.1.41
        // ratio表示在阈值上乘以一定比率，用于控制图的稠密程度
        // 相连的任意两点之间都是双向边
        val threshold = sqrt(log2(V.toDouble()) / (PI * V)) * ratio
        for (v in 0 until V) {
            for (w in v + 1 until V) {
                if ((points[v].x() - points[w].x()).pow(2) + (points[v].y() - points[w].y()).pow(2) < threshold.pow(2)) {
                    addEdge(v, w)
                    addEdge(w, v)
                }
            }
        }
    }

    // 只需要设置相连的两个顶点，权重为两点之间的距离
    fun addEdge(v: Int, w: Int) {
        adj[v].add(DirectedEdge(v, w, points[v].distanceTo(points[w])))
        E++
    }

    fun adj(v: Int): Iterable<DirectedEdge> {
        return adj[v]
    }

    fun edges(): Iterable<DirectedEdge> {
        val queue = Queue<DirectedEdge>()
        adj.forEach { bag ->
            bag.forEach { edge ->
                queue.enqueue(edge)
            }
        }
        return queue
    }

    fun getPoint(v: Int): Point2D {
        return points[v]
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
                .append(V)
                .append(" vertices, ")
                .append(E)
                .append(" edges\n")
        edges().forEach {
            stringBuilder.append(it.toString())
                    .append("\n")
        }
        return stringBuilder.toString()
    }

    fun draw() {
        StdDraw.clear()
        drawPoint()
//        drawVertex()
        drawEdge()
    }

    private fun drawPoint() {
        StdDraw.setPenRadius(GraphGraphics.DEFAULT_POINT_RADIUS)
        StdDraw.setPenColor(StdDraw.BLACK)
        points.forEach {
            StdDraw.point(it.x(), it.y())
        }
    }

    // 标注出所有顶点
    private fun drawVertex() {
        StdDraw.setPenRadius()
        StdDraw.setPenColor(StdDraw.RED)
        points.forEachIndexed { v, point ->
            StdDraw.text(point.x(), point.y() + GraphGraphics.DEFAULT_POINT_RADIUS * 2, v.toString())
        }
    }

    private fun drawEdge() {
        StdDraw.setPenRadius(GraphGraphics.DEFAULT_EDGE_WIDTH)
        StdDraw.setPenColor(StdDraw.LIGHT_GRAY)
        edges().forEach { edge ->
            StdDraw.line(points[edge.from()].x(), points[edge.from()].y(), points[edge.to()].x(), points[edge.to()].y())
        }
    }
}

/**
 * 找到欧几里得图中从一个点到另一个点的最短路径
 */
class EuclideanEdgeWeightedDigraphSP(
        private val digraph: EuclideanEdgeWeightedDigraph,
        private val s: Int,
        private val t: Int,
        private val drawingProcess: Boolean = false,
        private val delay: Long = 1000L) {
    // 这里的距离不是从起点到顶点v的距离，而是从起点到顶点v的实际距离加上顶点v到终点的直线距离
    // 终点和自己的直线距离为0，所以distTo[t]的值为起点到终点的实际距离
    private val distTo = Array(digraph.V) { Double.POSITIVE_INFINITY }
    private val edgeTo = arrayOfNulls<DirectedEdge>(digraph.V)
    private val pq = HeapIndexMinPriorityQueue<Double>(digraph.V)

    init {
        if (drawingProcess) {
            drawVertex()
        }
        distTo[s] = distance(s, t)
        pq[s] = distTo[s]
        while (!pq.isEmpty()) {
            val v = pq.delMin().second
            if (v == t) break
            relax(v)
        }
    }

    private fun relax(v: Int) {
        digraph.adj(v).forEach { edge ->
            val w = edge.to()
            // 从起点s到顶点v的实际距离
            val actualDistV = distTo[v] - distance(v, t)
            // 从起点s到顶点w的实际距离加上顶点w到终点t的直线距离
            val distW = actualDistV + edge.weight + distance(w, t)
            if (distTo[w] > distW) {
                distTo[w] = distW
                edgeTo[w] = edge
                pq[w] = distTo[w]
            }
            if (drawingProcess) {
                drawEdge(v, w)
                sleep(delay)
            }
        }
    }

    private fun distance(v: Int, w: Int): Double {
        return digraph.getPoint(v).distanceTo(digraph.getPoint(w))
    }

    fun dist(): Double {
        return distTo[t]
    }

    fun hashPath(): Boolean {
        return dist() != Double.POSITIVE_INFINITY
    }

    fun path(): Iterable<DirectedEdge>? {
        if (!hashPath()) return null
        val stack = Stack<DirectedEdge>()
        var edge = edgeTo[t]
        while (edge != null) {
            stack.push(edge)
            edge = edgeTo[edge.from()]
        }
        return stack
    }

    private fun drawEdge(v: Int, w: Int) {
        val pointV = digraph.getPoint(v)
        val pointW = digraph.getPoint(w)
        StdDraw.setPenRadius(GraphGraphics.DEFAULT_EDGE_WIDTH * 2)
        StdDraw.setPenColor(StdDraw.BLACK)
        StdDraw.line(pointV.x(), pointV.y(), pointW.x(), pointW.y())
    }

    /**
     * 标注出起始点
     */
    private fun drawVertex() {
        StdDraw.setPenRadius()
        StdDraw.setPenColor(StdDraw.RED)
        val pointS = digraph.getPoint(s)
        val pointT = digraph.getPoint(t)
        StdDraw.text(pointS.x(), pointS.y() + GraphGraphics.DEFAULT_POINT_RADIUS * 2, s.toString())
        StdDraw.text(pointT.x(), pointT.y() + GraphGraphics.DEFAULT_POINT_RADIUS * 2, t.toString())
    }
}

fun main() {
    // 为了让绘制的图形更直观且每次的图形都相同，为随机方法设置一个种子
    setSeed(70)
    val digraph = EuclideanEdgeWeightedDigraph(250)
    digraph.draw()
    val sp = EuclideanEdgeWeightedDigraphSP(digraph, 5, 230, true, delay = 100)
    println(sp.path()?.joinToString())
}

