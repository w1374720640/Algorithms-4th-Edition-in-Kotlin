package chapter4.section2

import chapter3.section4.LinearProbingHashST
import edu.princeton.cs.algs4.In

/**
 * 用符号作为顶点名的图
 * 根据[fileName]指定的文件构造图，使用[delim]来分隔顶点名
 */
class SymbolDigraph(fileName: String, delim: String) {
    private val st = LinearProbingHashST<String, Int>()

    init {
        val input = In(fileName)
        while (input.hasNextLine()) {
            val line = input.readLine()
            val items = line.split(delim)
            items.forEach {
                if (!st.contains(it)) {
                    st.put(it, st.size())
                }
            }
        }
    }

    private val names = Array(st.size()) { "" }
    private val digraph: Digraph

    init {
        st.keys().forEach {
            names[st.get(it)!!] = it
        }
        digraph = Digraph(st.size())
        val input = In(fileName)
        while (input.hasNextLine()) {
            val line = input.readLine()
            val items = line.split(delim)
            if (items.size > 1) {
                val v = st.get(items[0])!!
                for (i in 1 until items.size) {
                    digraph.addEdge(v, st.get(items[i])!!)
                }
            }
        }
    }

    /**
     * key是不是一个顶点
     */
    fun contains(key: String): Boolean {
        return st.get(key) != null
    }

    /**
     * key的索引
     */
    fun index(key: String): Int {
        val value = st.get(key)
        if (value == null) {
            throw IllegalArgumentException()
        } else {
            return value
        }
    }

    /**
     * 索引v的顶点名
     */
    fun name(v: Int): String {
        return names[v]
    }

    /**
     * 隐藏的Graph对象
     */
    fun G(): Digraph {
        return digraph
    }
}