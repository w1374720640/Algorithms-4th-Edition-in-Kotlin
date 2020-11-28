package chapter4.section2

/**
 * 为Digraph添加一个构造函数，它接受一幅有向图G然后创建并初始化这幅图的一个副本。
 * G的用例的对它作出的任何改动都不应该影响到它的副本。
 */
fun Digraph.copy(): Digraph {
    val newDigraph = Digraph(V)
    for (v in 0 until V) {
        adj(v).forEach { w ->
            newDigraph.addEdge(v, w)
        }
    }
    return newDigraph
}

fun main() {
    val digraph = getTinyDG()
    val copyDigraph = digraph.copy()
    println("before change:")
    println("digraph: $digraph")
    println("copy digraph: $copyDigraph")

    digraph.addEdge(1, 2)
    digraph.addEdge(2, 11)
    println("after change:")
    println("digraph: $digraph")
    println("copy digraph: $copyDigraph")
}