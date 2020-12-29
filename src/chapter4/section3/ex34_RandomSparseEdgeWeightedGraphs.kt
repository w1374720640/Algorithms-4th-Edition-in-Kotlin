package chapter4.section3

import edu.princeton.cs.algs4.StdRandom
import extensions.random

/**
 * 随机稀疏加权图
 * 基于你为练习4.1.40给出的解答编写一个随机稀疏加权图生成器。
 * 在赋予边的权重时，定义一个随机加权图的抽象数据结构并给出两种实现：
 * 一种按均匀分布生成权重，另一种按高斯分布生成权重。
 * 编写用例程序，用两种权重分布和一组精心挑选过的V和E的值生成随机的稀疏加权图，
 * 使得我们可以用它对权重的各种分布进行有意义的经验性测试。
 */
/**
 * 权重均匀分布的随机稀疏图，ratio表示每个顶点平均有多少条边
 */
class RandomSparseEvenlyEWG(V: Int = 100, ratio: Int = 5) : EdgeWeightedGraph(V) {
    init {
        // 先给每个顶点都添加一条边，尽量保证图是连通的
        repeat(V) {
            val edge = Edge(it, random(V), random())
            addEdge(edge)
        }
        repeat(V * (ratio - 1)) {
            val edge = Edge(random(V), random(V), random())
            addEdge(edge)
        }
    }
}

/**
 * 权重高斯分布的随机稀疏图，ratio表示每个顶点平均有多少条边
 * 默认的高斯分布函数均值为0，标准差为1
 */
class RandomSparseGaussianEWG(V: Int = 100, ratio: Int = 5) : EdgeWeightedGraph(V) {
    init {
        // 先给每个顶点都添加一条边，尽量保证图是连通的
        repeat(V) {
            val edge = Edge(it, random(V), StdRandom.gaussian())
            addEdge(edge)
        }
        repeat(V * (ratio - 1)) {
            val edge = Edge(random(V), random(V), StdRandom.gaussian())
            addEdge(edge)
        }
    }
}

fun main() {
    val evenlyEWG = RandomSparseEvenlyEWG()
    val gaussianEWG = RandomSparseGaussianEWG()
    println(gaussianEWG)
}