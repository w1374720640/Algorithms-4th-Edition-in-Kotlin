package chapter4.section3

import edu.princeton.cs.algs4.Bag

/**
 * 真假判断：在Kruskal算法的执行过程中，最小生成树中的每个顶点到它的子树中的某个顶点的距离比到非子树中的任意顶点都近。
 * 证明你的结论。
 *
 * 解：用最小生成树构造一个邻接表，找出每个顶点到所有子树的最大权重，
 * 遍历原图的邻接表，找到每个顶点所有非子树的最小权重，
 * 判断是否所有顶点子树权重的最大值是否都小于非子树权重的最小值。
 * 证明略
 */
fun ex20(graph: EdgeWeightedGraph): Boolean {
    val adj = Array(graph.V) { Bag<Edge>() }
    KruskalMST(graph).edges().forEach {
        val v = it.either()
        val w = it.other(v)
        adj[v].add(it)
        adj[w].add(it)
    }
    val maxAdjWeightArray = DoubleArray(graph.V) { Double.MIN_VALUE }
    for (v in 0 until graph.V) {
        adj[v].forEach {
            if (it.weight > maxAdjWeightArray[v]) {
                maxAdjWeightArray[v] = it.weight
            }
        }
    }
    val minGraphWeightArray = DoubleArray(graph.V) { Double.MAX_VALUE }
    for (v in 0 until graph.V) {
        graph.adj(v).forEach {
            if (it.weight < minGraphWeightArray[v] && !adj[v].contains(it)) {
                minGraphWeightArray[v] = it.weight
            }
        }
    }
    for (v in 0 until graph.V) {
        if (maxAdjWeightArray[v] > minGraphWeightArray[v]) return false
    }
    return true
}

fun main() {
    val tinyGraph = getTinyWeightedGraph()
    println(ex20(tinyGraph))
    val weightedGraph = getMediumWeightedGraph()
    println(ex20(weightedGraph))
    val randomGraph = getRandomEWG()
    println(ex20(randomGraph.first))
}