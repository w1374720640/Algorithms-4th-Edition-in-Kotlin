package chapter4.section4

import chapter1.section3.DoublyLinkedList
import chapter1.section3.addTail
import chapter1.section3.forwardIterator
import chapter2.section4.HeapMinPriorityQueue
import chapter3.section4.LinearProbingHashST
import chapter3.section5.LinearProbingHashSET
import chapter4.section3.drawEWGGraph
import chapter4.section3.getRandomEWG
import edu.princeton.cs.algs4.Queue
import extensions.formatDouble
import extensions.setSeed
import kotlin.math.PI
import kotlin.math.log2
import kotlin.math.sqrt

/**
 * 实现DijkstraSP的另一个版本，支持一个方法来返回一幅加权有向图中从s到t的另一条最短路径。
 * （如果从s到t的最短路径只有一条则返回null。）
 *
 * 解：这里题目稍作修改：求一个有向图中，从s到t的前k短路径
 * Path类型表示一条路径，使用一个最小优先队列保存每条路径，不使用relax放松顶点
 * 如果一个顶点有n个有效指出边（不包含自环边和指向顶点已访问的边），则路径的总数增加n
 * 如果一个顶点有效指出边为0，则包含该顶点的路径无法到达目标点
 * 不断从最小优先队列中取出路径，直到找到k条从s到t的最短路径
 * 和书中实现的Dijkstra算法相比，只遍历了图的部分边，没有全部遍历，空间复杂度较大
 */
class DijkstraKShortPath(digraph: EdgeWeightedDigraph, s: Int, t: Int, val k: Int) {
    class Path(edge: DirectedEdge) : Comparable<Path>, Iterable<DirectedEdge> {
        private val vertexSet = LinearProbingHashSET<Int>()
        private val list = DoublyLinkedList<DirectedEdge>()
        var weight = 0.0
            private set

        init {
            list.addTail(edge)
            vertexSet.add(edge.from())
            vertexSet.add(edge.to())
            weight += edge.weight
        }

        fun containVertex(v: Int): Boolean {
            return vertexSet.contains(v)
        }

        fun getLastVertex(): Int {
            return list.last!!.item.to()
        }

        fun addEdge(edge: DirectedEdge) {
            require(getLastVertex() == edge.from())
            list.addTail(edge)
            vertexSet.add(edge.to())
            weight += edge.weight
        }

        override fun iterator(): Iterator<DirectedEdge> {
            return list.forwardIterator()
        }

        override fun compareTo(other: Path): Int {
            return weight.compareTo(other.weight)
        }

        /**
         * 深度拷贝，返回一个新路径
         */
        fun copy(): Path {
            val iterator = iterator()
            val path = Path(iterator.next())
            while (iterator.hasNext()) {
                path.addEdge(iterator.next())
            }
            return path
        }
    }

    private val pq = HeapMinPriorityQueue<Path>()
    private val result = Queue<Path>()
    private val countST = LinearProbingHashST<Int, Int>()

    init {
        require(s in 0 until digraph.V && t in 0 until digraph.V && s != t && k > 0)

        digraph.adj(s).forEach { edge ->
            val path = Path(edge)
            pq.insert(path)
            countST.put(edge.to(), 1)
        }
        while (!pq.isEmpty()) {
            val path = pq.delMin()
            val v = path.getLastVertex()
            val count = (countST.get(v) ?: 0) + 1
            countST.put(v, count)

            if (v == t) {
                result.enqueue(path)
                if (result.size() == k) break
            }
            if (count > k) continue
            val iterator = digraph.adj(v).iterator()
            while (iterator.hasNext()) {
                val edge = iterator.next()
                val w = edge.to()
                // 不走回头路
                if (path.containVertex(w)) continue

                val newPath = path.copy()
                newPath.addEdge(edge)
                pq.insert(newPath)
            }

        }
    }

    /**
     * 获取前k短的路径
     */
    fun getPaths(): Iterable<Path> {
        return result
    }
}

fun main() {
    println("tinyEWD.txt:")
    val digraph = getTinyEWD()
    val sp = DijkstraKShortPath(digraph, 2, 4, 2)
    val paths = sp.getPaths()
    paths.forEachIndexed { index, path ->
        println("index=$index weight=${formatDouble(path.weight, 3)} ${path.joinToString()}")
    }
    println()

    println("euclidean digraph:")
    // 保持每次的运行结果一致
    setSeed(10)
    val euclideanV = 20
    // 欧几里得图的阈值，大于阈值的图几乎必然连通，使用时阈值乘以一定倍数
    val threshold = sqrt(log2(euclideanV.toDouble()) / (PI * euclideanV))
    val pair = getRandomEWG(euclideanV, threshold * 1.6)
    val euclideanEWG = pair.first
    val euclideanDigraph = EdgeWeightedDigraph(euclideanEWG.V)
    // 将加权无向图的每条边转换成两条反向的加权有向边，加权无向图转换为加权有向图
    euclideanEWG.edges().forEach { edge ->
        euclideanDigraph.addEdge(DirectedEdge(edge.v, edge.w, edge.weight))
        euclideanDigraph.addEdge(DirectedEdge(edge.w, edge.v, edge.weight))
    }
    // 获取从顶点0到顶点10最短的5条路径
    val euclideanSP = DijkstraKShortPath(euclideanDigraph, 0, 10, 5)
    val euclideanPaths = euclideanSP.getPaths()
    euclideanPaths.forEachIndexed { index, path ->
        println("index=$index weight=${formatDouble(path.weight, 3)} ${path.joinToString()}")
    }
    println()
    drawEWGGraph(euclideanEWG, points = pair.second, showIndex = true)

    // 稠密图测试性能
//    println("dense digraph:")
//    val denseV = 1000
//    val denseEWG = getDenseGraph(denseV) // E=V*(V-1)/2的稠密图
//    val denseDigraph = EdgeWeightedDigraph(denseEWG.V)
//    denseEWG.edges().forEach { edge ->
//        denseDigraph.addEdge(DirectedEdge(edge.v, edge.w, edge.weight))
//        denseDigraph.addEdge(DirectedEdge(edge.w, edge.v, edge.weight))
//    }
//    val time = spendTimeMillis {
//        val denseSP = DijkstraKShortPath(denseDigraph, 0, 10, 2)
//        val densePaths = denseSP.getPaths()
//        densePaths.forEachIndexed { index, path ->
//            println("index=$index weight=${formatDouble(path.weight, 3)} ${path.joinToString()}")
//        }
//    }
//    println("denseV=$denseV time=$time ms")
}