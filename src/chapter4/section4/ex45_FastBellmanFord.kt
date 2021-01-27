package chapter4.section4

import chapter1.section3.*
import edu.princeton.cs.algs4.Stack

/**
 * 快速Bellman-Ford算法
 * 对于边的权重为整数且绝对值不大于某个常数的特殊情况，
 * 给出一个解决一般的加权有向图中的单点最短路径问题的算法，其所需的运行时间低于EV级别。
 *
 * 解：遍历所有边，将所有权重为正的边相加得到路径可能的最大权重x，所有权重为负的边相加得到路径可能的最小权重y
 * 所有可能的权重总数k=x-y+1 （简单路径不包含环，所以权重必定在[y,x]之间）
 * 创建一个大小为k的数组，数组内存放单向链表，链表内存放顶点，
 * 起点的权重为0，存放在数组索引为|y|的链表中，从起点开始遍历，
 * 遍历时将该顶点按距离大小放到数组相应位置，并记录距离最小的顶点，
 * 下次遍历结束时，从返回的距离最小的顶点开始查找或从上次查找顶点向后查找
 *
 * 参考：https://github.com/reneargento/algorithms-sedgewick-wayne/blob/master/src/chapter4/section4/Exercise45_FastBellmanFord.java
 * 时间复杂度为：O(E + k * V)
 */
class FastBellmanFordSP(private val digraph: EdgeWeightedDigraph, private val s: Int) {
    private val edgeTo = arrayOfNulls<DirectedEdge>(digraph.V)
    private val distTo = Array(digraph.V) { Int.MAX_VALUE }
    private val array: Array<DoublyLinkedList<Int>>
    private var cost = 0 // relax被调用的次数
    private var cycle: Iterable<DirectedEdge>? = null // 负权重环

    private var x = 0 // 可能的最大权重
    private var y = 0 // 可能的最小权重

    init {
        digraph.edges().forEach {
            val weight = it.weight.toInt()
            if (weight >= 0) {
                x += weight
            } else {
                y += weight
            }
        }
        val k = x - y + 1

        array = Array(k) { DoublyLinkedList<Int>() }
        distTo[s] = 0
        getListByVertex(s).addHeader(s)
        var lastVertex = 0
        while (!hasNegativeCycle()) {
            val v = getNextVertex(lastVertex)
            if (v == -1) break
            lastVertex = relax(v)
        }
    }

    private fun getListByVertex(v: Int): DoublyLinkedList<Int> {
        require(hasPathTo(v))
        return array[distTo[v] - y]
    }

    private fun getNextVertex(v: Int): Int {
        var distance = distTo[v] - y
        while (distance < array.size) {
            if (!array[distance].isEmpty()) {
                return array[distance].deleteHeader()
            }
            distance++
        }
        return -1
    }

    private fun relax(v: Int): Int {
        var lastVertex = v
        digraph.adj(v).forEach { edge ->
            val w = edge.to()
            if (distTo[w] > distTo[v] + edge.weight.toInt()) {
                if (hasPathTo(w)) {
                    // 如果该点已经在数组中存在，先把原始数据移除，使用自定义的LinkedList比较麻烦
                    val list = getListByVertex(w)
                    var i = 0
                    val iterator = list.forwardIterator()
                    while (iterator.hasNext()) {
                        val value = iterator.next()
                        if (value == distTo[w]) {
                            list.delete(i)
                            break
                        }
                        i++
                    }
                }
                distTo[w] = distTo[v] + edge.weight.toInt()
                edgeTo[w] = edge
                getListByVertex(w).addTail(w)
                if (distTo[w] < distTo[lastVertex]) {
                    lastVertex = w
                }
            }
        }
        if (++cost % digraph.V == 0) {
            findNegativeCycle()
        }
        return lastVertex
    }

    private fun findNegativeCycle() {
        val newDigraph = EdgeWeightedDigraph(digraph.V)
        edgeTo.forEach { edge ->
            if (edge != null) {
                newDigraph.addEdge(edge)
            }
        }
        // 最短路径组成的加权有向图有环时，说明这个环是负权重环
        val cycleFinder = EdgeWeightedCycleFinder(newDigraph)
        if (cycleFinder.hasCycle()) {
            cycle = cycleFinder.cycle()
        }
    }

    fun hasNegativeCycle() = cycle != null

    fun negativeCycle() = cycle

    fun distTo(v: Int): Int {
        if (hasNegativeCycle()) return Int.MAX_VALUE
        return distTo[v]
    }

    fun hasPathTo(v: Int): Boolean {
        if (hasNegativeCycle()) return false
        return distTo[v] != Int.MAX_VALUE
    }

    fun pathTo(v: Int): Iterable<DirectedEdge>? {
        if (!hasPathTo(v)) return null
        val stack = Stack<DirectedEdge>()
        var edge = edgeTo[v]
        while (edge != null) {
            stack.push(edge)
            edge = edgeTo[edge.from()]
        }
        return stack
    }

    override fun toString(): String {
        if (hasNegativeCycle()) return "has negative cycle: ${negativeCycle()!!.joinToString()}"
        val stringBuilder = StringBuilder()
        for (v in 0 until digraph.V) {
            stringBuilder.append(s)
                    .append(" to ")
                    .append(v)
            if (hasPathTo(v)) {
                stringBuilder.append(" (")
                        .append(distTo(v))
                        .append("):")
                pathTo(v)!!.forEach {
                    stringBuilder.append(" ")
                            .append("${it.from()}->${it.to()} ${it.weight.toInt()}")
                }
                stringBuilder.append("\n")
            } else {
                stringBuilder.append(" Unreachable\n")
            }
        }
        return stringBuilder.toString()
    }
}

fun main() {
    val digraph = EdgeWeightedDigraph(5)
    digraph.addEdge(DirectedEdge(0, 1, 100.0))
    digraph.addEdge(DirectedEdge(0, 2, 1.0))
    digraph.addEdge(DirectedEdge(1, 2, -200.0))
    digraph.addEdge(DirectedEdge(2, 3, 1.0))
    digraph.addEdge(DirectedEdge(3, 4, 1.0))
    val bellmanFordSP = BellmanFordSP(digraph, 0)
    println(bellmanFordSP)
    val fastBellmanFordSP = FastBellmanFordSP(digraph, 0)
    println(fastBellmanFordSP)
}