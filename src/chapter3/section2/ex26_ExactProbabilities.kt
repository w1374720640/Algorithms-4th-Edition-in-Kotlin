package chapter3.section2

import chapter1.section4.factorial
import chapter2.sleep
import edu.princeton.cs.algs4.StdDraw
import extensions.formatDouble

/**
 * 准确的概率
 * 计算用N个随机的互不相同的键构造出练习3.2.9中每一棵树的概率
 *
 * 解：创建一个新类，继承BinaryTreeSt，并实现Comparable接口，
 * 以二叉树为key，出现次数为value，将3.2.9中未去重的全量二叉树列表依次插入另一颗二叉树中，
 * 遍历所有二叉树，打印出二叉树的图形以及概率
 */
fun ex26_ExactProbabilities(N: Int, delay: Long) {
    val allBinaryTree = createAllComparableBinaryTreeST(N)
    val binaryTreeST = BinaryTreeST<ComparableBinaryTreeST<Int, Int>, Int>()
    allBinaryTree.forEach { tree ->
        val count = binaryTreeST.get(tree)
        if (count == null) {
            binaryTreeST.put(tree, 1)
        } else {
            binaryTreeST.put(tree, count + 1)
        }
    }
    val total = allBinaryTree.size
    binaryTreeST.keys().forEach { tree ->
        drawBinaryTree(tree)
        val count = binaryTreeST.get(tree) ?: 1
        StdDraw.textLeft(0.02, 0.98, "${count}/$total = ${formatDouble(count.toDouble() / total, 3)}")
        sleep(delay)
    }
}

fun createAllComparableBinaryTreeST(N: Int): Array<ComparableBinaryTreeST<Int, Int>> {
    val array = Array(N) { it + 1 }
    val list = ArrayList<Array<Int>>()
    fullArray(array, 0, list)
    check(list.size == factorial(N).toInt())
    return Array(list.size) { index ->
        ComparableBinaryTreeST<Int, Int>().apply {
            list[index].forEach { key ->
                put(key, 0)
            }
        }
    }
}

/**
 * 可比较大小的二叉查找树
 */
class ComparableBinaryTreeST<K : Comparable<K>, V : Any> : BinaryTreeST<K, V>(), Comparable<ComparableBinaryTreeST<K, V>> {
    override fun compareTo(other: ComparableBinaryTreeST<K, V>): Int {
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
    val delay = 2000L
    ex26_ExactProbabilities(N, delay)
}