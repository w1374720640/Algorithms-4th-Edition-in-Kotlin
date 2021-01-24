package chapter4.section4

/**
 * 关键边
 * 给出一个算法来找到给定的加权有向图中的一条边，删去这条边使得给定的两个顶点之间的最短距离的增加值最大。
 *
 * 解：假设图的所有边权重为非负数，使用Dijkstra算法找到最短路径
 * 遍历最短路径的每条边，依次构造不含该边的加权有向图，再分别求出它们的最短路径，记录最短路径的最大值
 * 时间复杂度为：O(ElgV)+O(X)*(O(V)+O(ElgV))=O(X*ElgV) 其中X表示最短路径中边的数量
 */
fun ex37_CriticalEdges(digraph: EdgeWeightedDigraph, v: Int, w: Int): DirectedEdge? {
    val sp = DijkstraSP(digraph, v)
    val path = sp.pathTo(w) ?: return null
    var deleteEdge: DirectedEdge? = null
    var maxDist = sp.distTo(w)
    path.forEach { edge ->
        val newDigraph = EdgeWeightedDigraph(digraph.V)
        digraph.edges().forEach {
            if (it != edge) {
                newDigraph.addEdge(it)
            }
        }
        val newSP = DijkstraSP(newDigraph, v)
        if (newSP.hasPathTo(w) && newSP.distTo(w) > maxDist) {
            deleteEdge = edge
            maxDist = newSP.distTo(w)
        }
    }
    return deleteEdge
}

fun main() {
    val v = 0
    val w = 3
    val digraph = getTinyEWD()
    val sp = DijkstraSP(digraph, v)
    println(sp.pathTo(w)?.joinToString())
    val deleteEdge = ex37_CriticalEdges(digraph, v, w)
    println("delete: $deleteEdge")
}