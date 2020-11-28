package chapter4.section2

import edu.princeton.cs.algs4.In

/**
 * 修改Digraph，不允许存在平行边和自环。
 */
class DisallowParallelEdgesAndSelfLoopsDigraph : Digraph {
    constructor(V: Int) : super(V)
    constructor(input: In) : super(input)

    override fun addEdge(v: Int, w: Int) {
        if (v == w) return
        if (adj[v].contains(w)) return
        adj[v].add(w)
        E++
    }
}

fun main() {
    val digraph = DisallowParallelEdgesAndSelfLoopsDigraph(3)
    digraph.addEdge(0, 1)
    digraph.addEdge(0, 1)
    digraph.addEdge(1, 2)
    digraph.addEdge(1, 2)
    digraph.addEdge(0, 0)
    digraph.addEdge(1, 1)
    println(digraph)
}