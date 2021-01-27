package chapter4.section4

import chapter2.section4.HeapIndexMinPriorityQueue
import edu.princeton.cs.algs4.Queue
import edu.princeton.cs.algs4.Stack
import extensions.formatDouble

/**
 * 双向搜索
 * 基于算法4.9的代码为给定两点的最短路径问题实现一个类，但在初始化时将起点和终点都加入优先队列。
 * 这么做会使最短路径树从两个顶点同时开始生长，你的主要任务是决定两棵树相遇时应该怎么办。
 *
 * 解：算法4.9为最短路径的Dijkstra算法
 * 根据给定的有向图创建一个反向图
 * 创建两个大小为V的marked数组，用于表示需要放松的点在起点的搜索树中还是在终点的搜索树中，
 * 将marked数组A中起点的位置设为true，marked数组B中终点的位置设为true，
 * distTo数组中两个点的位置都设置为0.0，将两个点放入最小索引优先队列中。
 * 当从优先队列中取出一个值准备放松时，先判断这个顶点在数组A中对应的位置为true还是数组B中对应的位置为true，
 * 如果在数组A中对应位置的值为true，以原始有向图为数据源进行放松操作，
 * 如果在数组B中对应位置的值为true，以反向图为数据源进行放松操作，
 * 放松时，如果顶点v指向的顶点w在另一个数组中存在，则表示找到最短路径，
 * 搜索结束，拼接出完整路径，所以一个顶点不可能在数组A和数组B中对应位置的值同时为true。
 * 使用一个队列保存最短路径，如果最后一条边为v->w，
 * 先将起点s到v的路径放入栈中，再依次取出放入队列，然后将v->w边放入队列，最后将w到终点t的路径依次放入队列。
 */
class BidirectionalSearchSP(private val digraph: EdgeWeightedDigraph, s: Int, t: Int) {
    private val distTo = Array(digraph.V) { Double.POSITIVE_INFINITY }
    private val edgeTo = arrayOfNulls<DirectedEdge>(digraph.V)
    private val pq = HeapIndexMinPriorityQueue<Double>(digraph.V)
    private val marked = BooleanArray(digraph.V)
    private val reverseMarked = BooleanArray(digraph.V)
    private val reverseDigraph = object : EdgeWeightedDigraph(digraph.V) {
        override fun addEdge(edge: DirectedEdge) {
            // 将边按终点索引，可以根据终点查找所有指向该点的边
            adj[edge.to()].add(edge)
            E++
        }
    }

    private var dist: Double = Double.POSITIVE_INFINITY
    private var path: Iterable<DirectedEdge>? = null

    init {
        if (s == t) {
            dist = 0.0
            path = Queue()
        } else {
            digraph.edges().forEach { reverseDigraph.addEdge(it) }

            distTo[s] = 0.0
            distTo[t] = 0.0
            pq[s] = 0.0
            pq[t] = 0.0
            marked[s] = true
            reverseMarked[t] = true
            while (!pq.isEmpty() && !hasPath()) {
                val v = pq.delMin().second
                // 一个顶点不能同时被两个搜索树标记，同时存在于两颗树中时表示已找到最短路径，不会继续查找
                check(!(marked[v] && reverseMarked[v]))
                if (marked[v]) {
                    relax(v)
                } else {
                    reverseRelax(v)
                }
            }
        }
    }

    private fun relax(v: Int) {
        digraph.adj(v).forEach { edge ->
            val w = edge.to()
            if (reverseMarked[w]) {
                getShortestPath(edge)
                return
            }
            if (distTo[w] > distTo[v] + edge.weight) {
                distTo[w] = distTo[v] + edge.weight
                edgeTo[w] = edge
                pq[w] = distTo[w]
                marked[w] = true
            }
        }
    }

    private fun reverseRelax(w: Int) {
        // 反向图的放松过程和原始有向图相反
        reverseDigraph.adj(w).forEach { edge ->
            val v = edge.from()
            if (marked[v]) {
                getShortestPath(edge)
                return
            }
            if (distTo[v] > distTo[w] + edge.weight) {
                distTo[v] = distTo[w] + edge.weight
                edgeTo[v] = edge
                pq[v] = distTo[v]
                reverseMarked[v] = true
            }
        }
    }

    /**
     * 生成最终的最短路径
     */
    private fun getShortestPath(edge: DirectedEdge) {
        val queue = Queue<DirectedEdge>()
        dist = 0.0
        // 将起点s到顶点edge.from()的路径添加到最短路径中
        val stack = Stack<DirectedEdge>()
        var tempEdge = edgeTo[edge.from()]
        while (tempEdge != null) {
            dist += tempEdge.weight
            stack.push(tempEdge)
            tempEdge = edgeTo[tempEdge.from()]
        }
        stack.forEach { queue.enqueue(it) }

        // 将edge添加到最短路径中
        dist += edge.weight
        queue.enqueue(edge)

        // 将顶点edge.to()到终点t的路径添加到最短路径中
        tempEdge = edgeTo[edge.to()]
        while (tempEdge != null) {
            dist += tempEdge.weight
            queue.enqueue(tempEdge)
            tempEdge = edgeTo[tempEdge.to()]
        }
        path = queue
    }

    fun dist(): Double {
        return dist
    }

    fun hasPath(): Boolean {
        return dist() != Double.POSITIVE_INFINITY
    }

    fun path(): Iterable<DirectedEdge>? {
        return path
    }
}

fun main() {
    val digraph = getTinyEWDAG()
    for (s in 0 until digraph.V) {
        for (t in 0 until digraph.V) {
            val sp = BidirectionalSearchSP(digraph, s, t)
            println("s:$s t:$t dist=${formatDouble(sp.dist(), 2)} path=${sp.path()?.joinToString()}")
        }
    }
}