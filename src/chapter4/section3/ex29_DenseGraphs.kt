package chapter4.section3

import extensions.random
import extensions.spendTimeMillis

/**
 * 稠密图
 * 实现另一个版本的Prim算法，即时（但不适用优先队列）且能够在V²次加权边比较之内得到最小生成树
 *
 * 解：从顶点0开始遍历，获取顶点v的所有的邻接边，并将marked数组对应位置设置为true，表示已加入最小生成树
 * 边的另外一个顶点为w，当顶点w未被标记时，判断该边和edgeTo数组中的边的大小，新的边较小时，更新edgeTo中的w索引位置的边
 * 遍历所有不在最小生成树中的顶点，找出edgeTo中值最小的顶点，重复以上步骤，直到所有边都在最小生成树中
 * 每个顶点最多有V-1条边，所以获取所有邻接边的时间复杂度最坏为O(V)，判断是否标记、比较大小、更新边的时间复杂度为O(1)
 * 找到edgeTo中值最小的顶点时间复杂度为O(V)，上述操作执行V次，时间复杂度为O(V)
 * 所以，总时间复杂度为(O(V) + O(V)) * O(V) = O(V²)
 * 当面对稠密图时，E约等于V²，基于优先队列的即时Prim算法的时间复杂度ElogV约等于V²lgV，大于不使用优先队列的最小生成树算法
 */
class DenseGraphPrimMST(graph: EWG) : MST() {
    private val marked = BooleanArray(graph.V)
    private val edgeTo = Array<Edge?>(graph.V) { null }

    init {
        visit(graph, 0)
        repeat(graph.V - 1) {
            var minVertex = -1
            var minEdge: Edge? = null
            for (v in 0 until graph.V) {
                if (!marked[v] && edgeTo[v] != null) {
                    if (minVertex == -1 || edgeTo[v]!! < minEdge!!) {
                        minVertex = v
                        minEdge = edgeTo[v]
                    }
                }
            }
            visit(graph, minVertex)
        }
    }

    private fun visit(graph: EWG, v: Int) {
        marked[v] = true
        val minEdge = edgeTo[v]
        minEdge?.let {
            queue.enqueue(it)
            weight += it.weight
        }
        graph.adj(v).forEach { edge ->
            val w = edge.other(v)
            if (!marked[w]) {
                val oldEdge = edgeTo[w]
                if (oldEdge == null || edge < oldEdge) {
                    edgeTo[w] = edge
                }
            }
        }
    }
}

/**
 * 生成稠密图
 */
fun getDenseGraph(V: Int): EWG {
    val graph = EdgeWeightedGraph(V)
    for (v in 0 until V) {
        for (w in v + 1 until V) {
            graph.addEdge(Edge(v, w, random()))
        }
    }
    return graph
}

fun main() {
    // 验证算法正确性
    val graph = getTinyWeightedGraph()
    val primMST = DenseGraphPrimMST(graph)
    println(primMST.toString())

    // 比较使用优先队列和未使用优先队列的两种算法在处理稠密图时的性能差距
    val V = 5000
    val denseGraph = getDenseGraph(V)
    val time1 = spendTimeMillis {
        PrimMST(denseGraph)
    }
    println("time1=$time1 ms")
    val time2 = spendTimeMillis {
        DenseGraphPrimMST(denseGraph)
    }
    println("time2=$time2 ms")

    // 绘制最小生成树生长过程
    drawRandomEWG { PrimMST(it) }
}
