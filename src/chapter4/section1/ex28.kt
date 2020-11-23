package chapter4.section1

/**
 * 判断图是否有环，可以存在自环或平行边
 *
 * 解：自环：一条连接一个顶点和其自身的边
 * 平行边：连接同一对顶点的两条边
 * 自环和平行边不算环，在判断有环的逻辑上排除这两种情况
 */
class LooserCycle(graph: Graph) {
    private val marked = BooleanArray(graph.V)
    private val edgeTo = IntArray(graph.V)
    var hasCycle = false
        private set

    init {
        for (s in 0 until graph.V) {
            if (!marked[s]) {
                dfs(graph, s, s)
            }
        }
    }

    private fun dfs(graph: Graph, v: Int, parent: Int) {
        if (hasCycle) return
        marked[v] = true
        edgeTo[v] = parent
        graph.adj(v).forEach { w ->
            if (!marked[w]) {
                dfs(graph, w, v)
            } else if (w != parent
                    && w != v // 不是自环
                    && edgeTo[w] != v // 不是平行边
            ) {
                hasCycle = true
                return
            }
        }
    }
}

fun main() {
    val graph = Graph(3)
    graph.addEdge(0, 1)
    graph.addEdge(1, 2)
    println("${Cycle(graph).hasCycle} ${LooserCycle(graph).hasCycle}")
    graph.addEdge(0, 0) // 加一个自环
    println("${Cycle(graph).hasCycle} ${LooserCycle(graph).hasCycle}")
    graph.addEdge(0, 1) // 加一个平行边
    println("${Cycle(graph).hasCycle} ${LooserCycle(graph).hasCycle}")
    graph.addEdge(0, 2) // 加一个真的环
    println("${Cycle(graph).hasCycle} ${LooserCycle(graph).hasCycle}")
}