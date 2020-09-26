package chapter3.section2

import chapter2.sleep

/**
 * 将 A X C S E R H 作为键按顺序插入会构造一颗最坏情况下的二叉查找树结构
 * 最下方的结点的两个链接全部为空，其他结点都含有一个空链接
 * 用这些键给出构造最坏情况下的树的其他5种排序
 *
 * 解：远不止6种情况，列出可能的5种情况
 * 最坏情况二叉树高度为N
 */
fun ex2(): Array<CharArray> {
    return arrayOf(
            "ACEHRSX".toCharArray(),
            "ACXSRHE".toCharArray(),
            "AXCESRH".toCharArray(),
            "XSRHECA".toCharArray(),
            "XACSERH".toCharArray()
    )
}

fun main() {
    val array = ex2()
    array.forEach {
        val binaryTreeST = BinaryTreeST<Char, Int>()
        for (i in it.indices) {
            binaryTreeST.put(it[i], i)
        }
        drawBinaryTree(binaryTreeST)
        sleep(5000)
    }
}