package chapter4.section1

import chapter3.section4.LinearProbingHashST
import edu.princeton.cs.algs4.In
import edu.princeton.cs.algs4.StdIn
import extensions.inputPrompt

/**
 * 实现一个SymbolGraph（不一定必须使用Graph），只需要遍历一遍图的定义数据。
 * 由于需要查找符号表，实现中图的各种操作时耗可能会变为原来的logV倍。
 *
 * 解：SymbolGraph需要遍历两遍是因为在构造Graph对象时必须传递顶点数量
 * 将Graph中的代码移动到SymbolGraph中，在调用G()方法时再根据数据构造一个Graph对象
 * 遍历时，使用一个符号表保存读取到的顶点，如果一个顶点第一次被读取，索引为i，i不断自增，i最终将等于总顶点数
 * 使用一个额外的列表保存每个边，在读取结束时构造Graph对象
 */
class IterateOnceSymbolGraph(fileName: String, delim: String) {
    private val st = LinearProbingHashST<String, Int>()
    private val names = LinearProbingHashST<Int, String>()
    private var V: Int = 0
    private val graph: Graph

    init {
        val edges = ArrayList<Edge>()
        val input = In(fileName)
        while (input.hasNextLine()) {
            val line = input.readLine()
            val list = line.split(delim)
            if (list.size < 2) continue
            val firstValue = list[0]
            var firstIndex = st.get(firstValue)
            if (firstIndex == null) {
                firstIndex = V++
                st.put(firstValue, firstIndex)
                names.put(firstIndex, firstValue)
            }
            for (i in 1 until list.size) {
                val value = list[i]
                var index = st.get(value)
                if (index == null) {
                    index = V++
                    st.put(value, index)
                    names.put(index, value)
                }
                edges.add(Edge(firstIndex, index))
            }
        }
        graph = Graph(V)
        edges.forEach { graph.addEdge(it.small, it.large) }
    }

    fun contains(key: String): Boolean {
        return st.get(key) != null
    }

    fun index(key: String): Int {
        val value = st.get(key)
        if (value == null) {
            throw IllegalArgumentException()
        } else {
            return value
        }
    }

    fun name(v: Int): String {
        require(v < V)
        return names.get(v)!!
    }

    fun G(): Graph {
        return graph
    }
}

fun main() {
    inputPrompt()
    val fileName = "./data/movies.txt"
    val delim = "/"
    val sg = IterateOnceSymbolGraph(fileName, delim)
    val graph = sg.G()
    while (StdIn.hasNextLine()) {
        val key = StdIn.readLine()
        if (sg.contains(key)) {
            graph.adj(sg.index(key)).forEach {
                println("    ${sg.name(it)}")
            }
        } else {
            println("Dose not contain $key")
        }
    }
}