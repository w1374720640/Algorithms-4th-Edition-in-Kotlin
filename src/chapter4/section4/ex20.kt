package chapter4.section4

import edu.princeton.cs.algs4.In
import edu.princeton.cs.algs4.Queue
import extensions.formatDouble
import extensions.spendTimeMillis
import kotlin.math.E
import kotlin.math.ln
import kotlin.math.min
import kotlin.math.pow

/**
 * 从网上或者报纸上找到一张汇率表并用它构造一张套汇表。
 * 注意：不要使用根据若干数据计算得出的汇率表，它们的精度有限。
 * 附加题：从汇率市场上赚点外快！
 */
fun ex20(digraph: EdgeWeightedDigraph): MinWeightCycleFinder.Path? {
    var minPath: MinWeightCycleFinder.Path? = null
    for (s in 0 until digraph.V) {
        val finder = MinWeightCycleFinder(digraph, s)
        val path = finder.getPath()
        if (path != null) {
            if (minPath == null || path < minPath) {
                minPath = path
            }
        }
    }
    return minPath
}

/**
 * 获取一张真实汇率表
 * 原始数据来源：https://www.xe.com/zh-CN/currencytables/?from=USD&date=2021-01-01
 * 获取2021年1月1日167种货币之间的兑换比率，
 * 处理后的数据放在Github的另一个仓库中，可自行下载后放入data目录中，链接如下：
 * https://github.com/w1374720640/utility-room/blob/main/Algorithms/realRates.txt
 */
fun getRealRates(size: Int): Pair<Array<String>, EdgeWeightedDigraph> {
    val input = In("./data/realRates.txt")
    val V = min(size, input.readLine().toInt())
    val names = Array(V) { "" }
    val digraph = EdgeWeightedDigraph(V)
    repeat(V) { v ->
        val list = input.readLine().split(Regex(" +"))
        names[v] = list[0]
        repeat(V) { w ->
            val ratio = list[w + 1].toDouble()
            digraph.addEdge(DirectedEdge(v, w, -1.0 * ln(ratio)))
        }
    }
    return names to digraph
}

fun main() {
    var size = 0
    repeat(9) {
        size += 20
        val pair = getRealRates(size)
        val names = pair.first
        val digraph = pair.second
        println("V=${names.size}")
        val time = spendTimeMillis {
            val minPath = ex20(digraph)
            if (minPath == null) {
                println("Does not contain cycle")
            } else {
                val iterator = minPath.iterator()
                val firstEdge = iterator.next()
                val queue = Queue<Int>()
                queue.enqueue(firstEdge.from())
                queue.enqueue(firstEdge.to())
                while (iterator.hasNext()) {
                    val edge = iterator.next()
                    queue.enqueue(edge.to())
                }
                // 计算利润
                val profit = E.pow(minPath.weight * -1.0) - 1
                println("profit=${formatDouble(profit, 10)} minPath: ${minPath.joinToString()}")
                // 打印环中货币的名称，长度最大为V+1
                println("size: ${queue.size()}  cycle: ${queue.joinToString { names[it] }}")
            }
        }
        println("spend $time ms")
        println()
    }
}