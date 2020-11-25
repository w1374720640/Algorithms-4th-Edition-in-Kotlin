package chapter4.section1

import edu.princeton.cs.algs4.In
import edu.princeton.cs.algs4.Stack

/**
 * 如果用栈代替队列来实现广度优先搜索，我们还能得到最短路径吗？
 *
 * 解：能，用两个栈而不是一个栈
 * 遍历时，从一个栈中取顶点信息，将顶点的相邻顶点加入另一个栈中，当第一个栈为空时，交换两个栈
 * 当两个栈都为空时，遍历结束
 * 和用队列实现的广度优先搜索对比，空间复杂度增加了常数值，时间复杂度不变
 */
class StackBreadthFirstPath(graph: Graph, val s: Int) : Paths(graph, s) {
    private val marked = BooleanArray(graph.V)
    private val edgeTo = IntArray(graph.V)

    init {
        var inputStack = Stack<Int>()
        var outputStack = Stack<Int>()
        inputStack.push(s)
        marked[s] = true
        while (!inputStack.isEmpty) {
            val v = inputStack.pop()
            graph.adj(v).forEach { w ->
                if (!marked[w]) {
                    outputStack.push(w)
                    marked[w] = true
                    edgeTo[w] = v
                }
            }
            if (inputStack.isEmpty) {
                val temp = inputStack
                inputStack = outputStack
                outputStack = temp
            }
        }
    }

    override fun hasPathTo(v: Int): Boolean {
        return marked[v]
    }

    override fun pathTo(v: Int): Iterable<Int>? {
        if (!hasPathTo(v)) return null
        val stack = Stack<Int>()
        var w = v
        while (w != s) {
            stack.push(w)
            w = edgeTo[w]
        }
        stack.push(s)
        return stack
    }
}

fun main() {
    val graph = Graph(In("./data/mediumG.txt"))
    val paths1 = BreadthFirstPaths(graph, 0)
    val paths2 = StackBreadthFirstPath(graph, 0)
    for (v in 0 until graph.V) {
        check(paths1.pathTo(v)?.count() == paths2.pathTo(v)?.count())

        println("v : $v")
        println("paths1 : ${paths1.pathTo(v)?.joinToString()}")
        println("paths2 : ${paths2.pathTo(v)?.joinToString()}")
    }
    println()
    println("mediumG check succeed.")
}