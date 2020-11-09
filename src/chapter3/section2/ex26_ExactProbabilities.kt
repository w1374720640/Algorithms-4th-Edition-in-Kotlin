package chapter3.section2

import chapter1.section4.factorial
import chapter2.sleep
import edu.princeton.cs.algs4.StdDraw
import extensions.formatDouble

/**
 * 准确的概率
 * 计算用N个随机的互不相同的键构造出练习3.2.9中每一棵树的概率
 *
 * 解：创建一个新类，继承BinarySearchTree，并实现Comparable接口，
 * 以二叉查找树为key，出现次数为value，将3.2.9中未去重的全量二叉查找树列表依次插入另一颗二叉查找树中，
 * 遍历所有二叉查找树，打印出二叉查找树的图形以及概率
 */
fun ex26_ExactProbabilities(N: Int, delay: Long) {
    val allBinaryTree = createAllComparableBST(N)
    val bst = BinarySearchTree<ComparableBST<Int, Int>, Int>()
    allBinaryTree.forEach { tree ->
        val count = bst.get(tree)
        if (count == null) {
            bst.put(tree, 1)
        } else {
            bst.put(tree, count + 1)
        }
    }
    val total = allBinaryTree.size
    bst.keys().forEach { tree ->
        drawBST(tree)
        val count = bst.get(tree) ?: 1
        StdDraw.textLeft(0.02, 0.98, "${count}/$total = ${formatDouble(count.toDouble() / total * 100, 2)}%")
        sleep(delay)
    }
}

fun createAllComparableBST(N: Int): Array<ComparableBST<Int, Int>> {
    val array = Array(N) { it + 1 }
    val list = ArrayList<Array<Int>>()
    fullArray(array, 0, list)
    check(list.size == factorial(N).toInt())
    return Array(list.size) { index ->
        ComparableBST<Int, Int>().apply {
            list[index].forEach { key ->
                put(key, 0)
            }
        }
    }
}

/**
 * 可比较大小的二叉查找树
 */
class ComparableBST<K : Comparable<K>, V : Any> : BinarySearchTree<K, V>(), Comparable<ComparableBST<K, V>> {
    override fun compareTo(other: ComparableBST<K, V>): Int {
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
    val N = 5
    val delay = 1000L
    ex26_ExactProbabilities(N, delay)
}