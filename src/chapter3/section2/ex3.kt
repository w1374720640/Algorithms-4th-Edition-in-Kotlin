package chapter3.section2

import chapter2.sleep

/**
 * 给出 A X C S E R H 的5种能够构造出最优二叉查找树的排列
 */
fun ex3(): Array<CharArray> {
    return arrayOf(
            "HCAESRX".toCharArray(),
            "HCSARXE".toCharArray(),
            "HSXRCEA".toCharArray(),
            "HSRCAEX".toCharArray(),
            "HSCAEXR".toCharArray()
    )
}

fun main() {
    val array = ex3()
    array.forEach {
        val bst = BinarySearchTree<Char, Int>()
        for (i in it.indices) {
            bst.put(it[i], i)
        }
        drawBST(bst)
        sleep(3000)
    }
}