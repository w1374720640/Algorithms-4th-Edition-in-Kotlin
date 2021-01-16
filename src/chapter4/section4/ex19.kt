package chapter4.section4

import chapter1.section3.DoublyLinkedList
import chapter1.section3.addTail
import chapter1.section3.forwardIterator
import chapter3.section5.LinearProbingHashSET
import edu.princeton.cs.algs4.In
import edu.princeton.cs.algs4.Queue
import extensions.formatDouble
import kotlin.math.ln

/**
 * 找出正文中的例子里权重最低的环（即最佳套汇机会）。
 *
 * 解：参考练习4.4.7，找出权重最低的环
 * LowestWeightCycle用于找出在一个加权有向图中包含顶点s的权重最低环
 */
class MinWeightCycleFinder(digraph: EdgeWeightedDigraph, s: Int) {
    class Path(edge: DirectedEdge) : Comparable<Path>, Iterable<DirectedEdge> {
        private val vertexSet = LinearProbingHashSET<Int>()
        private val list = DoublyLinkedList<DirectedEdge>()
        var weight = 0.0
            private set

        init {
            list.addTail(edge)
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
    private var result: Path? = null
    private val array = arrayOfNulls<Path>(digraph.V)
    private val deletePathSet = LinearProbingHashSET<Path>()

    init {
        require(s in 0 until digraph.V)

        digraph.adj(s).forEach { edge ->
            val path = Path(edge)
            queue.enqueue(path)
            array[edge.to()] = path
        }
        while (!queue.isEmpty) {
            val path = queue.dequeue()
            // 如果已经有k条比该路径更优的路径（到达某个点的权重更小），则该路径直接丢弃
            if (deletePathSet.contains(path)) {
                deletePathSet.delete(path)
                continue
            }
            val v = path.getLastVertex()
            if (v == s) {
                if (result == null || path < result!!) {
                    if (result != null && deletePathSet.contains(result!!)) {
                        deletePathSet.delete(result!!)
                        continue
                    }
                    result = path
                }
                continue
            }
            val iterator = digraph.adj(v).iterator()
            while (iterator.hasNext()) {
                val edge = iterator.next()
                val w = edge.to()
                // 不走回头路
                if (path.containVertex(w)) continue

                val oldPath = array[w]
                if (oldPath == null || path.weight + edge.weight < oldPath.weight) {
                    if (oldPath != null) {
                        deletePathSet.add(oldPath)
                    }
                    val newPath = path.copy()
                    newPath.addEdge(edge)
                    queue.enqueue(newPath)
                    array[w] = newPath
                }
            }

        }
    }

    /**
     * 获取权重最小的环
     */
    fun getPath(): Path? {
        return result
    }
}

fun getRates(): Pair<Array<String>, EdgeWeightedDigraph> {
    val input = In("./data/rates.txt")
    val V = input.readInt()
    val names = Array(V) { "" }
    val digraph = EdgeWeightedDigraph(V)
    repeat(V) { v ->
        names[v] = input.readString()
        repeat(V) { w ->
            val ratio = input.readDouble()
            digraph.addEdge(DirectedEdge(v, w, -1.0 * ln(ratio)))
        }
    }
    return names to digraph
}

fun main() {
    val rates = getRates()
    val names = rates.first
    val digraph = rates.second
    var minPath: MinWeightCycleFinder.Path? = null
    for (s in 0 until digraph.V) {
        val finder = MinWeightCycleFinder(digraph, s)
        val path = finder.getPath()
        if (path != null) {
            println("s: $s weight:${formatDouble(path.weight, 5)}  path: ${path.joinToString()}")
            if (minPath == null || path < minPath) {
                minPath = path
            }
        }
    }
    if (minPath == null) {
        println("Does not contain cycle")
    } else {
        val iterator = minPath.iterator()
        val firstEdge = iterator.next()
        val queue = Queue<Int>()
        queue.enqueue(firstEdge.from())
        queue.enqueue(firstEdge.to())
        while (iterator.hasNext()) {
            val edge = iterator.next()
            queue.enqueue(edge.to())
        }
        println("minWeight=${formatDouble(minPath.weight, 5)} minPath: ${minPath.joinToString()}")
        println("minWeightCycle: ${queue.joinToString { names[it] }}")
    }
}