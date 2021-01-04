package chapter4.section3

import chapter1.section5.CompressionWeightedQuickUnionUF
import kotlin.math.abs

/**
 * Boruvka算法
 * 实现Boruvka算法：和Kruskal算法类似，只是分阶段地向一组森林中逐渐添加边来构造一颗最小生成树。
 * 在每个阶段中，找出所有连接两棵不同的树的权重最小的边，并将它们全部加入最小生成树。
 * 为了避免出现环，假设所有边的权重均不相同。
 * 提示：维护一个由顶点索引的数组来辨别连接每棵树和它最近的邻居的边。
 * 记得用上union-find数据结构
 *
 * 解：1、创建一个union-find的数据结构
 * 2、创建一个大小为V的边的数组
 * 3、遍历所有边，如果一个边的两个顶点不在一个连通分量中，分别以两个连通分量的id（不是顶点值）为索引，
 *    在数组中找到对应位置，如果为空或新的边权重更小，则更新数组中的值
 * 4、所有边遍历完成后，数组中的所有边都加入最小生成树中（需要去除重复边）
 * 5、重复步骤2到步骤4，直到最小生成树大小为V-1
 * 由于每个循环中连通分量至少减半（一个连通分量会和另一个或多个连通分量相连）
 * 所以最多需要lgV次遍历就可以找到唯一的一个连通分量
 * 遍历一次的时间复杂度为O(E)，总时间复杂度为O(ElgV)
 */
class BoruvkaMST(graph: EWG) : MST() {

    init {
        val uf = CompressionWeightedQuickUnionUF(graph.V)
        var i = 1
        while (i <= graph.V && queue.size() < graph.V - 1) {
            val array = arrayOfNulls<Edge>(graph.V)
            graph.edges().forEach { edge ->
                val v = edge.either()
                val w = edge.other(v)
                val j = uf.find(v)
                val k = uf.find(w)
                if (j != k) {
                    // 以j、k为索引在数组中查找，而不是以v、w为索引
                    if (array[j] == null || edge < array[j]!!) array[j] = edge
                    if (array[k] == null || edge < array[k]!!) array[k] = edge
                }
            }
            array.forEach { edge ->
                if (edge != null) {
                    val v = edge.either()
                    val w = edge.other(v)
                    if (!uf.connected(v, w)) {
                        queue.enqueue(edge)
                        weight += edge.weight
                        uf.union(v, w)
                    }
                }
            }
            i *= 2
        }
    }
}

fun main() {
    val graph1 = getTinyWeightedGraph()
    val mst1 = BoruvkaMST(graph1)
    println(mst1)

    val graph2 = getMediumWeightedGraph()
    val mst2 = KruskalMST(graph2)
    val mst3 = BoruvkaMST(graph2)
    println(mst2.weight())
    println(mst3.weight())
    // 如果两个浮点数差值小于某个很小值，则认为两个数相等
    println("mst2==mst3: ${abs(mst2.weight() - mst3.weight()) < 1E-12}")
}