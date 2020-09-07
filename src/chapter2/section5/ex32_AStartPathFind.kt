package chapter2.section5

import chapter1.section3.*
import chapter2.section4.HeapMinPriorityQueue
import kotlin.math.abs

/**
 * A*寻路算法
 * 参考文章：https://hogwartsrico.github.io/2016/03/11/AStarPathFinding/index.html
 * 在线演示：http://qiao.github.io/PathFinding.js/visual/
 * A*寻路算法用于在有障碍的地图中找到两点之间的最短路径，每次移动可以向四周8个方向中的一个方向前进一步
 *
 * 算法原理直接参考上面的参考文章，这里直接给出Kotlin实现的代码，并用StdDraw绘制寻路过程
 */
class AStartPathFind(val startPoint: Point, val endPoint: Point, val barriers: Array<Point>) {
    companion object {
        const val mapSize = 20
    }

    class Point(val x: Int, val y: Int, var parent: Point? = null) : Comparable<Point> {
        init {
            check(x in 0 until mapSize && y in 0 until mapSize)
        }

        var G = -1 //从起始点移动到该点的成本
            private set
        var H = -1 //从该点移动到终点的估算成本
            private set
        val F: Int //总的成本
            get() = G + H

        /**
         * 根据父结点计算G的值，根据曼哈顿距离计算H的值
         * 曼哈顿距离等于x坐标差值的绝对值和y坐标差值的绝对值之和
         * 表示在只能水平移动或垂直移动时从一个点移动到另一个点需要移动的次数（本题中可以沿对角线移动）
         * 也可以用欧几里得距离计算（两点之间的直线距离）
         */
        fun calculateDistance(endPoint: Point) {
            //parent为空时，G=0，parent不为空时，G=parent.G + 1
            G = (parent?.G ?: -1) + 1
            H = abs(endPoint.x - x) + abs(endPoint.y - y)
        }

        //比较大小时用总成本比较
        override fun compareTo(other: Point): Int {
            require(G >= 0 && H >= 0 && other.G >= 0 && H >= 0) { "Not initialized properly" }
            return F.compareTo(other.F)
        }

        //判断点是否相等时用坐标判断，和compareTo方法的结果不一致
        override fun equals(other: Any?): Boolean {
            if (other == null) return false
            if (this === other) return true
            if (other !is Point) return false
            return x == other.x && y == other.y
        }

        override fun hashCode(): Int {
            return x + 31 * y
        }
    }

    //用于存放已经计算过总成本的点，插入和删除的时间复杂度为lgN，查询点是否存在的时间复杂度为N
    private val openPQ = HeapMinPriorityQueue<Point>()

    //用于存放已经被排除的点
    private val closeSet = HashSet<Point>()

    /**
     * 开始查找，返回途径的每个点
     * 不包括起始点，包括结束点，所以列表长度等于需要走的步数
     */
    fun find(): DoublyLinkedList<Point> {
        val list = DoublyLinkedList<Point>()
        if (startPoint == endPoint) return list
        startPoint.calculateDistance(endPoint)
        openPQ.insert(startPoint)
        while (!openPQ.isEmpty()) {
            val point = openPQ.delMin()
            if (point == endPoint) {
                list.addHeader(point)
                var parent = point.parent
                while (parent != null) {
                    list.addHeader(parent)
                    parent = parent.parent
                }
                return list
            }
            val childList = getPointsAround(point)
            childList.forEach {
                openPQ.insert(it)
            }
            closeSet.add(point)
        }
        return list
    }

    /**
     * 获取一个点周围能直接到达的点（不包括自己、障碍点、openPQ、closeSet中的点）
     * FIXME 路径不能穿过两个成对角线的障碍点
     */
    fun getPointsAround(point: Point): SinglyLinkedList<Point> {
        val list = SinglyLinkedList<Point>()
        for (i in point.x - 1..point.x + 1) {
            for (j in point.y - 1..point.y + 1) {
                //点不在有效范围内或xy坐标和自己相同直接继续循环
                if (i < 0 || i >= mapSize || j < 0 || j >= mapSize || (i == point.x && j == point.y)) continue
                val childPoint = Point(i, j, point)
                if (!openPQ.contains(childPoint)
                        && !closeSet.contains(childPoint)
                        && !isBarrier(childPoint)) {
                    childPoint.calculateDistance(endPoint)
                    list.add(childPoint)
                }
            }
        }
        return list
    }

    fun isBarrier(point: Point): Boolean {
        barriers.forEach {
            if (point == it) {
                return true
            }
        }
        return false
    }
}

fun main() {
    val startPoint = AStartPathFind.Point(5, 10)
    val endPoint = AStartPathFind.Point(15, 10)
    val list = AStartPathFind(startPoint, endPoint, getBarriers()).find()
    if (list.isEmpty()) {
        if (startPoint == endPoint) {
            println("No need to move")
        } else {
            println("Cannot move to the end point")
        }
    } else {
        println("Need to move ${list.size()} times")
        list.forwardIterator().forEach {
            print("(${it.x},${it.y}) ")
        }
    }
}

fun getBarriers(): Array<AStartPathFind.Point> {
    //默认没有障碍
//    return emptyArray<AStartPathFind.Point>()

    //和y轴的平行的障碍
//    return arrayOf(
//            AStartPathFind.Point(10, 8),
//            AStartPathFind.Point(10, 9),
//            AStartPathFind.Point(10, 10),
//            AStartPathFind.Point(10, 11),
//            AStartPathFind.Point(10, 12)
//    )

    //包围住起始点的障碍
//    return arrayOf(
//            AStartPathFind.Point(4, 9),
//            AStartPathFind.Point(4, 10),
//            AStartPathFind.Point(4, 11),
//            AStartPathFind.Point(5, 11),
//            AStartPathFind.Point(6, 11),
//            AStartPathFind.Point(6, 10),
//            AStartPathFind.Point(6, 9),
//            AStartPathFind.Point(5, 9)
//    )

    //半包围起始点的障碍
    return arrayOf(
            AStartPathFind.Point(3, 8),
            AStartPathFind.Point(4, 8),
            AStartPathFind.Point(5, 8),
            AStartPathFind.Point(6, 8),
            AStartPathFind.Point(7, 8),
            AStartPathFind.Point(8, 8),
            AStartPathFind.Point(8, 9),
            AStartPathFind.Point(8, 10),
            AStartPathFind.Point(8, 11),
            AStartPathFind.Point(8, 12),
            AStartPathFind.Point(7, 12),
            AStartPathFind.Point(6, 12),
            AStartPathFind.Point(5, 12),
            AStartPathFind.Point(4, 12),
            AStartPathFind.Point(3, 12)
    )
}

