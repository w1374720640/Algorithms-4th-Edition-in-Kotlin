package chapter4.section2

/**
 * 最短先导路径
 * 给定一幅有向无环图和两个顶点v和w，找出v和w之间的最短先导路径。
 * 设v和w的一个共同的祖先顶点为x，先导路径为v到x的最短路径和w到x的最短路径。
 * v和w之间的最短先导路径是所有先导路径中的最短者。
 * 热身：构造一幅有向无环图，使得最短先导路径到达的祖先顶点x不是v和w的最近共同祖先。
 * 提示：进行两次广度优先搜索，一次从v开始，一次从w开始。
 *
 * 解：使用广度优先搜索找到v和w所有的可达顶点
 * 遍历所有顶点，如果该顶点即可以通过v顶点到达，又可以通过w顶点到达，计算该点的先导路径
 * 记录所有先导路径中的最短点
 */
fun ex22_ShortestAncestralPath(digraph: Digraph, v: Int, w: Int): Int {
    val vPaths = BreadthFirstDirectedPaths(digraph, v)
    val wPaths = BreadthFirstDirectedPaths(digraph, w)
    var s = -1
    var minDistance = Int.MAX_VALUE
    for (i in 0 until digraph.V) {
        if (vPaths.hasPathTo(i) && wPaths.hasPathTo(i)) {
            val distance = vPaths.distTo(i) + wPaths.distTo(i)
            if (distance < minDistance) {
                minDistance = distance
                s = i
            }
        }
    }
    return s
}

fun main() {
    val digraph = Digraph(6)
    digraph.addEdge(1, 0)
    digraph.addEdge(2, 1)
    digraph.addEdge(3, 2)
    digraph.addEdge(4, 2)
    digraph.addEdge(5, 4)

    digraph.addEdge(3, 1)
    digraph.addEdge(5, 1)

    println("LCA=${ex21_LCAInDAG(digraph, 3, 5)}")
    println("SAP=${ex22_ShortestAncestralPath(digraph, 3, 5)}")
}