package chapter3.section3

import edu.princeton.cs.algs4.StdDraw

/**
 * 在键按照降序插入红黑树的情况下重新回答上面两道练习
 */
fun main() {
    val bst = RedBlackBST<Int, Int>()
    val array = IntArray(100){100 - it}
    var result = true
    var preHeight = 0
    array.forEach {
        bst.put(it, 0)
        val height = bst.height()
        if (height < preHeight) {
            result = false
        }
        preHeight = height
    }
    println("result=$result")

    val showChangeProcessRedBlackBST = ShowChangeProcessRedBlackBST<Char, Int>()
    showChangeProcessRedBlackBST.showProcess = true
    for (key in 'K' downTo 'A') {
        showChangeProcessRedBlackBST.put(key, 0)
    }
    StdDraw.setPenColor()
    StdDraw.textLeft(0.02, 0.98, "end")
}