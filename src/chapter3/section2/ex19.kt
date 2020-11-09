package chapter3.section2

import chapter2.sleep

/**
 * 从练习3.2.1构造的二叉查找树中逐次删除树的根节点并画出每次删除所得到的树
 */
fun main() {
    val delay = 2000L

    val charArray = "EASYQUESTION".toCharArray()
    val bst = BinarySearchTree<Char, Int>()
    for (i in charArray.indices) {
        bst.put(charArray[i], i)
    }

    drawBST(bst)
    sleep(delay)
    charArray.sort()
    while (!bst.isEmpty()) {
        bst.delete(bst.root!!.key)
        drawBST(bst)
        sleep(delay)
    }
}