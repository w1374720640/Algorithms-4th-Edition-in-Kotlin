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
        if (bst.height() == 2) {
            println(array.joinToString())
            count++
        }
    }
    println("list.size()=${list.size} count=$count")
}

fun <K : Comparable<K>> RedBlackBST<K, *>.height(): Int {
    var blackCount = 0
    var node = root
    while (node != null) {
        if (!node.isRed()) {
            blackCount++
        }
        //红黑树完美黑色平衡，所以所有路径上的黑色结点数量相等
        node = node.left
    }
    //如果树只有一个红色结点，则高度为1，否则树的高度等于某一条路径上的黑色结点数量
    return if (blackCount == 0 && !isEmpty()) 1 else blackCount
}

fun main() {
    ex3()
}