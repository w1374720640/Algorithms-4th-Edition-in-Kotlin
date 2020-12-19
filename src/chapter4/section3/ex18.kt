package chapter4.section3

import chapter2.sleep

/**
 * 给出使用延时Prim算法、即时Prim算法和Kruskal算法在计算练习4.3.6中的图的最小生成树过程中的轨迹。
 */
fun main() {
    val graph = ex6()

    val delay = 2000L
    drawEWGGraph(graph) {
        LazyPrimMST(it)
    }
    sleep(delay)
    drawEWGGraph(graph) {
        PrimMST(it)
    }
    sleep(delay)
    drawEWGGraph(graph) {
        KruskalMST(it)
    }
}