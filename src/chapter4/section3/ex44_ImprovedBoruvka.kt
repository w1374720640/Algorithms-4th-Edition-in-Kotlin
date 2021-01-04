package chapter4.section3

import chapter1.section3.DoublyLinkedList
import chapter1.section3.addTail
import chapter1.section3.forwardIterator
import chapter1.section3.isEmpty
import edu.princeton.cs.algs4.Queue
import extensions.formatDouble
import kotlin.math.abs

/**
 * 改进的Boruvka算法
 * 给出Boruvka算法的另一种实现，用双向环形链表表示最小生成树的子树，
 * 使得子树可以被合并或改名，每个阶段所需的时间与E成正比（这样就不需要union-find数据结构了）
 *
 * 解：在每次循环的过程中，创建一个双向链表的数组（可以不是环形），链表中存放顶点的索引，
 * 使用链表数组替代union-find数据结构，每个链表代表一个最小生成树的子树，用链表头的顶点作为连通分量的id，
 * 合并链表A、B的过程中，将链表B的头部添加到链表A的尾部中，以链表B中所有的顶点为索引，将链表数组赋值为链表A
 * 原理和[chapter1.section5.QuickFindUF]类似，find的效率为O(1)，union的效率为O(N)
 * 遍历一次的时间复杂度为O(E)+O(V) ~O(E)，所以总时间复杂度仍然为O(ElgV)
 * 虽然时间复杂度相同，但效率确实降低了，可能有更好的实现方式（因为我的链表不需要是环形的）
 */
class ImprovedBoruvkaMST(graph: EWG) : MST {
    private val queue = Queue<Edge>()
    private var weight = 0.0

    init {
        // 初始化所有子树，每个子树中只有一个顶点
        val trees = Array(graph.V) {
            DoublyLinkedList<Int>().apply {
                addTail(it)
            }
        }
        var i = 1
        while (i <= graph.V && queue.size() < graph.V - 1) {
            val array = arrayOfNulls<Edge>(graph.V)
            graph.edges().forEach { edge ->
                val v = edge.either()
                val w = edge.other(v)
                val j = trees[v].first!!.item
                val k = trees[w].first!!.item
                if (j != k) {
                    if (array[j] == null || edge < array[j]!!) array[j] = edge
                    if (array[k] == null || edge < array[k]!!) array[k] = edge
                }
            }
            array.forEach { edge ->
                if (edge != null) {
                    val v = edge.either()
                    val w = edge.other(v)
                    val listV = trees[v]
                    val listW = trees[w]
                    if (listV.first!!.item != listW.first!!.item) {
                        queue.enqueue(edge)
                        weight += edge.weight

                        listV.addListTail(listW)
                        listW.forwardIterator().forEach {
                            trees[it] = listV
                        }
                    }
                }
            }
            i *= 2
        }
    }

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

/**
 * 在双向链表头部添加一个新的链表
 */
fun <T> DoublyLinkedList<T>.addListHeader(list: DoublyLinkedList<T>) {
    if (list.isEmpty()) return
    if (this.isEmpty()) {
        this.first = list.first
        this.last = list.last
        return
    }
    list.last!!.next = this.first
    this.first!!.previous = list.last
    this.first = list.first
}

/**
 * 在双向链表尾部添加一个新的链表
 */
fun <T> DoublyLinkedList<T>.addListTail(list: DoublyLinkedList<T>) {
    if (list.isEmpty()) return
    if (this.isEmpty()) {
        this.first = list.first
        this.last = list.last
        return
    }
    this.last!!.next = list.first
    list.first!!.previous = this.first
    this.last = list.last
}


fun main() {
    val graph1 = getTinyWeightedGraph()
    val mst1 = ImprovedBoruvkaMST(graph1)
    println(mst1)

    val graph2 = getMediumWeightedGraph()
    val mst2 = KruskalMST(graph2)
    val mst3 = ImprovedBoruvkaMST(graph2)
    println(mst2.weight())
    println(mst3.weight())
    // 如果两个浮点数差值小于某个很小值，则认为两个数相等
    println("mst2==mst3: ${abs(mst2.weight() - mst3.weight()) < 1E-12}")
}