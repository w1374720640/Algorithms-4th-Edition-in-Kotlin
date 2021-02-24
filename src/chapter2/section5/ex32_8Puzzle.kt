package chapter2.section5

import chapter1.section3.*
import chapter2.section4.HeapMinPriorityQueue
import chapter2.sleep
import edu.princeton.cs.algs4.StdDraw
import edu.princeton.cs.algs4.StdRandom
import extensions.formatInt
import java.awt.Color
import kotlin.math.abs

/**
 * 8字谜题
 * 8字谜题是S.Loyd于19实际70年代发明的一个游戏
 * 游戏需要一个三乘三的九宫格，其中八格中填上了1到8这8个数字，一格空着。你的目标就是将所有的格子排序
 * 可以将一个格子向上下或左右移动（但不能是对角线方向）到空白的格子中
 * 编写一个程序用A*算法解决这个问题
 * 先用到达九宫格的当前位置所需的步数加上错位的格子数量作为优先级函数（注意，步数至少大于等于错位的格子数）
 * 尝试用其他函数代替错位的格子数量，比如每个格子距离它的正确位置的曼哈顿距离，或是这些距离的平方之和
 *
 * 解：A*寻路算法见ex32_AStartPathFind文件，必须先看懂A*寻路算法才能看懂这题
 * 创建一个类，内含一个长度为9的数组，内容分别是1~8和-1，打乱所有元素作为初始状态，加入OpenList中。
 * 添加一个方法，每次可以向上下左右移动一个位置，移动时创建一个新对象，比较是否存在于OpenList或CloseList中，
 * 如果不存在，则加入OpenList中，计算该对象的总成本，父结点指向原对象，并将父结点加入CloseList中。
 * 每次从OpenList中弹出成本最小的对象，判断是否是最终位置，不是则重复上面的过程，
 * 直到达到最终位置，或OpenList为空（无法达到最终位置）
 * 计算总成本时，可以选择成本计算方式，如错位的格子数量、每个格子距离它的正确位置的曼哈顿距离、这些距离的平方之和等
 * 一共有9!=362880种状态
 */
class Puzzle() {
    enum class CostModel {
        WRONG_POSITION, MANHATTAN_DISTANCE, SUM_OF_SQUARES
    }

    class Node private constructor(val array: IntArray,
                                   var parent: Node? = null,
                                   val costModel: CostModel = CostModel.WRONG_POSITION) : Comparable<Node>, Cloneable {
        companion object {
            fun startNode(costModel: CostModel): Node {
                val array = IntArray(9) { it }
                //空白的格子用-1表示
                array[0] = -1
                StdRandom.shuffle(array)
                val node = Node(array, null, costModel)
                node.computingCosts()
                return node

            }

            fun startNode(array: IntArray, costModel: CostModel): Node {
                require(array.size == 9)
                val boolArray = BooleanArray(array.size) { false }
                array.forEach {
                    require(it == -1 || it in 1..8)
                    require(!boolArray[if (it == -1) 0 else it])
                    boolArray[if (it == -1) 0 else it] = true
                }

                val node = Node(array, null, costModel)
                node.computingCosts()
                return node
            }
        }

        var blankIndex = array.indexOf(-1)

        init {
            require(blankIndex != -1)
        }

        private var G = -1 //从起始点移动到该点的成本
        private var H = -1 //从该点移动到终点的估算成本
        private val F: Int //总的成本
            get() = G + H

        fun isEnd(): Boolean {
            for (i in 0..7) {
                if (array[i] != i + 1) return false
            }
            return array[8] == -1
        }

        private fun computingCosts() {
            G = if (parent == null) 0 else parent!!.G + 1
            H = when (costModel) {
                CostModel.WRONG_POSITION -> {
                    wrongPosition()
                }
                CostModel.MANHATTAN_DISTANCE -> {
                    manhattanDistance()
                }
                CostModel.SUM_OF_SQUARES -> {
                    val wrongPosition = wrongPosition()
                    val manhattanDistance = manhattanDistance()
                    wrongPosition * wrongPosition + manhattanDistance * manhattanDistance
                }
            }
        }

        private fun wrongPosition(): Int {
            var count = 0
            for (i in 0..7) {
                if (array[i] != i + 1) {
                    count++
                }
            }
            if (array[8] != -1) {
                count++
            }
            return count
        }

        private fun manhattanDistance(): Int {
            var distance = 0
            for (i in array.indices) {
                //获取索引为i的点应该处于的位置
                val rightPosition = if (array[i] == -1) 8 else array[i] - 1
                //索引为i的点x坐标为 i%3 y坐标为 2 - i/3，曼哈顿距离为x坐标差值的绝对值和y坐标差值的绝对值之和
                distance += abs(rightPosition / 3 - i / 3) + abs(rightPosition % 3 - i % 3)
            }
            return distance
        }

        fun moveLeft(): Node? {
            if (blankIndex % 3 == 0) return null
            val newNode = clone()
            newNode.swap(newNode.blankIndex, --newNode.blankIndex)
            newNode.computingCosts()
            return newNode
        }

        fun moveRight(): Node? {
            if (blankIndex % 3 == 2) return null
            val newNode = clone()
            newNode.swap(newNode.blankIndex, ++newNode.blankIndex)
            newNode.computingCosts()
            return newNode
        }

        fun moveTop(): Node? {
            if (blankIndex / 3 == 0) return null
            val newNode = clone()
            newNode.swap(newNode.blankIndex, newNode.blankIndex - 3)
            newNode.blankIndex -= 3
            newNode.computingCosts()
            return newNode
        }

        fun moveBottom(): Node? {
            if (blankIndex / 3 == 2) return null
            val newNode = clone()
            newNode.swap(newNode.blankIndex, newNode.blankIndex + 3)
            newNode.blankIndex += 3
            newNode.computingCosts()
            return newNode
        }

        private fun swap(i: Int, j: Int) {
            val temp = array[i]
            array[i] = array[j]
            array[j] = temp
        }

        fun draw(delay: Long) {
            val side = 1.0 / 3
            for (i in array.indices) {
                val x = i % 3
                val y = 2 - i / 3
                StdDraw.setPenColor(if (array[i] == -1) Color(0x98fb98) else Color(0xafeeee))
                StdDraw.filledRectangle((x + 0.5) * side, (y + 0.5) * side, side / 2, side / 2)
                StdDraw.setPenColor(Color.BLACK)
                StdDraw.textRight((x + 0.5) * side, (y + 0.5) * side, array[i].toString())
            }
            sleep(delay)
        }

        override fun compareTo(other: Node): Int {
            require(G != -1 && H != -1 && other.G != -1 && other.G != -1) { "Not initialized properly" }
            return F.compareTo(other.F)
        }

        override fun equals(other: Any?): Boolean {
            if (other == null) return false
            if (this === other) return true
            if (other !is Node) return false
            for (i in array.indices) {
                if (array[i] != other.array[i]) return false
            }
            return true
        }

        override fun hashCode(): Int {
            return array.contentHashCode()
        }

        override fun clone(): Node {
            val newNode = Node(array.clone(), this, costModel)
            newNode.H = H
            newNode.G = G
            return newNode
        }

        override fun toString(): String {
            return """
                |${formatInt(array[0], 2)} ${formatInt(array[1], 2)} ${formatInt(array[2], 2)}|
                |${formatInt(array[3], 2)} ${formatInt(array[4], 2)} ${formatInt(array[5], 2)}|
                |${formatInt(array[6], 2)} ${formatInt(array[7], 2)} ${formatInt(array[8], 2)}|
            """.trimIndent()
        }
    }

    //小顶堆，查询最小值很快
    private val openPQ = HeapMinPriorityQueue<Node>()

    //HashSet用于判断对象是否存在效率较高，用空间换时间
    private val openSet = HashSet<Node>()
    private val closeSet = HashSet<Node>()

    fun find(startNode: Node): DoublyLinkedList<Node> {
        val list = DoublyLinkedList<Node>()
        openPQ.insert(startNode)
        openSet.add(startNode)
        while (!openPQ.isEmpty()) {
            val minNode = openPQ.delMin()
            openSet.remove(minNode)
            if (minNode.isEnd()) {
                list.addTail(minNode)
                var parent = minNode.parent
                while (parent != null) {
                    list.addHeader(parent)
                    parent = parent.parent
                }
                return list
            }
            checkAndAddNode(minNode.moveLeft())
            checkAndAddNode(minNode.moveRight())
            checkAndAddNode(minNode.moveTop())
            checkAndAddNode(minNode.moveBottom())
            closeSet.add(minNode)
        }
        return list
    }

    private fun checkAndAddNode(node: Node?) {
        //用openSet判断是否存在而不用openPQ判断
        if (node != null && !openSet.contains(node) && !closeSet.contains(node)) {
            openPQ.insert(node)
            openSet.add(node)
        }
    }
}

fun main() {
    val costModel = Puzzle.CostModel.WRONG_POSITION
    val draw = true
    val delay = 1000L

    val puzzle = Puzzle()
    val startNode = Puzzle.Node.startNode(costModel)
    //可能会出现无法达到最终状态的初始布局，例如下面的初始化代码
//    val startNode = Puzzle.Node.startNode(intArrayOf(-1, 3, 2, 7, 1, 6, 8, 4, 5), costModel)
    println("startNode:")
    println(startNode)
    println()
    val list = puzzle.find(startNode)
    if (list.isEmpty()) {
        println("Find failed!")
    } else {
        println("Find successful, step=${list.size - 1}")
        list.forwardIterator().forEach {
            if (draw) {
                it.draw(delay)
            } else {
                println(it)
                println()
            }
        }
        if (!draw) {
            println("Find successful, step=${list.size - 1}")
        }
    }
}