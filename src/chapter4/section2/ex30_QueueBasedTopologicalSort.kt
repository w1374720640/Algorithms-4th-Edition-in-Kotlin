package chapter4.section2

import edu.princeton.cs.algs4.Queue

/**
 * 基于队列的拓扑排序
 * 实现一种拓扑排序，使用由顶点索引的数组来保存每个顶点的入度。
 * 遍历一遍所有边并使用练习4.2.7给出的Degrees类来初始化数组以及一条含有所有起点的队列。
 * 然后，重复以下操作直到起点队列为空：
 * 1、从队列中删去一个起点并将其标记
 * 2、遍历由被删除顶点指出的所有边，将所有被指向的顶点的入度减一
 * 3、如果顶点的入度变为0，将它插入起点队列
 */
class QueueBasedTopologicalSort(digraph: Digraph) {
    private var order: Queue<Int>? = null

    init {
        val cycle = DirectedCycle(digraph)
        // 先判断是否有环
        if (!cycle.hasCycle()) {
            val degrees = Degrees(digraph)
            val inDegrees = IntArray(digraph.V)
            val startQueue = Queue<Int>()
            for (i in 0 until digraph.V) {
                inDegrees[i] = degrees.inDegree(i)
                if (degrees.inDegree(i) == 0) {
                    startQueue.enqueue(i)
                }
            }

            while (!startQueue.isEmpty) {
                // 从起点队列中删除一个起点
                val s = startQueue.dequeue()
                if (order == null) {
                    order = Queue<Int>()
                }
                order!!.enqueue(s)

                // 与起点s直接相连的顶点v入度减一
                digraph.adj(s).forEach { v ->
                    inDegrees[v]--
                    if (inDegrees[v] == 0) {
                        startQueue.enqueue(v)
                    }
                }
            }
        }
    }


    /**
     * G是有向无环图吗
     */
    fun isDAG(): Boolean {
        return order != null
    }

    /**
     * 拓扑有序的所有顶点
     */
    fun order(): Iterable<Int>? {
        return order
    }
}

fun main() {
    val symbolDigraph = getJobsSymbolDigraph()
    val digraph = symbolDigraph.G()
    val topological = QueueBasedTopologicalSort(digraph)
    if (topological.isDAG()) {
        topological.order()!!.forEach {
            println(symbolDigraph.name(it))
        }
    }
}