package chapter5.section4

import chapter4.section2.Digraph
import edu.princeton.cs.algs4.Bag
import edu.princeton.cs.algs4.Queue
import edu.princeton.cs.algs4.Stack

/**
 * 证明
 * 开发一个新版本的NFA，使它能够打印一份证明，指出给定字符串包含在NFA能够识别的语言之中
 * （即终止于接受状态的一系列状态转换）
 *
 * 解：继承NFA类，构造有向图时逻辑不变，匹配字符串时不直接使用Int类型来记录位置，
 * 而是使用一个带有指向前一个位置指针的对象记录位置，
 * 同时重写有向图的广度优先路径搜索，判断可达性的同时记录路径，
 * 当NFA终止于接受状态时，找到最终状态，依次将其以及上一个位置加入栈中，最后打印栈的内容就是识别的路径
 */
class PrintPathNFA(regex: String) : NFA(regex) {

    private class Node(val index: Int, var pre: Node? = null)

    /**
     * 使用广度优先搜索来查找从给定起点列表可达的所有点，同时记录每个点的上下级关系
     */
    private class BreadFirstSearch(private val digraph: Digraph, source: Iterable<Node>) {
        private val marked = BooleanArray(digraph.V)
        private val edgeTo = Array<Node?>(digraph.V) { null }

        init {
            val queue = Queue<Node>()
            source.forEach {
                queue.enqueue(it)
                marked[it.index] = true
                edgeTo[it.index] = it
            }
            while (!queue.isEmpty) {
                val node = queue.dequeue()
                digraph.adj(node.index).forEach { w ->
                    if (!marked[w]) {
                        marked[w] = true
                        val newNode = Node(w, node)
                        edgeTo[w] = newNode
                        queue.enqueue(newNode)
                    }
                }
            }
        }

        fun getMarkedNode(): Bag<Node> {
            val bag = Bag<Node>()
            for (i in 0 until digraph.V) {
                if (marked[i]) {
                    bag.add(edgeTo[i]!!)
                }
            }
            return bag
        }
    }

    init {
        println("\nregex=$regex")
    }

    override fun recognizes(txt: String): Boolean {
        println("txt=$txt")
        val N = txt.length
        var i = 0
        // 使用重写的广度优先查找，可以返回所有的可达点以及其上一个节点
        var bfs = BreadFirstSearch(digraph, listOf(Node(0)))
        var equalSet = bfs.getMarkedNode()
        while (i < N) {
            val char = txt[i]
            val matchSet = Bag<Node>()
            val iterator = equalSet.iterator()
            while (iterator.hasNext()) {
                val node = iterator.next()
                val j = node.index
                if (j == M) continue
                when (regex[j]) {
                    '.' -> addMatch(matchSet, node)
                    '^' -> {
                        var k = j + 1
                        var hasMatch = false
                        while (!hasMatch && regex[k] != ']') {
                            if (regex[k] == '-') {
                                hasMatch = char > regex[k - 1] && char < regex[k + 1]
                            } else {
                                hasMatch = char == regex[k]
                            }
                            k++
                        }
                        if (!hasMatch) { // 只有在集合中匹配失败才是在补集中匹配成功
                            addMatch(matchSet, node)
                        }
                    }
                    '-' -> {
                        if (char > regex[j - 1] && char < regex[j + 1]) {
                            addMatch(matchSet, node)
                        }
                    }
                    '\\' -> {
                        if (regex[j + 1] == char) { // 转义字符需要直接和后一个字符匹配
                            addMatch(matchSet, node)
                        }
                    }
                    // 在可以使用转义字符的情况下，所有元字符都必须特殊处理，不能放到else里
                    '*', '+', '?', '(', ')', '[', ']', '|' -> {
                    }
                    else -> {
                        if (regex[j] == char) {
                            addMatch(matchSet, node)
                        }
                    }
                }
            }
            i++
            if (matchSet.isEmpty) {
                println("match failed.")
                return false
            }
            // 找到match中所有点为起点可达的所有顶点
            bfs = BreadFirstSearch(digraph, matchSet)
            equalSet = bfs.getMarkedNode()
        }
        equalSet.forEach {
            if (it.index == M) {
                val stack = Stack<Node>()
                var node = it
                while (node != null) {
                    stack.push(node)
                    node = node.pre
                }
                println("match succeed, path=${stack.joinToString(separator = "->") { it.index.toString() }}")
                return true
            }
        }
        println("match failed.")
        return false
    }

    /**
     * 将匹配转换的下一个元素添加到背包中，每个匹配转换的下一个元素都不相同
     */
    private fun addMatch(bag: Bag<Node>, node: Node) {
        check(match[node.index] != -1) { "Match conversion should not be -1" }
        bag.add(Node(match[node.index], node))
    }
}

fun main() {
    testNFA { PrintPathNFA(it) }
}