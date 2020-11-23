package chapter4.section1

import edu.princeton.cs.algs4.Queue

/**
 * 检测平行边
 * 设计一个线性时间的算法来统计图中的平行边的总数
 *
 * 解：参考练习4.1.28中检测平行边的代码，不过这里用广度优先搜索
 */
fun Graph.parallelEdgeCount(): Int {
    val marked = BooleanArray(V)
    val edgeTo = IntArray(V)
    var count = 0
    for (s in 0 until V) {
        if (!marked[s]) {
            val queue = Queue<Int>()
            marked[s] = true
            edgeTo[s] = s
            queue.enqueue(s)
            while (!queue.isEmpty) {
                val v = queue.dequeue()
                adj(v).forEach { w ->
                    if (!marked[w]) {
                        marked[w] = true
                        edgeTo[w] = v
                        queue.enqueue(w)
                    } else if (w != v && edgeTo[w] == v) {
                        count++
                    }
                }
            }
        }
    }
    return count
}

fun main() {
    val graph = Graph(4)
    graph.addEdge(0, 1)
    graph.addEdge(1, 2)
    graph.addEdge(2, 3)
    println(graph.parallelEdgeCount())
    graph.addEdge(0, 0)
    println(graph.parallelEdgeCount())
    graph.addEdge(0, 1)
    println(graph.parallelEdgeCount())
    graph.addEdge(2, 3)
    println(graph.parallelEdgeCount())
}