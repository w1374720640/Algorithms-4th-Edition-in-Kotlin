package chapter4.section1

import chapter2.swap
import extensions.inputPrompt
import extensions.random
import extensions.readInt

/**
 * 随机简单图
 * 编写一个程序RandomSimpleGraph，从命令行接受整数V和E，用均等的几率生成含有V个顶点和E条边的所有可能的简单图
 *
 * 解：先生成所有可能的边，再以相等概率随机取E条边
 * 相等概率获取数组中的内容可以参考练习1.1.36中随机打乱数组的算法
 */
fun ex39_RandomSimpleGraph(V: Int, E: Int): Graph {
    val graph = Graph(V)
    val defaultEdge = Edge(0, 0)
    val edges = Array(V * (V - 1) / 2) { defaultEdge }
    var i = 0
    for (v in 0 until V) {
        for (w in v + 1 until V) {
            edges[i++] = Edge(v, w)
        }
    }
    check(i == edges.size)

    repeat(E) {
        val index = random(V - it)
        val edge = edges[index]
        graph.addEdge(edge.small, edge.large)
        edges.swap(index, V - it - 1)
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