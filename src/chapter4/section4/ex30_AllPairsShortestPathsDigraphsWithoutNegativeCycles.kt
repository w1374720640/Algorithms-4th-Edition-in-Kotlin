package chapter4.section4

import edu.princeton.cs.algs4.Queue
import extensions.formatDouble

/**
 * 含有负权重环的图中的任意顶点对之间的最短路径
 * 参考4.4.4.3节框注“任意顶点对之间的最短路径”所实现的不含负权重的图中的任意顶点对之间的最短路径问题并设计一份API。
 * 使用Bellman-Ford算法的一个变种来确定权重数组pi[]，使得对于任意边v->w，边的权重加上pi[v]和pi[w]之差的和非负。
 * 然后更新所有边的权重，使得Dijkstra算法可以在新图中找出所有的最短路径。
 *
 * 解：这题题目有误，书上说的是“含有负权重环的图”，但实际上应该是“不含负权重环的图”，可以含有负权重边
 * 图的顶点数量为V，创建一个顶点数量为V+1的新图，新顶点为s，添加V个从顶点s指向其他顶点的边，权重为0，O(V+E)
 * 使用Bellman-Ford算法求从新顶点s到达其他任意顶点的最短路径， O(V*E)
 * pi数组为从新顶点s到其他所有顶点的最短路径的权重值，
 * 证明边的权重加上pi[v]减去pi[w]为非负值：
 * 若原图有两个顶点v、w，一条从v指向w的边，边的权重为e
 *     若e==0，则pi[w]<=pi[v]，e+pi[v]-pi[w]>=0
 *     若e>0，则pi[w]<=pi[v]，e+pi[v]-pi[w]>=e>0
 *     若e<0，则pi[w]<=pi[v]+e，e+pi[v]-pi[w]>=0
 * 创建一个大小为V的新图，使用e+pi[v]-pi[w]作为新图中每条边的权重，O(V+E)
 * 若原始图中v->w边在顶点w的最短路径上，则新图中v->w边也在顶点w的最短路径上
 * 使用Dijkstra算法找出任意两个顶点之间的最短距离（图中不含负权重边），O(V*ElgV)
 * 顶点w的最短路径为dist[w]，则e=dist[w]-pi[v]+pi[w]
 *
 * 求任意两个顶点之间的最短路径时，如果直接使用Bellman-Ford算法循环V次，时间复杂度为O(V*EV)
 * 使用这题中的优化算法，时间复杂度会降低到O(V*ElgV)
 */
class DigraphWithoutNegativeCyclesAllPairsSP(digraph: EdgeWeightedDigraph) {
    private val dijkstraSPArray: Array<DijkstraSP>
    private val pi: Array<Double>

    init {
        val bellmanDigraph = EdgeWeightedDigraph(digraph.V + 1)
        digraph.edges().forEach { bellmanDigraph.addEdge(it) }
        repeat(digraph.V) {
            bellmanDigraph.addEdge(DirectedEdge(digraph.V, it, 0.0))
        }

        val bellmanFordSP = BellmanFordSP(bellmanDigraph, digraph.V)
        check(!bellmanFordSP.hasNegativeCycle()) { bellmanFordSP.toString() } // 只能处理不含负权重环的图

        pi = Array(digraph.V) { Double.POSITIVE_INFINITY }
        repeat(digraph.V) {
            pi[it] = bellmanFordSP.distTo(it)
        }

        val dijkstraDigraph = EdgeWeightedDigraph(digraph.V)
        digraph.edges().forEach {
            // 将边的权重转换为非负数
            dijkstraDigraph.addEdge(DirectedEdge(it.from(), it.to(), it.weight + pi[it.from()] - pi[it.to()]))
        }

        dijkstraSPArray = Array(digraph.V) {
            DijkstraSP(dijkstraDigraph, it)
        }
    }


    fun dist(v: Int, w: Int): Double {
        val distance = dijkstraSPArray[v].distTo(w)
        if (distance == Double.POSITIVE_INFINITY) return distance
        return distance - pi[v] + pi[w]
    }

    fun hasPath(v: Int, w: Int): Boolean {
        return dijkstraSPArray[v].distTo(w) != Double.POSITIVE_INFINITY
    }

    fun path(v: Int, w: Int): Iterable<DirectedEdge>? {
        if (!hasPath(v, w)) return null
        val path = dijkstraSPArray[v].pathTo(w)
        check(path != null)
        val queue = Queue<DirectedEdge>()
        path.forEach {
            // 还原每条边的真实权重
            queue.enqueue(DirectedEdge(it.from(), it.to(), it.weight - pi[it.from()] + pi[it.to()]))
        }
        return queue
    }
}

fun main() {
    val digraph = getTinyEWDn()
    val allPairsSP1 = DigraphWithoutNegativeCyclesAllPairsSP(digraph)
    for (v in 0 until digraph.V) {
        // 对比直接用Bellman-Ford算法和本题给出算法的结果是否相同
        val bellmanFordSP1 = BellmanFordSP(digraph, v)
        for (w in 0 until digraph.V) {
            println("v=$v w=$w")
            println("bellman distance=${formatDouble(bellmanFordSP1.distTo(w), 2)} path=${bellmanFordSP1.pathTo(w)?.joinToString()}")
            println("allPair distance=${formatDouble(allPairsSP1.dist(v, w), 2)} path=${allPairsSP1.path(v, w)?.joinToString()}")
        }
    }
}