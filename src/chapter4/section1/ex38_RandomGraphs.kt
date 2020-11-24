package chapter4.section1

import extensions.inputPrompt
import extensions.random
import extensions.readInt

/**
 * 随机图
 * 编写一个程序ErdosRenyiGraph，从命令行接受整数V和E，随机生成E对0到V-1之间的整数来构造一幅图。
 * 注意：生成器可能会产生自环和平行边。
 */
fun ex38_RandomGraphs(V: Int, E: Int): Graph {
    val graph = Graph(V)
    repeat(E) {
        graph.addEdge(random(V), random(V))
    }
    return graph
}

fun main() {
    inputPrompt()
    val V = readInt("V: ")
    val E = readInt("E: ")
    val graph = ex38_RandomGraphs(V, E)
    drawGraph(graph)
}