package chapter4.section2

/**
 * 编写一个方法，检查一副有向无环图的顶点的给定排列是否是该图顶点的拓扑排序
 *
 * 解：虽然根据书中的拓扑排序算法得出的结果只有一种，但实际上有多种排列方式都可以满足拓扑顺序，只要能满足先后顺序就行
 * 创建一个marked数组，遍历给定排列时，将顶点对应位置的marked值设置为true，
 * 同时检查该顶点所有直接可达的顶点，只要有一个直接可达顶点的marked值为true，则不符合条件，
 * 如果遍历完成后，所有顶点的直接可达顶点对应的marked值为false，则该排序是图的拓扑排序
 */
fun ex9(digraph: Digraph, iterable: Iterable<Int>): Boolean {
    val marked = BooleanArray(digraph.V)
    iterable.forEach { v ->
        marked[v] = true
        digraph.adj(v).forEach { w ->
            if (marked[w]) return false
        }
    }
    return true
}

fun main() {
    val symbolDigraph = getJobsSymbolDigraph()
    val digraph = symbolDigraph.G()
    val topological = Topological(digraph)
    check(topological.isDAG())
    val iterable1 = topological.order()!!
    println(iterable1.joinToString())
    println(ex9(digraph, iterable1))
    val iterable2 = listOf(4, 0, 5, 9, 8, 1, 7, 11, 12, 10, 3, 2, 6)
    println(iterable2.joinToString())
    println(ex9(digraph, iterable2))
    val iterable3 = listOf(8, 9, 5, 4, 0, 1, 7, 11, 12, 10, 2, 6, 3)
    println(iterable3.joinToString())
    println(ex9(digraph, iterable3))
}