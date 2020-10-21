package chapter3.section3

/**
 * 将键 E A S Y Q U T I O N 按顺序插入一颗空2-3树并画出结果
 */
fun main() {
    val array = "EASYQUTION".toCharArray()
    val bst = RedBlackBST<Char, Int>()
    array.forEach {
        bst.put(it, 0)
    }
    drawRedBlackBST(bst)
}