package chapter3.section3

import edu.princeton.cs.algs4.StdDraw
import extensions.random
import java.awt.Color

/**
 * 绘制红黑树的图形，参考二叉查找树绘制类 BinaryTreeGraphics
 * 区别是可以绘制红色结点，且可以选择以2-3查找树的形式绘制红黑树
 * 2-3查找树比普通二叉查找树更扁平
 */
class RedBlackBSTGraphics<K : Comparable<K>, V : Any>(bst: RedBlackBST<K, V>) {

    class Node<K : Comparable<K>, V : Any>(val originNode: RedBlackBST.Node<K, V>,
                                           var x: Int,
                                           val y: Int,
                                           var left: Node<K, V>? = null,
                                           var right: Node<K, V>? = null)

    var showKey = false //是否在结点中间显示key
    var showSize = false //是否在结点上方显示每个子树的大小
    var showFlatView = false //是否显示更扁平的2-3查找树
    val size = bst.size()

    private val rootX = getNodeSize(bst.root!!.left)
    val root = if (size == 0) null else Node(bst.root!!, rootX, 1)
    var minX = rootX
    var maxX = rootX
    var maxY = 1

    val flatRoot = if (size == 0) null else Node(bst.root!!, rootX, 1)
    var flatMinX = rootX
    var flatMaxX = rootX
    var flatMaxY = 1

    init {
        if (root != null) {
            addChildNode(root, rootX)
            addFlatChildNode(flatRoot!!, rootX)
        }
    }

    /**
     * 线性时间复杂度构造二叉树（需要结点中记录当前结点及所有子结点的数量）
     */
    private fun addChildNode(parentNode: Node<K, V>, parentIndex: Int) {
        val leftOriginNode = parentNode.originNode.left
        if (leftOriginNode != null) {
            val leftIndex = parentIndex - getNodeSize(leftOriginNode.right) - 1
            val leftNode = Node(leftOriginNode, leftIndex, parentNode.y + 1)
            parentNode.left = leftNode
            minX = minOf(minX, leftIndex)
            maxY = maxOf(maxY, parentNode.y + 1)
            addChildNode(leftNode, leftIndex)
        }
        val rightOriginNode = parentNode.originNode.right
        if (rightOriginNode != null) {
            val rightIndex = parentIndex + getNodeSize(rightOriginNode.left) + 1
            val rightNode = Node(rightOriginNode, rightIndex, parentNode.y + 1)
            parentNode.right = rightNode
            maxX = maxOf(maxX, rightIndex)
            maxY = maxOf(maxY, parentNode.y + 1)
            addChildNode(rightNode, rightIndex)
        }
    }

    private fun addFlatChildNode(parentNode: Node<K, V>, parentIndex: Int) {
        val leftOriginNode = parentNode.originNode.left
        if (leftOriginNode != null) {
            val leftIndex = parentIndex - getNodeSize(leftOriginNode.right) - 1
            val leftNode = Node(leftOriginNode, leftIndex, if (leftOriginNode.isRed()) parentNode.y else parentNode.y + 1)
            parentNode.left = leftNode
            flatMinX = minOf(flatMinX, leftIndex)
            flatMaxY = maxOf(flatMaxY, parentNode.y + 1)
            addFlatChildNode(leftNode, leftIndex)
        }
        val rightOriginNode = parentNode.originNode.right
        if (rightOriginNode != null) {
            val rightIndex = parentIndex + getNodeSize(rightOriginNode.left) + 1
            val rightNode = Node(rightOriginNode, rightIndex, if (rightOriginNode.isRed()) parentNode.y else parentNode.y + 1)
            parentNode.right = rightNode
            flatMaxX = maxOf(flatMaxX, rightIndex)
            flatMaxY = maxOf(flatMaxY, parentNode.y + 1)
            addFlatChildNode(rightNode, rightIndex)
        }
    }

    private fun getNodeSize(node: RedBlackBST.Node<K, V>?): Int {
        return node?.count ?: 0
    }

    companion object {
        private const val CANVAS_DEFAULT_SIZE = 512
        private const val FLAT_X_EXPEND_RATIO = 0.4 //扁平的二叉树水平方向扩大倍数
    }

    private var nodeRadio = 0.0 //结点圆的半径

    fun draw() {
        if (root == null || flatRoot == null) {
            StdDraw.clear()
            return
        }
        nodeRadio = 1.0 / (maxX - minX + 2) / 2
        if (showFlatView) {
            StdDraw.setCanvasSize((CANVAS_DEFAULT_SIZE * (2 + FLAT_X_EXPEND_RATIO)).toInt(), CANVAS_DEFAULT_SIZE)
            StdDraw.setXscale(0.0, (2 + FLAT_X_EXPEND_RATIO))

            StdDraw.setPenColor(Color.BLACK)
            StdDraw.textLeft(0.02, 0.98, "红黑树")

            draw(root)

            StdDraw.setPenColor(Color.LIGHT_GRAY)
            StdDraw.line(1.0, 0.0, 1.0, 1.0)
            StdDraw.setPenColor(Color.BLACK)
            StdDraw.textLeft(1.02, 0.98, "2-3树")

            drawFlatView(flatRoot)
        } else {
            StdDraw.setCanvasSize()
            StdDraw.setXscale()
            draw(root)
        }
    }

    private fun draw(node: Node<K, V>) {
        if (node.left != null) {
            StdDraw.setPenColor(if (node.left?.originNode.isRed()) Color.RED else Color.BLACK)
            StdDraw.line(convertX(node), convertY(node), convertX(node.left!!), convertY(node.left!!))
        }
        if (node.right != null) {
            StdDraw.setPenColor(if (node.right?.originNode.isRed()) Color.RED else Color.BLACK)
            StdDraw.line(convertX(node), convertY(node), convertX(node.right!!), convertY(node.right!!))
        }
        StdDraw.setPenColor(Color.WHITE)
        StdDraw.filledCircle(convertX(node), convertY(node), nodeRadio)
        StdDraw.setPenColor(if (node.originNode.isRed()) Color.RED else Color.BLACK)
        StdDraw.circle(convertX(node), convertY(node), nodeRadio)
        if (showKey) {
            StdDraw.text(convertX(node), convertY(node), node.originNode.key.toString())
        }
        if (showSize) {
            StdDraw.text(convertX(node), convertY(node) + nodeRadio + 0.02, node.originNode.count.toString())
        }
        if (node.left != null) {
            draw(node.left!!)
        }
        if (node.right != null) {
            draw(node.right!!)
        }
    }

    private fun drawFlatView(node: Node<K, V>) {
        StdDraw.setPenColor(Color.BLACK)
        if (node.left != null && !node.left?.originNode.isRed()) {
            StdDraw.line(convertFlatX(node), convertFlatY(node), convertFlatX(node.left!!), convertFlatY(node.left!!))
        }
        if (node.right != null) {
            StdDraw.line(convertFlatX(node), convertFlatY(node), convertFlatX(node.right!!), convertFlatY(node.right!!))
        }

        if (node.left != null) {
            drawFlatView(node.left!!)
        }
        if (node.right != null) {
            drawFlatView(node.right!!)
        }

        StdDraw.setPenColor(Color.WHITE)
        StdDraw.filledCircle(convertFlatX(node), convertFlatY(node), nodeRadio)
        StdDraw.setPenColor(Color.BLACK)
        StdDraw.circle(convertFlatX(node), convertFlatY(node), nodeRadio)

        if (node.left?.originNode.isRed()) {
            val leftX = convertFlatX(node.left!!)
            val leftY = convertFlatY(node.left!!)
            val x = convertFlatX(node)
            val y = convertFlatY(node)
            StdDraw.setPenColor(Color.WHITE)
            StdDraw.filledRectangle((leftX + x) / 2, (leftY + y) / 2, (x - leftX) / 2, nodeRadio)
            StdDraw.setPenColor(Color.BLACK)
            StdDraw.line(leftX, leftY + nodeRadio, x, y + nodeRadio)
            StdDraw.line(leftX, leftY - nodeRadio, x, y - nodeRadio)
            //需要重新绘制左子结点的文字
            if (showKey) {
                StdDraw.text(leftX, leftY, node.left!!.originNode.key.toString())
            }
        }
        //只显示key，不显示大小
        if (showKey) {
            StdDraw.text(convertFlatX(node), convertFlatY(node), node.originNode.key.toString())
        }
    }

    private fun convertX(node: Node<K, V>): Double {
        return (node.x - minX + 1) / (maxX - minX + 2).toDouble()
    }

    private fun convertY(node: Node<K, V>): Double {
        return (maxY - node.y + 1) / (maxY + 1).toDouble()
    }

    private fun convertFlatX(node: Node<K, V>): Double {
        return convertX(node) + node.x * nodeRadio * 2 * FLAT_X_EXPEND_RATIO + 1.0
    }

    private fun convertFlatY(node: Node<K, V>): Double {
        return convertY(node)
    }
}

fun <K : Comparable<K>, V : Any> drawRedBlackBST(bst: RedBlackBST<K, V>,
                                                 showKey: Boolean = true,
                                                 showSize: Boolean = false,
                                                 showFlatView: Boolean = true) {
    val graphics = RedBlackBSTGraphics(bst)
    graphics.showKey = showKey
    graphics.showSize = showSize
    graphics.showFlatView = showFlatView
    graphics.draw()
}

fun main() {
    val array = IntArray(20) { random(100) }
    val binaryTreeST = RedBlackBST<Int, Int>()
    for (i in array.indices) {
        binaryTreeST.put(array[i], i)
    }
    drawRedBlackBST(binaryTreeST, showSize = true)
}