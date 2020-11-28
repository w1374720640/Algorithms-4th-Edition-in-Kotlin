package chapter4.section2

import edu.princeton.cs.algs4.Stack

/**
 * 有向环的API
 */
class DirectedCycle(digraph: Digraph) {
    private val marked = BooleanArray(digraph.V)
    private val edgeTo = IntArray(digraph.V)
    private val onStack = BooleanArray(digraph.V)
    private var stack: Stack<Int>? = null

    init {
        for (v in 0 until digraph.V) {
            if (!marked[v]) {
                dfs(digraph, v)
            }
        }
    }

    private fun dfs(digraph: Digraph, v: Int) {
        marked[v] = true
        onStack[v] = true
        digraph.adj(v).forEach { w ->
            if (hasCycle()) return
            if (!marked[w]) {
                edgeTo[w] = v
                dfs(digraph, w)
            } else if (onStack[w]) {
                // 如果w点被当前方法调用栈访问过，说明当前路径上存在环
                var s = v
                stack = Stack<Int>()
                while (s != w) {
                    stack!!.push(s)
                    s = edgeTo[s]
                }
                stack!!.push(w)
                // 顶点v被添加了两次，用于表现环的特点
                stack!!.push(v)
            }
        }
        // 方法退出时，表明v点未连接任何环结构，重置onStack对应位置的值，而不重置marked对应位置的值
        // 当通过其他路径再次遍历到v点时，既不用遍历v点连接的点，也不用加入到环的路径中
        onStack[v] = false
    }

    /**
     * 是否含有有向环
     */
    fun hasCycle(): Boolean {
        return stack != null
    }

    /**
     * 有向环中的所有顶点（如果存在的话）
     */
    fun cycle(): Iterable<Int>? {
        return stack
    }
}

fun main() {
    val digraph = getTinyDG()
    val cycle = DirectedCycle(digraph)
    if (cycle.hasCycle()) {
        println("has cycle")
        println(cycle.cycle()!!.joinToString())
    } else {
        println("no cycle")
    }
}