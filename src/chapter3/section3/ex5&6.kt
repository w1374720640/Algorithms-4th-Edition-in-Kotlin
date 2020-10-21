package chapter3.section3

import chapter1.section4.factorial
import chapter2.sleep
import chapter3.section2.fullArray
import edu.princeton.cs.algs4.StdDraw
import extensions.formatDouble

/**
 * 3.3.5: 右图显示了N=1到6之间大小为N的所有不同的2-3树（无先后次序）
 * 请画出N=7、8、9和10的大小为N的所有不同的2-3树
 * 3.3.6: 计算用N个随机键构造练习3.3.5中每棵2-3树的概率
 *
 * 解：参考练习3.2.9、3.2.26
 */
fun ex5(N: Int, delay: Long) {
    val allBSTArray = createAllComparableRedBlackBST(N)
    val countBST = RedBlackBST<ComparableRedBlackBST<Int, Int>, Int>()
    allBSTArray.forEach { tree ->
        val count = countBST.get(tree)
        if (count == null) {
            countBST.put(tree, 1)
        } else {
            countBST.put(tree, count + 1)
        }
    }
    val total = allBSTArray.size
    println("N=$N  factorial(N)=$total  diffTreeNum=${countBST.size()}")
    countBST.keys().forEach { tree ->
        drawRedBlackBST(tree)
        val count = countBST.get(tree) ?: 1
        StdDraw.setPenColor()
        StdDraw.textLeft(0.3, 0.98, "N=$N  probabilities=${count}/${total}=${formatDouble(count.toDouble() / total * 100, 2)}%")
        sleep(delay)
    }
}

fun createAllComparableRedBlackBST(N: Int): Array<ComparableRedBlackBST<Int, Int>> {
    val array = Array(N) { it + 1 }
    val list = ArrayList<Array<Int>>()
    fullArray(array, 0, list)
    check(list.size == factorial(N).toInt())
    return Array(list.size) { index ->
        ComparableRedBlackBST<Int, Int>().apply {
            list[index].forEach { key ->
                put(key, 0)
            }
        }
    }
}

/**
 * 可比较大小的红黑树
 */
class ComparableRedBlackBST<K : Comparable<K>, V : Any> : RedBlackBST<K, V>(), Comparable<ComparableRedBlackBST<K, V>> {
    override fun compareTo(other: ComparableRedBlackBST<K, V>): Int {
        return compare(this.root, other.root)
    }

    fun compare(node1: Node<K, V>?, node2: Node<K, V>?): Int {
        if (node1 == null && node2 == null) return 0
        if (node1 == null) return -1
        if (node2 == null) return 1
        val result1 = node1.key.compareTo(node2.key)
        if (result1 != 0) return result1
        val result2 = compare(node1.left, node2.left)
        if (result2 != 0) return result2
        return compare(node1.right, node2.right)
    }
}

fun main() {
    var N = 1
    val delay = 1000L
    repeat(10) {
        ex5(N, delay)
        N++
    }
}