package chapter3.section3

import extensions.formatDouble
import extensions.inputPrompt
import extensions.readInt
import kotlin.math.log2
import kotlin.math.pow

/**
 * 红黑树的最坏情况
 * 找出如何构造一颗大小为N的最差红黑树，其中根结点到几乎所有空链接的路径长度均为2lgN
 *
 * 解：设叶子结点路径上的黑色结点数为x，最差的红黑树大小为 2^(x+1)-2 ，插入顺序为逆序
 * 从根结点到空链接的路径长度不可能为2lgN，肯定小于2lgN
 */
fun main() {
    inputPrompt()
    val blackHeight = readInt("blackHeight: ")
    val bst = RedBlackBST<Int, Int>()
    for (i in 2.0.pow(blackHeight + 1).toInt() - 2 downTo 1) {
        bst.put(i, 0)
    }
    println("2lgN=${formatDouble(2 * log2(bst.size().toDouble()), 2)}")
    drawRedBlackBST(bst, showKey = false, showFlatView = false)
}