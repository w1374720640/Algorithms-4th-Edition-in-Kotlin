package chapter4.section4

import extensions.formatDouble
import extensions.formatInt
import extensions.random

/**
 * 网格图中的最短路径
 * 给定一个N*N的正整数矩阵，找到从(0,0)到(N-1,N-1)的最短路径，路径的长度即为路径中所有正整数之和。
 * 在只能向右和向下移动的限制下重新解答这个问题。
 *
 * 解：为矩阵的每个顶点随机生成一个正整数值，为每个顶点添加指出边，边的权重等于被指向顶点在矩阵中的值，
 * 构造一个加权有向图，使用Dijkstra算法找出最短路径，路径的长度为边的权重之和再加上起点的值
 */
class GridDigraph(val N: Int, limitDirection: Boolean = false) : EdgeWeightedDigraph(N * N) {
    private val array = Array(N) { Array(N) { random(100) } }

    init {
        for (i in 0 until N * N) {
            val x = i / N
            val y = i % N
            if (!limitDirection) {
                // 和正上方顶点相连
                if (x > 0) addEdge(DirectedEdge(i, (x - 1) * N + y, array[x - 1][y].toDouble()))
                // 和左侧顶点相连
                if (y > 0) addEdge(DirectedEdge(i, x * N + y - 1, array[x][y - 1].toDouble()))
            }
            // 和正下方顶点相连
            if (x < N - 1) addEdge(DirectedEdge(i, (x + 1) * N + y, array[x + 1][y].toDouble()))
            // 和右侧顶点相连
            if (y < N - 1) addEdge(DirectedEdge(i, x * N + y + 1, array[x][y + 1].toDouble()))
        }
    }

    fun getValue(v: Int): Int {
        require(v in 0 until N * N)
        return array[v / N][v % N]
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
                .append("N=")
                .append(N)
                .append("\n")
        repeat(N) { x ->
            repeat(N) { y ->
                stringBuilder.append(formatInt(x * N + y, 3))
                        .append(":")
                        .append(formatInt(array[x][y], 2))
            }
            stringBuilder.append("\n")
        }
        return stringBuilder.toString()
    }
}

class GridDijkstraSP(private val gridDigraph: GridDigraph, s: Int) : SP(gridDigraph, s) {
    private val sp = DijkstraSP(gridDigraph, s)

    override fun distTo(v: Int): Double {
        return sp.distTo(v) + gridDigraph.getValue(s)
    }

    override fun hasPathTo(v: Int): Boolean {
        return sp.hasPathTo(v)
    }

    override fun pathTo(v: Int): Iterable<DirectedEdge>? {
        return sp.pathTo(v)
    }
}

fun main() {
    val N = 5
    val s = 6
    val t = 19
    val gridDigraph = GridDigraph(N)
    println(gridDigraph)
    val sp = GridDijkstraSP(gridDigraph, s)
    println(sp.pathTo(t)?.joinToString(prefix = "dist=${formatDouble(sp.distTo(t), 2)} "))
}