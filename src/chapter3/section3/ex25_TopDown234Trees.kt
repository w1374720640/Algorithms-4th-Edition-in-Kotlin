package chapter3.section3

import edu.princeton.cs.algs4.Stack
import extensions.shuffle

/**
 * 自顶向下的2-3-4树
 * 使用平衡的2-3-4树作为数据结构实现符号标的基本API
 * 在树的表示中使用红黑链接并实现正文所述的插入算法
 * 其中沿查找路径向下的过程中分解4-结点并进行颜色转换
 * 在回溯向上的过程中将4-结点配平
 *
 * 解：书中的标准实现红链接只能在左侧，一个黑结点不能同时有两个红色子结点
 * 2-3-4树中一个黑色结点可以同时拥有两个红色子结点（这里不允许单独的红色右子结点，但是可以左右子结点同时为红色）
 * 2-3-4树的效率更高，因为旋转及颜色翻转的次数更少却能保证相同的层数
 * 网络上的红黑树实现通常都是2-3-4树，且可以有单独的红色右子结点（见练习3.3.27）
 *
 * 这里只实现了put方法，没有实现delete方法
 */
open class TopDown234Tree<K : Comparable<K>, V : Any> : RedBlackBST<K, V>() {

    override fun put(key: K, value: V) {
        if (isEmpty()) {
            root = Node(key, value, color = BLACK)
        } else {
            root = put(root, key, value)
            root!!.color = BLACK
        }
    }

    override fun put(node: Node<K, V>?, key: K, value: V): Node<K, V> {
        if (node == null) return Node(key, value)
        var h: Node<K, V> = node
        if (h.left.isRed() && h.right.isRed()) flipColors(h)

        when {
            key > h.key -> h.right = put(h.right, key, value)
            key < h.key -> h.left = put(h.left, key, value)
            else -> h.value = value
        }

        if (!h.left.isRed() && h.right.isRed()) h = rotateLeft(h)
        if (h.left.isRed() && h.left?.left.isRed()) h = rotateRight(h)

        resize(h)
        return h
    }
}

/**
 * 没有两个连续的红色结点（但是一个黑色结点可以有两个红色子结点）
 */
fun <K : Comparable<K>, V : Any> noTwoConsecutiveRedNodes(bst: RedBlackBST<K, V>): Boolean {
    if (bst.isEmpty()) return true
    val stack = Stack<RedBlackBST.Node<K, V>>()
    stack.push(bst.root!!)
    while (!stack.isEmpty) {
        val node = stack.pop()

        if (node.isRed() && (node.left.isRed() || node.right.isRed())) return false

        if (node.right != null) {
            stack.push(node.right)
        }
        if (node.left != null) {
            stack.push(node.left)
        }
    }
    return true
}

/**
 * 测试2-3-4树是否正确
 */
fun test234Tree(bst: RedBlackBST<Int, Int>) {
    require(bst.isEmpty())

    val array = IntArray(100) { it }
    array.shuffle()
    array.forEach {
        bst.put(it, 0)
    }

    check(bst.size() == 100)
    check(noTwoConsecutiveRedNodes(bst))
    check(perfectBlackBalance(bst))
    println("2-3-4Tree check succeed.")
}

fun main() {
    test234Tree(TopDown234Tree())

    val array = IntArray(50) { it }
    array.shuffle()
    val topDown234Tree = TopDown234Tree<Int, Int>()
    array.forEach {
        topDown234Tree.put(it, 0)
    }
    drawRedBlackBST(topDown234Tree, showKey = false)
}

