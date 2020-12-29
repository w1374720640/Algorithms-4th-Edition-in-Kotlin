package chapter4.section3

import chapter1.section5.CompressionWeightedQuickUnionUF

/**
 * 验证
 * 编写一个使用最小生成树算法以及EdgeWeightedGraph类的方法check()，
 * 使用以下根据命题J得到的最优切分条件来验证给定的一组边就是一颗最小生成树：
 * 如果给定的一组边是一颗生成树，且删除树的任意边得到的切分中权重最小的横切边正是被删除的那条边，
 * 则这组边就是图的最小生成树。
 * 你的方法的运行时间的增长数量级是多少？
 *
 * 解：依次删除给定最小生成树中的一条边，使用union-find方法确定剩余顶点的连通性，
 * 然后遍历所有的边，找出所有横切边中权重最小的边，判断和删除的边是否相同，如果不同则不是最小生成树。
 * 时间复杂度为 O(V) * (O(V) + O(E)) = O(VE)
 */
fun ex33_Certification(graph: EWG, tree: Iterable<Edge>): Boolean {
    // 树的大小必须等于V-1
    if (tree.count() != graph.V - 1) return false
    repeat(graph.V - 1) {
        val uf = CompressionWeightedQuickUnionUF(graph.V)
        var deleteEdge: Edge? = null
        tree.forEachIndexed { index, edge ->
            if (index == it) {
                deleteEdge = edge
            } else {
                val v = edge.v
                val w = edge.other(v)
                uf.union(v, w)
            }
        }
        // 删除一条边后，最小生成树必须分裂为两个独立的连通分量
        if (uf.count() != 2) return false
        var minEdge: Edge? = null
        graph.edges().forEach { edge ->
            val v = edge.v
            val w = edge.other(v)
            if (!uf.connected(v, w) && (minEdge == null || edge < minEdge!!)) {
                minEdge = edge
            }
        }
        // 最小的横切边应该是被删除的那个边
        if (minEdge != deleteEdge) return false
    }
    return true
}

fun main() {
    val graph = getTinyWeightedGraph()
    val tree = PrimMST(graph).edges()
    println(ex33_Certification(graph, tree))
}