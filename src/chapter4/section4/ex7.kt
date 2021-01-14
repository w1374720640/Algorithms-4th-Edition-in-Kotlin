package chapter4.section4

import chapter1.section3.DoublyLinkedList
import chapter1.section3.addTail
import chapter1.section3.forwardIterator
import chapter2.section4.HeapMaxPriorityQueue
import chapter3.section5.LinearProbingHashSET
import chapter4.section3.drawEWGGraph
import chapter4.section3.getRandomEWG
import edu.princeton.cs.algs4.Queue
import edu.princeton.cs.algs4.Stack
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
 * Path类型表示一条路径，从起点开始进行广度优先搜索
 * 使用一个队列保存广度优先遍历过程中的路径
 * 如果一个顶点有n个有效指出边（不包含自环边和指向顶点已访问的边），则路径的总数增加n
 * 如果一个顶点有效指出边为0，则包含该顶点的路径无法到达目标点
 * 使用一个最大优先队列保存最短的k条路径（控制优先队列大小小于等于k）
 * 不断从队列中取出路径，判断需要继续查找还是丢弃路径
 * 为了防止路径重复，使用一个大小为V的最大优先队列数组，
 * 当需要添加一条新边时，只有当前路径加新边的权重小于经过该点的前k短路径时才加入队列中，否则丢弃
 *
 * 未证明算法正确性，算法复杂度未知，而且和Dijkstra算法无关...
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

    private val queue = Queue<Path>()
    private val result = HeapMaxPriorityQueue<Path>()
    private val array = Array(digraph.V) { HeapMaxPriorityQueue<Path>() }
    private val deletePathSet = LinearProbingHashSET<Path>()

    init {
        require(s in 0 until digraph.V && t in 0 until digraph.V && s != t && k > 0)

        digraph.adj(s).forEach { edge ->
            val path = Path(edge)
            queue.enqueue(path)
            array[edge.to()].insert(path)
        }
        while (!queue.isEmpty) {
            val path = queue.dequeue()
            val v = path.getLastVertex()
            if (v == t) {
                result.insert(path)
                if (result.size() > k) {
                    val deletePath = result.delMax()
                    if (deletePathSet.contains(deletePath)) {
                        deletePathSet.delete(deletePath)
                        continue
                    }
                }
                continue
            }
            // 如果已经有k条比该路径更优的路径（到达某个点的权重更小），则该路径直接丢弃
            if (deletePathSet.contains(path)) {
                deletePathSet.delete(path)
                continue
            }
            val iterator = digraph.adj(v).iterator()
            while (iterator.hasNext()) {
                val edge = iterator.next()
                val w = edge.to()
                // 不走回头路
                if (path.containVertex(w)) continue

                val maxPQ = array[w]
                check(maxPQ.size() <= k)
                if (maxPQ.size() == k) {
                    // 如果已经有k个到该结点的更短路径，则该路径不通
                    if (maxPQ.max().weight <= path.weight + edge.weight) continue
                    // 如果当前路径更短，删除最大的那个，并在下次从队列中取出时跳过
                    deletePathSet.add(maxPQ.delMax())
                }
                val newPath = path.copy()
                newPath.addEdge(edge)
                queue.enqueue(newPath)
                maxPQ.insert(newPath)
            }

        }
    }

    /**
     * 获取前k短的路径
     */
    fun getPaths(): Iterable<Path> {
        val stack = Stack<Path>()
        var i = 0
        while (!result.isEmpty() && i++ < k) {
            stack.push(result.delMax())
        }
        return stack
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