package chapter3.section3

import chapter3.section2.fullArray

/**
 * 使用什么顺序插入键 S E A C H X M 能够得到一颗高度为1的2-3树？
 *
 * 解：先添加一个计算红黑树的高度的方法（只统计黑色结点），再根据练习3.2.9中的方法生成所有可能的排序，
 * 统计所有让树高度为1的插入顺序（我实现的方法树高度从1开始，所以应该统计让height()方法返回2的树）
 */
fun ex3() {
    val list = ArrayList<Array<Char>>()
    fullArray("SEACHXM".toCharArray().toTypedArray(), 0, list)
    var count = 0
    list.forEach { array ->
        val bst = RedBlackBST<Char, Int>()
        array.forEach {
            bst.put(it, 0)
        }
        if (bst.blackHeight() == 2) {
            println(array.joinToString())
            count++
        }
    }
    println("list.size()=${list.size} count=$count")
}

/**
 * 红黑树的黑色结点高度，等同于2-3树的高度
 */
fun <K : Comparable<K>> RedBlackBST<K, *>.blackHeight(): Int {
    var blackCount = 0
    var node = root
    while (node != null) {
        if (!node.isRed()) {
            blackCount++
        }
        //红黑树完美黑色平衡，所以所有路径上的黑色结点数量相等
        node = node.left
    }
    return blackCount
}

/**
 * 红黑树的实际高度
 */
fun <K : Comparable<K>> RedBlackBST<K, *>.height(): Int {
    if (isEmpty()) return 0
    return height(root!!)
}

fun <K : Comparable<K>, V : Any> height(node: RedBlackBST.Node<K, V>): Int {
    var leftHeight = 0
    var rightHeight = 0
    if (node.left != null) {
        leftHeight = height(node.left!!)
    }
    if (node.right != null) {
        rightHeight = height(node.right!!)
    }
    return maxOf(leftHeight, rightHeight) + 1
}

fun main() {
    ex3()
}