package chapter4.section1

import chapter3.section5.LinearProbingHashSET
import edu.princeton.cs.algs4.Queue
import extensions.spendTimeMillis

/**
 * 计算由movies.txt得到的图的连通分量的数量和包含的顶点数小于10的连通分量的数量。
 * 计算最大的连通分量的离心率、直径、半径和中点。
 * Kevin Bacon在最大的连通分量之中吗？
 *
 * 解：使用[SymbolGraph]处理movies.txt文件，得到无向图
 * 使用[BreadthFirstCC]计算连通分量的数量，不能用深度优先搜索，因为栈太深会溢出，
 * 遍历图的每个顶点，记录每个连通分量的数量和其中一个顶点
 * 获取图中Kevin Bacon对应的顶点，再判断该顶点和最大连通分量中的代表值是否连通
 *
 * 计算最大连通分量的离心率等数据时，需要先找到最大分量的一个顶点，再从该顶点开始进行广度优先搜索，
 * 在广度优先搜索的过程中，在一个新的图中插入边，构造新的图，用新构造的图计算离心率等数据
 */
fun main() {
    val sg = SymbolGraph("./data/movies.txt", "/")
    val graph = sg.G()
    val cc = BreadthFirstCC(graph)
    val ccArray = Array(cc.count()) { MoviesCC(it, 0, 0) }
    for (i in 0 until graph.V) {
        val id = cc.id(i)
        val moviesCC = ccArray[id]
        if (moviesCC.count == 0) {
            moviesCC.example = i
        }
        moviesCC.count++
    }
    ccArray.sortBy { it.count }

    val less10Size = ccArray.count { it.count < 10 }
    println("Number of CC less than 10 : $less10Size")

    println("Is Kevin Bacon among the largest CC : ${cc.connected(sg.index("Bacon, Kevin"), ccArray.last().example)}")

    val maxCC = ccArray.last()
    val newGraph = getGraphByCC(graph, maxCC)
    // 下面的操作非常慢，时间复杂度为O(N*(N+E))
    val time = spendTimeMillis {
        val graphProperties = GraphProperties(newGraph)
        // “计算最大的连通分量的离心率”这句话不准确，每个顶点都有自己的离心率，但图没有离心率，只有直径、半径、中点等参数
        // 这里随机计算一个顶点的离心率
        println("Eccentricity of the largest CC : ${graphProperties.eccentricity(maxCC.id)}")
        println("Diameter : ${graphProperties.diameter()}")
        println("Radius : ${graphProperties.radius()}")
        println("Center : ${graphProperties.center()}")
    }
    println("spend $time ms")
}

private class MoviesCC(val id: Int, var count: Int, var example: Int)

/**
 * 根据一个不连通图[graph]中的一个连通分量[cc]，生成一个连通的图
 */
private fun getGraphByCC(graph: Graph, cc: MoviesCC): Graph {
    val newGraph = Graph(cc.count)
    val marked = BooleanArray(graph.V)
    val indexMapping = IntArray(graph.V)
    val set = LinearProbingHashSET<Edge>()
    var i = 0
    val queue = Queue<Int>()
    indexMapping[cc.example] = i
    marked[cc.example] = true
    queue.enqueue(cc.example)
    i++

    while (!queue.isEmpty) {
        val v = queue.dequeue()
        graph.adj(v).forEach { w ->
            if (!marked[w]) {
                marked[w] = true
                queue.enqueue(w)
                indexMapping[w] = i
                i++
            }
            // 利用集合对边去重
            val edge = Edge(indexMapping[v], indexMapping[w])
            if (!set.contains(edge)) {
                set.add(edge)
            }
        }
    }
    set.keys().forEach { edge ->
        newGraph.addEdge(edge.small, edge.large)
    }
    return newGraph
}