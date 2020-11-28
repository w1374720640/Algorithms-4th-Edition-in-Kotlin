package chapter4.section2

/**
 * 为Digraph编写一个测试用例
 */
fun main() {
    val digraph = getTinyDG()
    check(digraph.V == 13)
    check(digraph.E == 22)
    check(digraph.hasEdge(0, 1))
    check(!digraph.hasEdge(1, 0))
    digraph.addEdge(1, 0)
    check(digraph.E == 23)
    check(digraph.hasEdge(1, 0))
    check(digraph.adj(6).count() == 3)
    check(digraph.hasEdge(2, 3))
    check(digraph.hasEdge(3, 2))

    val reverseDigraph = digraph.reverse()
    check(reverseDigraph.V == 13)
    check(reverseDigraph.E == 23)
    check(reverseDigraph.hasEdge(6, 7))
    check(!reverseDigraph.hasEdge(7, 6))

    println("check succeed.")
}