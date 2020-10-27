package chapter3.section3

import extensions.shuffle

/**
 * 树的绘制
 * 为RedBlackBST添加一个draw()方法，像正文一样绘制出红黑树
 */
fun main() {
    val array = IntArray(20) { it }
    array.shuffle()
    val bst = RedBlackBST<Int, Int>()
    array.forEach {
        bst.put(it, 0)
    }
    drawRedBlackBST(bst)
}