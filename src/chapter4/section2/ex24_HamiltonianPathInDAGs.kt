package chapter4.section2

/**
 * 有向无环图中的汉密尔顿路径
 * 设计一种线性时间的算法来判定给定的有向无环图中是否存在一条能够正好只访问每个顶点一次的有向路径。
 * 答案：计算给定图的拓扑排序并顺序检查拓扑排序中每一对相邻的顶点之间是否存在一条边。
 *
 * 解：汉密尔顿图：由指定的起点前往指定的终点，途中经过所有其他节点且只经过一次
 * 在图论中是指含有汉密尔顿回路的图，闭合的汉密尔顿路径称作汉密尔顿回路，含有图中所有顶点的路径称作汉密尔顿路径
 * 参考：https://zh.wikipedia.org/wiki/%E5%93%88%E5%AF%86%E9%A1%BF%E5%9B%BE
 * 直接根据提示的答案给出代码
 */
fun ex24_HamiltonianPathInDAGs(digraph: Digraph): Boolean {
    val topological = Topological(digraph)
    if (!topological.isDAG()) return false
    val order = topological.order()!!.iterator()
    if (!order.hasNext()) return false
    var v = order.next()
    while (order.hasNext()) {
        val w = order.next()
        if (!digraph.hasEdge(v, w)) return false
        v = w
    }
    return true
}

fun main() {
    val digraph1 = getJobsSymbolDigraph().G()
    println(ex24_HamiltonianPathInDAGs(digraph1))

    val digraph2 = Digraph(8)
    digraph2.addEdge(0, 1)
    digraph2.addEdge(1, 2)
    digraph2.addEdge(1, 3)
    digraph2.addEdge(2, 4)
    digraph2.addEdge(2, 5)
    digraph2.addEdge(4, 5)
    digraph2.addEdge(3, 6)
    digraph2.addEdge(3, 7)
    digraph2.addEdge(6, 7)
    digraph2.addEdge(5, 3)
    println(ex24_HamiltonianPathInDAGs(digraph2))
}