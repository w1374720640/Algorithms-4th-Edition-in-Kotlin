package chapter4.section1

/**
 * 欧拉环和汉密尔顿环
 * 考虑以下4组边定义的图：
 * 0-1 0-2 0-3 1-3 1-4 2-5 2-9 3-6 4-7 4-8 5-8 5-9 6-7 6-9 7-8
 * 0-1 0-2 0-3 1-3 0-3 2-5 5-6 3-6 4-7 4-8 5-8 5-9 6-7 6-9 8-8
 * 0-1 1-2 1-3 0-3 0-4 2-5 2-9 3-6 4-7 4-8 5-8 5-9 6-7 6-9 7-8
 * 4-1 7-9 6-2 7-3 5-0 0-2 0-8 1-6 3-9 6-3 2-8 1-5 9-8 4-5 4,7
 * 哪几幅图含有欧拉环（恰好包含了所有的边且没有重复的环）？
 * 哪几幅图含有汉密尔顿环（恰好包含了所有的顶点且没有重复的环）？
 *
 * 解：连通的无向图G有欧拉路径的充要条件是：G中奇顶点（连接的边数量为奇数的顶点）的数目等于0或2
 * 连通的无向图G是欧拉环（存在欧拉回路）的充要条件是：G中每个顶点的度都是偶数
 * 官方实现见jar包中的[edu.princeton.cs.algs4.EulerianCycle]类
 * 或在线代码：https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/EulerianCycle.java.html
 * 这里只简单判断每个顶点的度是否都是偶数
 *
 * 汉密尔顿图：由指定的起点前往指定的终点，途中经过所有其他节点且只经过一次
 * 在图论中是指含有哈密顿回路的图，闭合的哈密顿路径称作哈密顿回路，含有图中所有顶点的路径称作哈密顿路径
 * 参考：https://zh.wikipedia.org/wiki/%E5%93%88%E5%AF%86%E9%A1%BF%E5%9B%BE
 * 代码不会写，略
 */
fun eulerian(graph: Graph): Boolean {
    for (v in 0 until graph.V) {
        val iterable = graph.adj(v)
        if (iterable.count() % 2 != 0) {
            return false
        }
    }
    return true
}

fun main() {
    println(eulerian(getGraph1()))
    println(eulerian(getGraph2()))
    println(eulerian(getGraph3()))
    println(eulerian(getGraph4()))
}

private fun getGraph1(): Graph {
    val graph = Graph(10)
    graph.addEdge(0, 1)
    graph.addEdge(0, 2)
    graph.addEdge(0, 3)
    graph.addEdge(1, 3)
    graph.addEdge(1, 4)
    graph.addEdge(2, 5)
    graph.addEdge(2, 9)
    graph.addEdge(3, 6)
    graph.addEdge(4, 7)
    graph.addEdge(4, 8)
    graph.addEdge(5, 8)
    graph.addEdge(5, 9)
    graph.addEdge(6, 7)
    graph.addEdge(6, 9)
    graph.addEdge(7, 8)
    return graph
}

private fun getGraph2(): Graph {
    val graph = Graph(10)
    graph.addEdge(0, 1)
    graph.addEdge(0, 2)
    graph.addEdge(0, 3)
    graph.addEdge(1, 3)
    graph.addEdge(0, 3)
    graph.addEdge(2, 5)
    graph.addEdge(5, 6)
    graph.addEdge(3, 6)
    graph.addEdge(4, 7)
    graph.addEdge(4, 8)
    graph.addEdge(5, 8)
    graph.addEdge(5, 9)
    graph.addEdge(6, 7)
    graph.addEdge(6, 9)
    graph.addEdge(8, 8)
    return graph
}

private fun getGraph3(): Graph {
    val graph = Graph(10)
    graph.addEdge(0, 1)
    graph.addEdge(1, 2)
    graph.addEdge(1, 3)
    graph.addEdge(0, 3)
    graph.addEdge(0, 4)
    graph.addEdge(2, 5)
    graph.addEdge(2, 9)
    graph.addEdge(3, 6)
    graph.addEdge(4, 7)
    graph.addEdge(4, 8)
    graph.addEdge(5, 8)
    graph.addEdge(5, 9)
    graph.addEdge(6, 7)
    graph.addEdge(6, 9)
    graph.addEdge(7, 8)
    return graph
}

private fun getGraph4(): Graph {
    val graph = Graph(10)
    graph.addEdge(4, 1)
    graph.addEdge(7, 9)
    graph.addEdge(6, 2)
    graph.addEdge(7, 3)
    graph.addEdge(5, 0)
    graph.addEdge(0, 2)
    graph.addEdge(0, 8)
    graph.addEdge(1, 6)
    graph.addEdge(3, 9)
    graph.addEdge(6, 3)
    graph.addEdge(2, 8)
    graph.addEdge(1, 5)
    graph.addEdge(9, 8)
    graph.addEdge(4, 5)
    graph.addEdge(4, 7)
    return graph
}