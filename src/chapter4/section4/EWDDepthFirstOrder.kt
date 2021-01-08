package chapter4.section4

import edu.princeton.cs.algs4.Queue
import edu.princeton.cs.algs4.Stack

/**
 * 加权有向图中基于深度优先搜索的顶点排序
 * 参考[chapter4.section2.DepthFirstOrder]
 */
class EWDDepthFirstOrder(digraph: EdgeWeightedDigraph) {
    private val marked = BooleanArray(digraph.V)
    private val pre = Queue<Int>()
    private val post = Queue<Int>()
    private val reversePost = Stack<Int>()

    init {
        for (v in 0 until digraph.V) {
            if (!marked[v]) {
                dfs(digraph, v)
            }
        }
    }

    private fun dfs(digraph: EdgeWeightedDigraph, v: Int) {
        marked[v] = true
        pre.enqueue(v)
        digraph.adj(v).forEach { edge ->
            val w = edge.to()
            if (!marked[w]) {
                dfs(digraph, w)
            }
        }
        post.enqueue(v)
        reversePost.push(v)
    }

    /**
     * 所有顶点的前序排列
     */
    fun pre(): Iterable<Int> {
        return pre
    }

    /**
     * 所有顶点的后序排列
     */
    fun post(): Iterable<Int> {
        return post
    }

    /**
     * 所有顶点的逆后续排列
     */
    fun reversePost(): Iterable<Int> {
        return reversePost
    }
}