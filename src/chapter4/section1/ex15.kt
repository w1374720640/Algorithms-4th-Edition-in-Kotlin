package chapter4.section1

import edu.princeton.cs.algs4.Bag
import edu.princeton.cs.algs4.StdIn
import extensions.inputPrompt

/**
 * 修改Graph的输入流构造函数，允许从标准输入读入图的邻接表（方法类似与SymbolGraph)
 * 如图4.1.26的tinyGadj.txt所示
 * 在顶点和边的总数之后，每一行由一个顶点和它所有相邻顶点组成
 *
 * 解：标准输入需要用StdIn类从键盘读取，而原Graph用的是In类，继承后重写构造函数
 * 代码看起来很烂
 */
class AdjGraph : Graph(0) {
    init {
        var i = 0
        // 记录输入流中边的实际数量
        var edge = 0
        while (StdIn.hasNextLine()) {
            val line = StdIn.readLine()
            when (i) {
                0 -> {
                    V = line.toInt()
                    adj = Array(V) { Bag<Int>() }
                }
                1 -> edge = line.toInt()
                else -> {
                    val intList = line.split(" ")
                    if (intList.size > 1) {
                        edge -= intList.size - 1
                        val v = intList[0].toInt()
                        check(v in 0..V) { "$v should be in the range of 0..$V" }

                        for (j in 1 until intList.size) {
                            val w = intList[j].toInt()
                            check(w in 0..V) { "$w should be in the range of 0..$V" }
                            addEdge(v, w)
                        }
                    }
                }
            }
            i++
        }
        check(edge == 0) { "The number of edges should be $E" }
    }
}

fun main() {
    inputPrompt()
    val graph = AdjGraph()
    println(graph.toString())
}