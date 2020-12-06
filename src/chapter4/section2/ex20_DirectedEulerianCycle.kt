package chapter4.section2

/**
 * 有向欧拉环
 * 欧拉环是一条每条边恰好出现一次的有向环。
 * 编写一个程序Euler来找出有向图中的欧拉环或者说明它不存在。
 * 提示：当且仅当有向图G是连通的且每个顶点的出度和入度相同时G含有一条有向欧拉环
 *
 * 解：先统计每个顶点的出度，再获取有向图的反向图获取每个顶点的入度
 * 根据提示判断有向图G是否是一个欧拉环
 * 因为欧拉环的定义是：恰好包含了所有的边且没有重复的环
 * 所以所有的顶点都在欧拉环中
 */
fun ex20_DirectedEulerianCycle(digraph: Digraph): Boolean {
    val outDegrees = IntArray(digraph.V)

    for (i in 0 until digraph.V) {
        outDegrees[i] = digraph.adj(i).count()
    }
    val reverseDigraph = digraph.reverse()
    for (i in 0 until digraph.V) {
        val inDegree = reverseDigraph.adj(i).count()
        if (inDegree != outDegrees[i]) return false
    }
    return true
}

fun main() {
    val digraph1 = getTinyDG()
    println(ex20_DirectedEulerianCycle(digraph1))

    val digraph2 = Digraph(6)
    digraph2.addEdge(0, 1)
    digraph2.addEdge(1, 2)
    digraph2.addEdge(2, 3)
    digraph2.addEdge(3, 4)
    digraph2.addEdge(4, 5)
    digraph2.addEdge(5, 3)
    digraph2.addEdge(3, 2)
    digraph2.addEdge(2, 0)
    println(ex20_DirectedEulerianCycle(digraph2))
}