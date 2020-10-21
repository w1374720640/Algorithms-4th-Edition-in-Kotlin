package chapter3.section3

/**
 * 将键 Y L P M X H C R A E S 按顺序插入一颗空2-3树并画出结果
 */
fun main() {
    val array = "YLPMXHCRAES".toCharArray()
    val bst = RedBlackBST<Char, Int>()
    array.forEach {
        bst.put(it, 0)
    }
    drawRedBlackBST(bst)
}