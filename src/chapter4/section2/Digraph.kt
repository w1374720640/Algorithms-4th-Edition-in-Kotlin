package chapter4.section2

import edu.princeton.cs.algs4.Bag
import edu.princeton.cs.algs4.In

/**
 * 有向图的API
 */
class Digraph {
    val V: Int
    var E: Int = 0
    private val adj: Array<Bag<Int>>

    constructor(V: Int) {
        this.V = V
        adj = Array(V) { Bag<Int>() }
    }

    constructor(input: In) : this(input.readInt()) {
        val E = input.readInt()
        repeat(E) {
            addEdge(input.readInt(), input.readInt())
        }
    }

    /**
     * 向有向图中添加一条边 v -> w
     */
    fun addEdge(v: Int, w: Int) {
        adj[v].add(w)
        E++
    }

    fun adj(v: Int): Iterable<Int> {
        return adj[v]
    }

    /**
     * 该图的反向图
     */
    fun reverse(): Digraph {
        val reverseDigraph = Digraph(V)
        for (v in 0 until V) {
            adj[v].forEach { w ->
                reverseDigraph.addEdge(w, v)
            }
        }
        return reverseDigraph
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
                .append(V)
                .append(" vertices, ")
                .append(E)
                .append(" edges\n")
        for (i in adj.indices) {
            stringBuilder.append(i)
                    .append(": ")
            adj[i].forEach {
                stringBuilder.append(it)
                        .append(" ")
            }
            stringBuilder.append("\n")
        }
        return stringBuilder.toString()
    }
}

fun main() {
    // 官网最新测试数据包内容和书中略有不同，需要修改成原文中相同的数据
    val path = "./data/tinyDG.txt"
    val digraph = Digraph(In(path))
    println(digraph)
}