package chapter4.section4

import edu.princeton.cs.algs4.In
import extensions.formatDouble
import extensions.formatInt

/**
 * 优先级限制下的并行任务调度问题的关键路径方法
 */
class CPM(private val N: Int) {
    private val V = N * 2 + 2
    private val s = 2 * N // 起点
    private val t = 2 * N + 1 // 终点
    private val digraph = EdgeWeightedDigraph(V)
    private var lp: AcyclicLP? = null

    constructor(input: In) : this(input.readInt()) {
        input.readLine()
        repeat(N) { v ->
            // 使用一个或一个以上的空格做分隔符（正则匹配）
            val list = input.readLine().split(Regex(" +"))
            val weight = list[0].toDouble()
            // 测试数据jobsPC.txt在权重和后继任务中间还有一个整数，和书中不同，忽略
            val successor = ArrayList<Int>()
            for (i in 2 until list.size) {
                successor.add(list[i].toInt())
            }
            addTask(weight, v, *successor.toIntArray())
        }
        sort()
    }

    fun addTask(weight: Double, v: Int, vararg successor: Int) {
        digraph.addEdge(DirectedEdge(v, v + N, weight))
        digraph.addEdge(DirectedEdge(s, v, 0.0))
        digraph.addEdge(DirectedEdge(v + N, t, 0.0))
        successor.forEach {
            digraph.addEdge(DirectedEdge(v + N, it, 0.0))
        }
    }

    fun sort() {
        lp = AcyclicLP(digraph, s)
    }

    fun total(): Double {
        return lp?.distTo(t) ?: Double.NEGATIVE_INFINITY
    }

    override fun toString(): String {
        val total = total()
        if (total == Double.NEGATIVE_INFINITY) return "Error!"
        val stringBuilder = StringBuilder("Start times:\n")
        for (i in 0 until N) {
            stringBuilder.append(formatInt(i, 4))
                    .append(": ")
                    .append(if (lp!!.distTo(i) == Double.NEGATIVE_INFINITY) "Unreachable" else formatDouble(lp!!.distTo(i), 2, 6))
                    .append("\n")
        }
        stringBuilder.append("Finish time:\n")
                .append(formatDouble(total, 2))
        return stringBuilder.toString()
    }
}

fun getJobsPC() = CPM(In("./data/jobsPC.txt"))

fun main() {
    println(getJobsPC())
}