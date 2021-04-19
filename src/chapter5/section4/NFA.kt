package chapter5.section4

import chapter4.section2.Digraph
import chapter4.section2.DirectedDFS
import edu.princeton.cs.algs4.Bag
import edu.princeton.cs.algs4.Stack

class NFA(val regex: String) {
    val M = regex.length
    val digraph = Digraph(M + 1)

    init {
        val stack = Stack<Int>()
        var i = 0
        while (i < M) {
            when (regex[i]) {
                '(' -> {
                    digraph.addEdge(i, i + 1)
                    stack.push(i)
                }
                '|' -> {
                    stack.push(i)
                }
                '*' -> {
                    // 单字符的闭包
                    digraph.addEdge(i, i - 1)
                    digraph.addEdge(i - 1, i)
                    digraph.addEdge(i, i + 1)
                }
                ')' -> {
                    var left = stack.pop()
                    if (regex[left] == '|') {
                        val or = left
                        left = stack.pop()
                        digraph.addEdge(left, or + 1)
                        digraph.addEdge(or, i)
                    }
                    digraph.addEdge(i, i + 1)
                    if (i < M - 1 && regex[i + 1] == '*') {
                        // 括号外的闭包
                        i++
                        digraph.addEdge(left, i)
                        digraph.addEdge(i, left)
                        digraph.addEdge(i, i + 1)
                    }
                }
            }
            i++
        }
    }

    fun recognizes(txt: String): Boolean {
        val N = txt.length
        var i = 0
        // 存放所有经过ε-转换等价的点，代码保证了所有点不会重复，所以直接用Bag不需要用Set
        var equalSet = Bag<Int>()
        var dfs = DirectedDFS(digraph, 0)
        for (j in 0..M) {
            if (dfs.marked(j)) {
                equalSet.add(j)
            }
        }
        while (i < N) {
            // 存放匹配转换后的点，所有点不会重复
            val matchSet = Bag<Int>()
            equalSet.forEach {
                if (it < M && (regex[it] == '.' || regex[it] == txt[i])) {
                    matchSet.add(it + 1)
                }
            }
            i++
            if (matchSet.isEmpty) return false
            equalSet = Bag()
            // 找到match中所有点为起点可达的所有顶点
            dfs = DirectedDFS(digraph, matchSet)
            for (j in 0..M) {
                if (dfs.marked(j)) {
                    equalSet.add(j)
                }
            }
        }
        equalSet.forEach {
            if (it == M) return true
        }
        return false
    }
}

fun main() {
    testNFA { NFA(it) }
}