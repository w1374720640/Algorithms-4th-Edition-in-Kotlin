package chapter4.section3

import extensions.formatDouble
import kotlin.math.PI
import kotlin.math.log2
import kotlin.math.sqrt

/**
 * 最小生成树中的最长边
 * 运行实验并根据经验分析最小生成树中最长边的长度以及图中不长于该边的边的总数
 */
fun ex41_LongestMSTEdge(graph: EWG, mst: MST): Pair<Edge, Int> {
    val iterator = mst.edges().iterator()
    require(iterator.hasNext())
    var maxEdge = iterator.next()
    while (iterator.hasNext()) {
        val nextEdge = iterator.next()
        if (nextEdge > maxEdge) {
            maxEdge = nextEdge
        }
    }
    var count = 0
    graph.edges().forEach {
        if (it <= maxEdge) {
            count++
        }
    }
    return maxEdge to count
}

fun main() {
    val V = 1000
    // 权重均匀分布的随机稀疏图
    val evenlyEWG = RandomSparseEvenlyEWG(V)
    // 权重高斯分布的随机稀疏图
    val gaussianEWG = RandomSparseGaussianEWG(V)
    val threshold = sqrt(log2(V.toDouble()) / (PI * V))
    // 稀疏的欧几里得图
    val euclideanEWG = getRandomEWG(V, threshold * 2).first
    // E=V*(V-1)/2的稠密图
    val denseEWG = getDenseGraph(V)

    val evenlyResult = ex41_LongestMSTEdge(evenlyEWG, PrimMST(evenlyEWG))
    // 除了打印最小生成树中的最大边和图中不长于该边的总数外，还打印总数和顶点数量的比例
    println("maxEdge: ${evenlyResult.first}  count=${evenlyResult.second}  ratio=${formatDouble(evenlyResult.second.toDouble() / V, 2)}")
    val gaussianResult = ex41_LongestMSTEdge(gaussianEWG, PrimMST(gaussianEWG))
    println("maxEdge: ${gaussianResult.first}  count=${gaussianResult.second}  ratio=${formatDouble(gaussianResult.second.toDouble() / V, 2)}")
    val euclideanResult = ex41_LongestMSTEdge(euclideanEWG, PrimMST(euclideanEWG))
    println("maxEdge: ${euclideanResult.first}  count=${euclideanResult.second}  ratio=${formatDouble(euclideanResult.second.toDouble() / V, 2)}")
    val denseResult = ex41_LongestMSTEdge(denseEWG, PrimMST(denseEWG))
    println("maxEdge: ${denseResult.first}  count=${denseResult.second}  ratio=${formatDouble(denseResult.second.toDouble() / V, 2)}")
}