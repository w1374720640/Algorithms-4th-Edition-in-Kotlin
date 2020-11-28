package chapter4.section2

/**
 * 顶点对可达性的API
 */
class TransitiveClosure(digraph: Digraph) {
    private val all = Array(digraph.V) { DirectedDFS(digraph, it) }

    /**
     * w是从v可达的吗
     */
    fun reachable(v: Int, w: Int): Boolean {
        return all[v].marked(w)
    }
}