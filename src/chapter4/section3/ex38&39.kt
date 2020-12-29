package chapter4.section3

import extensions.formatStringLength
import extensions.spendTimeMillis
import kotlin.math.PI
import kotlin.math.log2
import kotlin.math.sqrt

/**
 * 练习4.3.38：延时的代价
 * 对于各种图的模型，运行实验并根据经验比较Prim算法的延时版本和即时版本的性能差异
 *
 * 练习4.3.39：对比Prim算法与Kruskal算法
 * 运行实验并根据经验比较Prim算法的延时版本和即时版本与Kruskal算法的性能差异
 */
fun main() {
    val V = 50000
    // 稀疏图V的值可以适当增大
    val evenlyEWG = RandomSparseEvenlyEWG(V) // 权重均匀分布的随机稀疏图
    val gaussianEWG = RandomSparseGaussianEWG(V) // 权重高斯分布的随机稀疏图
    // 欧几里得图的阈值，大于阈值的图几乎必然连通，使用时阈值乘2
    val threshold = sqrt(log2(V.toDouble()) / (PI * V))
    val euclideanEWG = getRandomEWG(V, threshold * 2).first // 稀疏的欧几里得图
    // 稠密图V的值不能过大
    val denseEWG = getDenseGraph(V / 10) // E=V*(V-1)/2的稠密图

    println("V=$V")
    val evenlyTime = spendTimeMillis {
        PrimMST(evenlyEWG)
    }
    println("${formatStringLength("evenlyTime", 20, true)} = $evenlyTime ms")
    val evenlyLazyTime = spendTimeMillis {
        LazyPrimMST(evenlyEWG)
    }
    println("${formatStringLength("evenlyLazyTime", 20, true)} = $evenlyLazyTime ms")
    val evenlyKruskalTime = spendTimeMillis {
        KruskalMST(evenlyEWG)
    }
    println("${formatStringLength("evenlyKruskalTime", 20, true)} = $evenlyKruskalTime ms")
    println()

    val gaussianTime = spendTimeMillis {
        PrimMST(gaussianEWG)
    }
    println("${formatStringLength("gaussianTime", 20, true)} = $gaussianTime ms")
    val gaussianLazyTime = spendTimeMillis {
        LazyPrimMST(gaussianEWG)
    }
    println("${formatStringLength("gaussianLazyTime", 20, true)} = $gaussianLazyTime ms")
    val gaussianKruskalTime = spendTimeMillis {
        KruskalMST(gaussianEWG)
    }
    println("${formatStringLength("gaussianKruskalTime", 20, true)} = $gaussianKruskalTime ms")
    println()

    val euclideanTime = spendTimeMillis {
        PrimMST(euclideanEWG)
    }
    println("${formatStringLength("euclideanTime", 20, true)} = $euclideanTime ms")
    val euclideanLazyTime = spendTimeMillis {
        LazyPrimMST(euclideanEWG)
    }
    println("${formatStringLength("euclideanLazyTime", 20, true)} = $euclideanLazyTime ms")
    val euclideanKruskalTime = spendTimeMillis {
        KruskalMST(euclideanEWG)
    }
    println("${formatStringLength("euclideanKruskalTime", 20, true)} = $euclideanKruskalTime ms")
    println()

    val denseTime = spendTimeMillis {
        PrimMST(denseEWG)
    }
    println("${formatStringLength("denseTime", 20, true)} = $denseTime ms")
    val denseLazyTime = spendTimeMillis {
        LazyPrimMST(denseEWG)
    }
    println("${formatStringLength("denseLazyTime", 20, true)} = $denseLazyTime ms")
    val denseKruskalTime = spendTimeMillis {
        KruskalMST(denseEWG)
    }
    println("${formatStringLength("denseKruskalTime", 20, true)} = $denseKruskalTime ms")
}