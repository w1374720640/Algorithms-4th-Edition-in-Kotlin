package chapter3.section2

import chapter2.sleep

/**
 * 从练习3.2.1构造的二叉树中将所有键按照插入顺序逐个删除并画出每次删除所得到的树
 */
fun main() {
    val delay = 2000L

    val charArray = "EASYQUESTION".toCharArray()
    val binaryTreeST = BinaryTreeST<Char, Int>()
    for (i in charArray.indices) {
        binaryTreeST.put(charArray[i], i)
    }

    drawBinaryTree(binaryTreeST)
    sleep(delay)
    for (i in charArray.indices) {
        if (binaryTreeST.contains(charArray[i])) {
            binaryTreeST.delete(charArray[i])
            drawBinaryTree(binaryTreeST)
            sleep(delay)
        }
    }
}