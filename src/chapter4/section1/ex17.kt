package chapter4.section1

import chapter2.sleep
import edu.princeton.cs.algs4.Queue

/**
 * 图的周长为图中最短环的长度。如果是无环图，则它的周长为无穷大。
 * 为GraphProperties添加一个方法girth()，返回图的周长。
 * 提示：在每个顶点都进行广度优先搜索。
 * 含有s的最小环为s到某个顶点v的最短路径加上从v返回到s的边。
 *
 * 解：遍历图的所有顶点，对每个顶点进行广度优先遍历
 * 对顶点s进行广度优先遍历时，如果w的相邻顶点v已经被标记，且不是顶点w的父顶点，则含有顶点s的最小环同时包含顶点v
 * 环的大小为s到w的最短距离加s到v的最短距离再加一
 */
fun GraphProperties.girth(): Int {
    val marked = BooleanArray(graph.V)
    val edgeTo = IntArray(graph.V)
    val distTo = IntArray(graph.V)
    val init = {
        for (i in marked.indices) marked[i] = false
        for (i in edgeTo.indices) edgeTo[i] = 0
        for (i in distTo.indices) distTo[i] = 0
    }

    val getMinCycleSize: (Int) -> Int = { s ->
        var minSize = Int.MAX_VALUE
        init()
        val queue = Queue<Int>()
        marked[s] = true
        edgeTo[s] = s
        queue.enqueue(s)
        label@ while (!queue.isEmpty) {
            val w = queue.dequeue()
            val iterator = graph.adj(w).iterator()
            while (iterator.hasNext()) {
                val v = iterator.next()
                if (!marked[v]) {
                    marked[v] = true
                    edgeTo[v] = w
                    distTo[v] = distTo[w] + 1
                    queue.enqueue(v)
                } else if (v != edgeTo[w]) {
                    minSize = distTo[w] + distTo[v] + 1
                    break@label
                }
            }
        }
        minSize
    }

    var minSize = Int.MAX_VALUE
    for (i in 0 until graph.V) {
        val size = getMinCycleSize(i)
        if (size < minSize) {
            minSize = size
        }
    }
    return minSize
}

fun main() {
    testGraphProperties(getGraph1())
    sleep(5000)
    testGraphProperties(getGraph2())
    sleep(5000)
    testGraphProperties(getGraph3())
}

private fun testGraphProperties(graph: Graph) {
    val graphProperties = GraphProperties(graph)
    val girth = graphProperties.girth()
    if (girth == Int.MAX_VALUE) {
        println("No circle")
    } else {
        println("girth=${graphProperties.girth()}")
    }
    drawGraph(graph)
}

private fun getGraph1(): Graph {
    val graph = Graph(10)
    graph.addEdge(0, 1)
    graph.addEdge(1, 2)
    graph.addEdge(2, 3)
    graph.addEdge(3, 4)
    graph.addEdge(4, 5)
    graph.addEdge(5, 6)
    graph.addEdge(6, 7)
    graph.addEdge(7, 8)
    graph.addEdge(8, 9)
    return graph
}

private fun getGraph2(): Graph {
    val graph = getGraph1()
    graph.addEdge(9, 0)
    return graph
}

private fun getGraph3(): Graph {
    val graph = getGraph2()
    graph.addEdge(0, 4)
    graph.addEdge(4, 7)
    return graph
}