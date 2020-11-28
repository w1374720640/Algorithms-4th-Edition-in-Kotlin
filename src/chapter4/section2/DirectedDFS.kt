package chapter4.section2

/**
 * 有向图的可达性API
 */
class DirectedDFS {
    private val marked: BooleanArray

    /**
     * 在[digraph]中找到从[s]可达的所有顶点
     */
    constructor(digraph: Digraph, s: Int) {
        marked = BooleanArray(digraph.V)
        dfs(digraph, s)
    }

    /**
     * 在[digraph]中找到从[source]中的所有顶点可达的所有顶点
     */
    constructor(digraph: Digraph, source: Iterable<Int>) {
        marked = BooleanArray(digraph.V)
        source.forEach { v ->
            if (!marked[v]) {
                dfs(digraph, v)
            }
        }
    }

    private fun dfs(digraph: Digraph, v: Int) {
        marked[v] = true
        digraph.adj(v).forEach { w ->
            if (!marked[w]) {
                dfs(digraph, w)
            }
        }
    }

    /**
     * [v]是可达的吗？
     */
    fun marked(v: Int): Boolean {
        return marked[v]
    }
}

fun main() {
    val digraph = getTinyDG()
    val directedDFS = DirectedDFS(digraph, 0)
    println(directedDFS.marked(2))
    println(directedDFS.marked(6))
}