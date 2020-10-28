package chapter3.section3

import edu.princeton.cs.algs4.Stack
import extensions.formatDouble
import extensions.shuffle

/**
 * 统计红色结点
 * 编写一段程序，统计给定的红黑树中红色结点所占的比例
 * 对于N=10⁴、10⁵和10⁶，用你的程序统计至少100棵随机构造的大小为N的红黑树并得出一个猜想
 */
fun ex43_CountRedNodes(N: Int, times: Int): Double {
    require(N > 0 && times > 0)
    var redNum = 0L
    repeat(times) {
        val array = IntArray(N) { it }
        array.shuffle()
        val bst = RedBlackBST<Int, Int>()
        array.forEach {
            bst.put(it, 0)
        }
        redNum += bst.getRedNodeNum()
    }
    return redNum.toDouble() / times / N
}

fun <K : Comparable<K>, V : Any> RedBlackBST<K, V>.getRedNodeNum(): Int {
    if (isEmpty()) return 0
    var redNum = 0
    val stack = Stack<RedBlackBST.Node<K, V>>()
    stack.push(root!!)
    while (!stack.isEmpty) {
        val node = stack.pop()
        if (node.isRed()) {
            redNum++
        }
        if (node.right != null) {
            stack.push(node.right!!)
        }
        if (node.left != null) {
            stack.push(node.left!!)
        }
    }
    return redNum
}

fun main() {
    var N = 10000
    val times = 100
    repeat(5) {
        val redRatio = ex43_CountRedNodes(N, times)
        println("N=$N times=$times ratio=${formatDouble(redRatio, 4)}")
        N *= 2
    }
}