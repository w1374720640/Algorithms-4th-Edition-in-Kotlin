package chapter4.section4

/**
 * 给定两点的最短路径
 * 设计并实现一份API，使用Dijkstra算法的改进版本解决加权有向图中给定两点的最短路径问题
 *
 * 解：需要先求从x点到y点的最短路径，再求从y到x的最短路径，路径可能不存在
 * 用两个[DijkstraSP]类分别求两条最短路径
 */
class SourceSinkShortestPaths(digraph: EdgeWeightedDigraph, val x: Int, val y: Int) {
    private val xSP = DijkstraSP(digraph, x)
    private val ySP = DijkstraSP(digraph, y)

    fun hasPathXToY() = xSP.hasPathTo(y)

    fun hasPathYToX() = ySP.hasPathTo(x)

    fun distXToY() = xSP.distTo(y)

    fun distYToX() = ySP.distTo(x)

    fun pathXToY() = xSP.pathTo(y)

    fun pathYToX() = ySP.pathTo(x)
}

fun main() {
    val digraph = getTinyEWD()
    val paths = SourceSinkShortestPaths(digraph, 0, 1)
    println(paths.pathXToY()?.joinToString())
    println(paths.pathYToX()?.joinToString())
}