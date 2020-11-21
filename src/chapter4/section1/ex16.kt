package chapter4.section1

import edu.princeton.cs.algs4.In

/**
 * 顶点v的离心率是它和离它最远的顶点的最短距离。
 * 图的直径即所有顶点的最大离心率，半径为所有顶点的最小离心率，中点为离心率和半径相等的顶点。
 * 实现以下API，如表4.1.10所示。
 *
 * 解：用练习4.1.13中的distTo方法找到给定点到顶点v的最短距离，遍历所有顶点，最大值就是离心率
 * 根据离心率、直径、半径、中点的定义，图应该是一个所有顶点都连通的图
 */
class GraphProperties(val graph: Graph) {

    /**
     * v的离心率
     */
    fun eccentricity(v: Int): Int {
        val paths = DistToBreadFirstPaths(graph, v)
        var max = 0
        for (i in 0 until graph.V) {
            val distance = paths.distTo(i)
            if (distance > max) {
                max = distance
            }
        }
        return max
    }

    /**
     * G的直径
     */
    fun diameter(): Int {
        var max = 0
        for (i in 0 until graph.V) {
            val eccentricity = eccentricity(i)
            if (eccentricity > max) {
                max = eccentricity
            }
        }
        return max
    }

    /**
     * G的半径
     */
    fun radius(): Int {
        var min = Int.MAX_VALUE
        for (i in 0 until graph.V) {
            val eccentricity = eccentricity(i)
            if (eccentricity < min) {
                min = eccentricity
            }
        }
        return min
    }

    /**
     * G的某个中点
     */
    fun center(): Int {
        var min = Int.MAX_VALUE
        var minIndex = 0
        for (i in 0 until graph.V) {
            val eccentricity = eccentricity(i)
            if (eccentricity < min) {
                min = eccentricity
                minIndex = i
            }
        }
        return minIndex
    }
}

fun main() {
    val graph = Graph(In("./data/mediumG.txt"))
    val graphProperties = GraphProperties(graph)
    println("eccentricity(0)=${graphProperties.eccentricity(0)}")
    println("diameter=${graphProperties.diameter()}")
    println("radius=${graphProperties.radius()}")
    val center = graphProperties.center()
    println("center=$center")
    println("eccentricity(${center})=${graphProperties.eccentricity(center)}")
}