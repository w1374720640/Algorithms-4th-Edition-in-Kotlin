package chapter4.section3

import chapter1.section5.CompressionWeightedQuickUnionUF
import chapter2.swap
import edu.princeton.cs.algs4.Stack
import extensions.shuffle
import extensions.spendTimeMillis

/**
 * 切分
 * 根据快速排序的切分思想（而非使用优先队列）实现一种新方法，检查Kruskal算法中的当前边是否属于最小生成树
 *
 * 解：1、先将所有的边存放于数组内，随机打乱数组
 * 2、以第一个元素为分割点，将数组分割为大于它和小于它的两部分
 * 3、不断对左侧部分分割，直到左侧（包括分割点）边的数量数量小于等于V-1（为了优化分割效率，范围可以适当扩大）
 * 4、对左侧的所有元素排序，再依次判断所有元素是否是最小生成树的边
 * 5、左侧遍历完成后，比较现有最小生成树的大小和V-1的差值X
 * 6、如果X不等于0，使用第2步和第3步的方法在右侧切分出小于等于X的范围，重复第4步，第5步
 * 7、如果X等于0，程序结束，得到最小生成树
 */
class PartitioningKruskalMST(graph: EWG) : MST() {

    init {
        val edges = graph.edges()
        val iterator = edges.iterator()
        val array = Array(edges.count()) { iterator.next() }
        array.shuffle()

        val uf = CompressionWeightedQuickUnionUF(graph.V)
        // 存放分割点，用于在第6步的过程中确定切分的范围
        val stack = Stack<Int>()
        stack.push(array.size - 1)
        stack.push(0)
        while (stack.size() >= 2 && queue.size() < graph.V - 1) {
            val start = stack.pop()
            val end = stack.peek() // 这里用peek()，不弹出
            val p = partition(array, start, end)
            if (p - start < graph.V - 1 - queue.size() + 20) {
                // 如果分割后的左侧范围比需要最大数量少，将左侧所有元素排序后依次插入
                // 这里为了提高分割效率，比需要的数量多20个也直接排序后插入
                if (p != start) array.sort(start, p)
                for (i in start..p) {
                    val edge = array[i]
                    val v = edge.either()
                    val w = edge.other(v)
                    if (uf.connected(v, w)) continue
                    queue.enqueue(edge)
                    weight += edge.weight
                    uf.union(v, w)
                }
                if (p + 1 < end) stack.push(p + 1)
            } else {
                // 如果分割后的左侧范围比需要的数量多，则将左侧继续分割
                stack.push(p - 1)
                stack.push(start)
            }
        }
    }


    private fun partition(array: Array<Edge>, start: Int, end: Int): Int {
        val firstEdge = array[start]
        var i = start + 1
        var j = end
        while (true) {
            while (i <= j && array[i] <= firstEdge) i++
            while (i <= j && array[j] >= firstEdge) j--
            if (i >= j) break
            array.swap(i, j)
        }
        if (j != start) array.swap(start, j)
        return j
    }
}

fun main() {
    val graph = getMediumWeightedGraph()
    val mst1 = KruskalMST(graph)
    val mst2 = PartitioningKruskalMST(graph)
    println("mst1==mst2: ${mst1.weight() == mst2.weight()}")

    val V = 5000
    // 获取一个随机稠密图，E=V*(V-1)/2
    val denseEWG = getDenseGraph(V)
    var mst3: MST? = null
    val time3 = spendTimeMillis {
        mst3 = KruskalMST(denseEWG)
    }
    println("time3=$time3 ms")
    var mst4: MST? = null
    val time4 = spendTimeMillis {
        mst4 = PartitioningKruskalMST(denseEWG)
    }
    println("time4=$time4 ms")
    println("mst3==mst4: ${mst3!!.weight() == mst4!!.weight()}")
}