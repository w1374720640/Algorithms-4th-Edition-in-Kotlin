package chapter3.section3

import edu.princeton.cs.algs4.StdDraw

/**
 * 用字母A到K按顺序构造一颗红黑树并画出结果，
 * 然后大致说明在按照升序插入键来构造一颗红黑树的过程中发生了什么（可以参考正文中的图例）
 */
fun main() {
    val bst = ShowChangeProcessRedBlackBST<Char, Int>()
    bst.showProcess = true
    for (key in 'A'..'K') {
        bst.put(key, 0)
    }
    StdDraw.setPenColor()
    StdDraw.textLeft(0.02, 0.98, "end")
}