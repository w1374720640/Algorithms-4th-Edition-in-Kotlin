package chapter3.section3

import chapter2.sleep
import chapter3.section2.BinarySearchTree
import chapter3.section2.drawBST
import extensions.shuffle

/**
 * 随机生成两棵均含有16个结点的红黑树
 * 画出它们（手绘或者代码绘制均可）并将它们使用同一组键构造的（非平衡的）二叉查找树进行比较
 */
fun main() {
    val times = 10
    val delay = 2000L

    repeat(times) {
        val array = IntArray(16){it}
        array.shuffle()
        val redBlackBST = RedBlackBST<Int, Int>()
        val bst = BinarySearchTree<Int, Int>()
        array.forEach {
            redBlackBST.put(it, 0)
            bst.put(it, 0)
        }
        drawRedBlackBST(redBlackBST, showKey = false, showFlatView = false)
        sleep(delay)
        drawBST(bst, showKey = false)
        sleep(delay)
    }
}