package chapter3.section3

import chapter2.sleep
import chapter3.section2.BinarySearchTree
import chapter3.section2.drawBST
import chapter3.section2.fullArray
import chapter3.section2.height
import edu.princeton.cs.algs4.StdDraw

/**
 * 找出一组键的序列使得用它顺序构造的二叉查找树比用它顺序构造的红黑树的高度更低，或者证明这样的序列不存在
 *
 * 解：对于部分长度的序列可能存在，部分长度的序列不存在
 * 对指定序列全排列，依次对比每个排列构造的两种树的高度
 */
fun main() {
    val delay = 2000L
    for (N in 4..9) {
        val array = Array(N) { it }
        val list = ArrayList<Array<Int>>()
        fullArray(array, 0, list)

        val resultList = ArrayList<Array<Int>>()
        list.forEach {
            val redBlackBST = RedBlackBST<Int, Int>()
            val bst = BinarySearchTree<Int, Int>()
            it.forEach { key ->
                redBlackBST.put(key, 0)
                bst.put(key, 0)
            }
            if (redBlackBST.height() > bst.height()) {
                resultList.add(it)
            }
        }

        println("N=$N  resultList.size()=${resultList.size}")
        if (resultList.isNotEmpty()) {
            val randomArray = resultList.random()
            println("randomArray: ${randomArray.joinToString()}")
            val redBlackBST = RedBlackBST<Int, Int>()
            val bst = BinarySearchTree<Int, Int>()
            randomArray.forEach { key ->
                redBlackBST.put(key, 0)
                bst.put(key, 0)
            }
            drawRedBlackBST(redBlackBST, showFlatView = false)
            StdDraw.setPenColor()
            StdDraw.textLeft(0.02, 0.98, "N=$N  array=${randomArray.joinToString()}")
            sleep(delay)
            drawBST(bst)
            StdDraw.setPenColor()
            StdDraw.textLeft(0.02, 0.98, "N=$N  array=${randomArray.joinToString()}")
            sleep(delay)
        }
    }
}