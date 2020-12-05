package chapter4.section2

import chapter1.section3.SinglyLinkedList
import chapter1.section3.add
import chapter1.section3.forEach
import chapter1.section3.isEmpty

/**
 * 有向无环图中的LCA
 * 给定一幅有向无环图和两个顶点v和w，找出v和w的LCA（Lowest Common Ancestor， 最近共同祖先）。
 * LCA的计算再实现编程语言的多重继承、分析家谱数据（找出家族中近亲繁衍的程度）和其他一些应用中很有用。
 * 提示：将有向无环图中的顶点v的高度定义为从根结点到v的最长路径。
 * 在所有v和w的共同祖先中，高度最大者就是v和w的最近共同组先。
 *
 * 解：共同祖先查找要求有唯一的一个根结点，所有顶点从子结点指向父结点，最终都指向根结点
 * 1、分别获取v和w顶点的可达顶点，O(V+E)
 * 2、遍历所有顶点，找到在有向图中v和w同时可达的所有顶点，即所有的共同祖先，O(V)
 * 3、遍历所有顶点，找到出度为0的根结点，O(V)
 * 4、获取有向图的反向图，通过广度优先遍历，找到与根结点距离最远的共同祖先，就是与v和w最近的共同祖先，O(V+E)
 */
fun ex21_LCAInDAG(digraph: Digraph, v: Int, w: Int): Int {
    val vPaths = BreadthFirstDirectedPaths(digraph, v)
    val wPaths = BreadthFirstDirectedPaths(digraph, w)

    val linkedList = SinglyLinkedList<Int>()
    for (i in 0 until digraph.V) {
        if (vPaths.hasPathTo(i) && wPaths.hasPathTo(i)) {
            linkedList.add(i)
        }
    }
    if (linkedList.isEmpty()) return -1

    var root = -1
    for (i in 0 until digraph.V) {
        if (!digraph.adj(i).iterator().hasNext()) {
            if (root == -1) {
                root = i
            } else {
                throw IllegalStateException("The Digraph should have only one root node")
            }
        }
    }
    check(root != -1) { "The Digraph should have only one root node" }

    val reverseDigraph = digraph.reverse()
    val rootPaths = BreadthFirstDirectedPaths(reverseDigraph, root)
    var s = -1
    var maxDistance = -1
    linkedList.forEach {
        val distance = rootPaths.distTo(it)
        if (distance > maxDistance) {
            maxDistance = distance
            s = it
        }
    }
    return s
}

fun main() {
    val digraph = Digraph(6)
    digraph.addEdge(1, 0)
    digraph.addEdge(2, 1)
    digraph.addEdge(3, 2)
    digraph.addEdge(4, 2)
    digraph.addEdge(5, 4)

    println("LCA=${ex21_LCAInDAG(digraph, 3, 5)}")
}