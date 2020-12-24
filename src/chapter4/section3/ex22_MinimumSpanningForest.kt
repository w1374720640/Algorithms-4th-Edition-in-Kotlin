package chapter4.section3

import chapter1.section5.CompressionWeightedQuickUnionUF
import chapter2.section4.HeapIndexMinPriorityQueue
import edu.princeton.cs.algs4.Queue
import extensions.formatDouble

/**
 * 最小生成森林
 * 开发新版的Prim算法和Kruskal算法来计算一幅加权图的最小生成森林，图不一定是连通的。
 * 使用4.1节中连通分量的API并找到每个连通分量的最小生成树。
 *
 * 解：最小生成森林的数量等于连通分量的数量，每个连通分量的id为森林中的一个顶点
 * 分别以每个连通分量中的一个顶点为起点，使用Prim算法或Kruskal算法求单个最小生成树
 */
class PrimMSF(graph: EWG) : MSF {
    private val trees = ArrayList<InnerPrimMST>()
    private val ids = IntArray(graph.V)
    private var id = 0

    private val marked = BooleanArray(graph.V)
    private val indexMinPQ = HeapIndexMinPriorityQueue<Edge>(graph.V)
    private var weight = 0.0

    init {
        for (s in 0 until graph.V) {
            if (marked[s]) continue
            val mst = InnerPrimMST()
            trees.add(mst)
            visit(graph, s)
            while (!indexMinPQ.isEmpty()) {
                val edge = indexMinPQ.min()
                indexMinPQ.delMin()
                mst.queue.enqueue(edge)
                mst.weight += edge.weight
                weight += edge.weight
                val v = edge.either()
                val w = edge.other(v)
                if (!marked[v]) visit(graph, v)
                if (!marked[w]) visit(graph, w)
            }
            id++
        }
        // 最小生成树的数量应该和连通分量的数量相等
        check(id == trees.size)
    }

    private fun visit(graph: EWG, v: Int) {
        marked[v] = true
        ids[v] = id
        graph.adj(v).forEach {
            val w = it.other(v)
            if (!marked[w]) {
                if (!indexMinPQ.contains(w) || indexMinPQ[w]!!.weight > it.weight) {
                    indexMinPQ[w] = it
                }
            }
        }
    }

    override fun count(): Int {
        return id
    }

    override fun tree(v: Int): MST {
        return trees[ids[v]]
    }

    override fun trees(): Iterable<MST> {
        return trees
    }

    private class InnerPrimMST : MST {
        val queue = Queue<Edge>()
        var weight = 0.0

        override fun edges(): Iterable<Edge> {
            return queue
        }

        override fun weight(): Double {
            return weight
        }

        override fun toString(): String {
            val stringBuilder = StringBuilder()
                    .append("weight=")
                    .append(formatDouble(weight, 2))
                    .append("\n")
            queue.forEach {
                stringBuilder.append(it.toString())
                        .append("\n")
            }
            return stringBuilder.toString()
        }
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
                .append("count=")
                .append(count())
                .append(" weight=")
                .append(formatDouble(weight, 2))
                .append("\n")
        trees.forEach {
            stringBuilder.append(it.toString())
        }
        return stringBuilder.toString()
    }
}

/**
 * 最小生成森林的Kruskal算法
 */
class KruskalMSF(graph: EWG) : MSF {
    private val cc = EdgeWeightedGraphCC(graph)
    private val trees = Array(cc.count()) { InnerKruskalMST() }
    private val ids = IntArray(graph.V) { -1 }

    // 预处理，为每个连通分量分配一个最小生成树
    init {
        var index = 0
        for (v in 0 until graph.V) {
            val id = cc.id(v)
            if (ids[id] == -1) {
                ids[id] = index
                ids[v] = index
                index++
            } else {
                ids[v] = ids[id]
            }
        }
    }

    private val edges = ArrayList<Edge>()
    private val uf = CompressionWeightedQuickUnionUF(graph.V)
    private var weight = 0.0

    init {
        var count = 0
        graph.edges().forEach {
            edges.add(it)
        }
        edges.sort()
        for (edge in edges) {
            val v = edge.either()
            val w = edge.other(v)
            if (uf.find(v) != uf.find(w)) {
                // 最终会连通但还未连通时，找到对应的最小生成树，加入树中
                val mst = trees[ids[v]]
                mst.queue.enqueue(edge)
                mst.weight += edge.weight
                weight += edge.weight
                uf.union(v, w)
                count++
            }
            if (count == graph.V - 1) break
        }
    }

    override fun count(): Int {
        return cc.count()
    }

    override fun tree(v: Int): MST {
        return trees[ids[v]]
    }

    override fun trees(): Iterable<MST> {
        // 数组没有实现Iterable接口，转换一下
        return trees.asIterable()
    }

    private class InnerKruskalMST : MST {
        val queue = Queue<Edge>()
        var weight = 0.0

        override fun edges(): Iterable<Edge> {
            return queue
        }

        override fun weight(): Double {
            return weight
        }

        override fun toString(): String {
            val stringBuilder = StringBuilder()
                    .append("weight=")
                    .append(formatDouble(weight, 2))
                    .append("\n")
            queue.forEach {
                stringBuilder.append(it.toString())
                        .append("\n")
            }
            return stringBuilder.toString()
        }
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
                .append("count=")
                .append(count())
                .append(" weight=")
                .append(formatDouble(weight, 2))
                .append("\n")
        trees.forEach {
            stringBuilder.append(it.toString())
        }
        return stringBuilder.toString()
    }

}

private fun getForestEWG(): EdgeWeightedGraph {
    val graph = EdgeWeightedGraph(10)
    graph.addEdge(Edge(0, 7, 0.2))
    graph.addEdge(Edge(7, 5, 0.3))
    graph.addEdge(Edge(5, 3, 0.4))
    graph.addEdge(Edge(3, 0, 0.5))
    graph.addEdge(Edge(0, 5, 0.1))
    graph.addEdge(Edge(4, 2, 0.3))
    graph.addEdge(Edge(2, 6, 0.4))
    graph.addEdge(Edge(6, 4, 0.2))
    graph.addEdge(Edge(1, 8, 0.3))
    return graph
}

fun main() {
    val graph1 = getForestEWG()
    val primMSF = PrimMSF(graph1)
    println("PrimMSF:")
    println(primMSF.toString())
    println(primMSF.tree(4))

    val graph2 = getForestEWG()
    val kruskalMSF = KruskalMSF(graph2)
    println("KruskalMSF:")
    println(kruskalMSF.toString())
    println(kruskalMSF.tree(8))
}