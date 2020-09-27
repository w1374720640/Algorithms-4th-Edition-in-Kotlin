package chapter3.section2

import edu.princeton.cs.algs4.StdDraw
import extensions.random
import java.awt.Color

/**
 * 绘制二叉树的图形
 * 标准的二叉树图形所有结点在x轴上的投影应该连续且间距相等，一个结点左子树的所有结点在x轴的投影都在该结点左侧
 * 理想情况下，根结点在x轴的投影在所有投影的中点，所有结点组成完全二叉树，树的深度为lgN+1
 * 最坏情况下，根节点在所有投影的最左侧或最右侧，其他结点最多只有一个子结点，树的深度为N
 * 创建一个虚拟的二维坐标，左上角为原点，水平向右为x轴正方向，垂直向下为y轴正方向
 * 根节点的坐标固定为(N,1)，其他结点的x坐标范围为[1,2N]，y坐标范围为[1,N]
 * 新建一个结点，包含原始结点的引用以及左右子结点、x、y坐标值，从原始的根结点开始，依次转换为新的结点
 * 创建一个长度为2N+1的结点数组，数组的索引等于结点的x坐标，所以根结点在索引为N的位置
 * 插入子结点时，设父结点的索引为i，有两种方式插入子结点：
 *
 * 1、通过左右移动结点在数组中的位置来实现正确的插入，时间复杂度为O(N^2)：
 * 当插入左子结点时，判断i-1的位置是否有值，如果没有值则直接插入
 * 如果i-1的位置有值，判断i在N的左侧还是右侧，如果在左侧，将i左侧所有结点左移一位，然后在i-1位置插入
 * 如果在右侧，将i及右侧所有结点向右移一位（包括i），然后在i位置插入
 * 当插入右子结点时，判断i+1的位置是否有值，如果没有则直接插入
 * 如果i+1的位置有值，判断i在N的左侧还是右侧，如果在左侧，将i及左侧所有结点左移一位（包括i），然后在i位置插入
 * 如果在右侧，将i右侧所有结点右移一位，然后在i+1位置插入
 *
 * 2、当每个结点记录了子树的大小时，可以使用子树的大小直接计算正确的位置，时间复杂度O(N)：
 * 插入左子结点时，左子结点的索引 leftIndex = parentIndex - leftNode.right.count - 1
 * 插入右子结点时，右子结点的索引 rightIndex = parentIndex + rightNode.left.count + 1
 * 遍历二叉树即可插入所有结点
 * （这种方法使用长度为N的数组即可实现，但为了和方式1保持一致，仍然使用长度为2N+1的数组）
 *
 * 记录x坐标和y坐标的边界值，绘制时对坐标转换使图形居中，从根节点依次绘制
 */
class BinaryTreeGraphics<K : Comparable<K>, V : Any>(binaryTreeST: BinaryTreeST<K, V>, useLinearAlgorithm: Boolean) {

    class Node<K : Comparable<K>, V : Any>(val originNode: BinaryTreeST.Node<K, V>,
                                           var x: Int,
                                           val y: Int,
                                           var left: Node<K, V>? = null,
                                           var right: Node<K, V>? = null)

    //是否在结点中间显示key
    var showKey = false

    //是否在结点上方显示每个子树的大小
    var showSize = false
    val size = binaryTreeST.size()
    val array = arrayOfNulls<Node<K, V>>(size * 2 + 1)

    //根结点坐标固定为(size,1)
    val root = if (size == 0) null else Node(binaryTreeST.root!!, size, 1)
    var minX = size
    var maxX = size
    var maxY = 1

    init {
        if (root != null) {
            array[size] = root
            if (useLinearAlgorithm) {
                addChildNodeLinear(root, size)
            } else {
                addChildNodeSquare(root)
            }
        }
    }

    /**
     * 线性时间复杂度构造二叉树（需要结点中记录当前结点及所有子结点的数量）
     */
    private fun addChildNodeLinear(parentNode: Node<K, V>, parentIndex: Int) {
        val leftOriginNode = parentNode.originNode.left
        if (leftOriginNode != null) {
            val leftIndex = parentIndex - getNodeSize(leftOriginNode.right) - 1
            val leftNode = Node(leftOriginNode, leftIndex, parentNode.y + 1)
            parentNode.left = leftNode
            minX = minOf(minX, leftIndex)
            maxY = maxOf(maxY, parentNode.y + 1)
            addChildNodeLinear(leftNode, leftIndex)
        }
        val rightOriginNode = parentNode.originNode.right
        if (rightOriginNode != null) {
            val rightIndex = parentIndex + getNodeSize(rightOriginNode.left) + 1
            val rightNode = Node(rightOriginNode, rightIndex, parentNode.y + 1)
            parentNode.right = rightNode
            maxX = maxOf(maxX, rightIndex)
            maxY = maxOf(maxY, parentNode.y + 1)
            addChildNodeLinear(rightNode, rightIndex)
        }
    }

    private fun getNodeSize(node: BinaryTreeST.Node<K, V>?): Int {
        return node?.count ?: 0
    }

    /**
     * 平方级复杂度构造二叉树（更通用，适用于结点中没有记录子树大小的情况）
     */
    private fun addChildNodeSquare(node: Node<K, V>) {
        if (node.originNode.left != null) {
            var leftIndex = node.x - 1
            if (array[leftIndex] != null) {
                if (node.x < size) {
                    shiftLeft(minX, leftIndex)
                    minX--
                }
                if (node.x > size) {
                    leftIndex = node.x
                    shiftRight(node.x, maxX)
                    maxX++
                }
            } else {
                //只有在最左侧插入时array[leftIndex]才为空
                minX--
            }
            val leftNode = Node(node.originNode.left!!, leftIndex, node.y + 1)
            array[leftIndex] = leftNode
            node.left = leftNode
            maxY = maxOf(maxY, node.y + 1)
            addChildNodeSquare(leftNode)
        }
        if (node.originNode.right != null) {
            var rightIndex = node.x + 1
            if (array[rightIndex] != null) {
                if (node.x < size) {
                    rightIndex = node.x
                    shiftLeft(minX, node.x)
                    minX--
                }
                if (node.x > size) {
                    shiftRight(rightIndex, maxX)
                    maxX++
                }
            } else {
                maxX++
            }
            val rightNode = Node(node.originNode.right!!, rightIndex, node.y + 1)
            array[rightIndex] = rightNode
            node.right = rightNode
            maxY = maxOf(maxY, node.y + 1)
            addChildNodeSquare(rightNode)
        }
    }

    /**
     * 数组范围左移
     */
    private fun shiftLeft(start: Int, end: Int) {
        for (i in start..end) {
            array[i]?.let { it.x-- }
            array[i - 1] = array[i]
        }
    }

    /**
     * 数组范围右移
     */
    private fun shiftRight(start: Int, end: Int) {
        for (i in end downTo start) {
            array[i]?.let { it.x++ }
            array[i + 1] = array[i]
        }
    }

    //结点圆的半径
    private var nodeRadio = 0.0

    fun draw() {
        if (root == null) return
        nodeRadio = 1.0 / (maxX - minX + 2) / 2
        StdDraw.clear()
        draw(root)
    }

    private fun draw(node: Node<K, V>) {
        StdDraw.setPenColor(Color.BLACK)
        if (node.left != null) {
            StdDraw.line(convertX(node), convertY(node), convertX(node.left!!), convertY(node.left!!))
        }
        if (node.right != null) {
            StdDraw.line(convertX(node), convertY(node), convertX(node.right!!), convertY(node.right!!))
        }
        StdDraw.setPenColor(Color.WHITE)
        StdDraw.filledCircle(convertX(node), convertY(node), nodeRadio)
        StdDraw.setPenColor(Color.BLACK)
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

    private fun convertX(node: Node<K, V>): Double {
        return (node.x - minX + 1) / (maxX - minX + 2).toDouble()
    }

    private fun convertY(node: Node<K, V>): Double {
        return (maxY - node.y + 1) / (maxY + 1).toDouble()
    }
}

fun <K : Comparable<K>, V : Any> drawBinaryTree(binaryTree: BinaryTreeST<K, V>, showKey: Boolean = true, showSize: Boolean = false) {
    if (binaryTree.isEmpty()) return
    val graphics = BinaryTreeGraphics(binaryTree, true)
    graphics.showKey = showKey
    graphics.showSize = showSize
    graphics.draw()
}

fun main() {
    val array = IntArray(20) { random(100) }
    val binaryTreeST = BinaryTreeST<Int, Int>()
    for (i in array.indices) {
        binaryTreeST.put(array[i], i)
    }
    drawBinaryTree(binaryTreeST, showSize = true)
}