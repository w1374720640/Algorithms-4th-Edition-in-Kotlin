package chapter4.section1

import edu.princeton.cs.algs4.Bag
import edu.princeton.cs.algs4.In
import extensions.readInt
import java.lang.StringBuilder

/**
 * 无向图的API
 */
class Graph {
    var V: Int
        private set
    var E: Int = 0
        private set
    private val adj: Array<Bag<Int>>

    /**
     * 创建一个含有V个顶点但不含有边的图
     */
    constructor(V: Int) {
        this.V = V
        adj = Array(V) { Bag<Int>() }
    }

    /**
     * 从标准输入流input读入一幅图
     */
    constructor(input: In) : this(input.readInt()) {
        E = input.readInt()
        repeat(E) {
            addEdge(input.readInt(), input.readInt())
        }
    }

    /**
     * 向图中添加一条边v-w
     */
    fun addEdge(v: Int, w: Int) {
        adj[v].add(w)
        adj[w].add(v)
    }

    /**
     * 和v相邻的所有顶点
     */
    fun adj(v: Int): Iterable<Int> {
        return adj[v]
    }

    /**
     * 对象的字符串表示
     */
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
    val path = "./data/tinyG.txt"
    val graph = Graph(In(path))
    println(graph.toString())
}