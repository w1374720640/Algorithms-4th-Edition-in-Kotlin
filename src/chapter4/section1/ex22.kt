package chapter4.section1

import chapter3.section3.RedBlackBST
import edu.princeton.cs.algs4.In
import edu.princeton.cs.algs4.Stack
import edu.princeton.cs.algs4.StdDraw

/**
 * 编写一段程序BaconHistogram，打印一副Kevin Bacon数的柱状图，
 * 显示movies.txt中Kevin Bacon数为0、1、2、3……的演员分别有多少。
 * 将值为无穷大的人（不与Kevin Bacon连通）归为一类。
 *
 * 解：因为要求的是演员的Kevin Bacon数，所以在用广度优先搜索遍历图时，需要按层统计每层的演员数量
 * 可以参考练习4.1.14中用两个栈替代队列的广度优先搜索方法，输入栈排空时，一层遍历结束
 */
class BaconHistogram {
    private val sg = SymbolGraph("./data/movies.txt", "/")
    private val graph = sg.G()
    private val marked = BooleanArray(graph.V)
    private var actorNum = 0 // 记录演员的总数

    init {
        // SymbolGraph中没有区分演员和电影，而且由于可能存在不连通的顶点，不能用二分图处理
        // 重新遍历文件，统计演员总数
        val input = In("./data/movies.txt")
        while (input.hasNextLine()) {
            val line = input.readLine()
            val items = line.split("/")
            if (items.size > 1) {
                for (i in 1 until items.size) {
                    val index = sg.index(items[i])
                    if (!marked[index]) {
                        marked[index] = true
                        actorNum++
                    }
                }
            }
        }
        for (i in marked.indices) {
            marked[i] = false
        }
    }

    private val bst = RedBlackBST<Int, Int>() // 记录Kevin Bacon数和数量的对应关系
    private var maxActorNum = 0 // 记录所有Kevin Bacon数中最多的演员数量

    init {
        val s = sg.index("Bacon, Kevin")
        var inputStack = Stack<Int>()
        var outputStack = Stack<Int>()
        inputStack.push(s)
        marked[s] = true
        var isActor = false // 标记当前遍历的是演员还是电影
        var degrees = 0 //度数
        bst.put(degrees, 1)
        degrees++

        var count = 0
        while (!inputStack.isEmpty) {
            val v = inputStack.pop()
            graph.adj(v).forEach { w ->
                if (!marked[w]) {
                    marked[w] = true
                    outputStack.push(w)
                    count++
                }
            }
            if (inputStack.isEmpty) {
                if (isActor) {
                    bst.put(degrees, count)
                    degrees++
                }
                isActor = !isActor
                count = 0

                val temp = inputStack
                inputStack = outputStack
                outputStack = temp
            }
        }

        count = 0
        bst.keys().forEach { key ->
            val value = bst.get(key)!!
            if (value > maxActorNum) {
                maxActorNum = value
            }
            count += value
        }
        val notConnectedActorNum = actorNum - count
        if (notConnectedActorNum > maxActorNum) {
            maxActorNum = notConnectedActorNum
        }
        bst.put(Int.MAX_VALUE, notConnectedActorNum)
    }

    companion object {
        // 可绘制区域的左边界和下边界
        const val LEFT = 0.05
        const val BOTTOM = 0.05

        // 一条数据左右边距百分比
        const val MARGIN_PERCENT = 0.1

        // 绘制文字时的间距
        const val TEXT_MARGIN = 0.02
    }

    fun draw() {
        val width = 1.0 / bst.size() * (1 - LEFT * 2)
        val drawWidth = width * (1 - MARGIN_PERCENT * 2)
        StdDraw.clear()
        StdDraw.setPenColor()
        StdDraw.line(LEFT, BOTTOM, 1 - LEFT, BOTTOM)
        StdDraw.line(LEFT, BOTTOM, LEFT, 1 - BOTTOM)
        var i = 0
        bst.keys().forEach { key ->
            val value = bst.get(key)!!
            val x = (i + 0.5) * width + LEFT
            val y = value.toDouble() / maxActorNum * (1 - BOTTOM * 2) / 2 + BOTTOM
            val halfWidth = drawWidth / 2
            val halfHeight = value.toDouble() / maxActorNum * (1 - BOTTOM * 2) / 2
            StdDraw.filledRectangle(x, y, halfWidth, halfHeight)
            StdDraw.text(x, BOTTOM - TEXT_MARGIN, if (key == Int.MAX_VALUE) "not connected" else key.toString())
            StdDraw.text(x, y + halfHeight + TEXT_MARGIN, value.toString())
            i++
        }
    }
}

fun main() {
    BaconHistogram().draw()
}