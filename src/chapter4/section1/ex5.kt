package chapter4.section1

import edu.princeton.cs.algs4.In

/**
 * 修改Graph，不允许存在平行边和自环
 */
class DisallowParallelEdgesAndSelfLoopsGraph : Graph {
    constructor(V: Int) : super(V)
    constructor(input: In) : super(input)

    override fun addEdge(v: Int, w: Int) {
        if (v == w) return
        if (adj[v].contains(w)) return
        adj[v].add(w)
        adj[w].add(v)
        E++
    }
}

fun main() {
    val path = "./data/tinyG.txt"
    val graph = Graph(In(path))
    // 平行边
    graph.addEdge(0, 1)
    // 自环
    graph.addEdge(2, 2)
    println(graph.toString())

    val graph2 = DisallowParallelEdgesAndSelfLoopsGraph(In(path))
    graph2.addEdge(0, 1)
    graph2.addEdge(2, 2)
    println(graph2.toString())
}