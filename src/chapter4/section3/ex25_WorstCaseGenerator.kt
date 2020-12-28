package chapter4.section3

import extensions.formatDouble
import extensions.random
import extensions.spendTimeMillis

/**
 * 最坏情况生成器
 * 开发一个加权图生成器，图中含有V个顶点和E条边，使得延时的Prim算法所需的运行时间是非线性的。
 * 对于即时的Prim算法回答相同的问题。
 *
 * 解：由命题M及其证明可知，最坏情况下优先队列一次插入的成本为~lgE，删除最小元素的成本为~2lgE，
 * 当加权图中插入的边权重按降序排列时，插入的成本为~lgE，延时Prim算法所需的运行时间为~ElgE
 * 堆排序的最坏情况很难确定，参考练习2.4.16
 */
fun ex25_WorstCaseGenerator(V: Int, E: Int): EdgeWeightedGraph {
    val graph = EdgeWeightedGraph(V)
    var weight = random()
    // 每个边权重的差值
    val diff = 0.00001
    repeat(E) {
        val v = random(V)
        val w = random(V)
        // 后插入的边权重始终比前面的小
        weight -= diff
        graph.addEdge(Edge(v, w, weight))
    }
    return graph
}

fun main() {
    val V = 1000
    var E = 10_0000
    var lastTime = 0L
    repeat(5) {
        val graph = ex25_WorstCaseGenerator(V, E)
        val time = spendTimeMillis {
            // 重复10次
            repeat(10) {
                LazyPrimMST(graph)
            }
        }
        println("V=$V W=$E time=$time ms ratio=${if (lastTime == 0L) "/" else formatDouble(time.toDouble() / lastTime, 2)}")
        E *= 2
        lastTime = time
    }
}