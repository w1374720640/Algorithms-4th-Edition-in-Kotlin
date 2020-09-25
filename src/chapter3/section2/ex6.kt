package chapter3.section2

import chapter3.section1.testOrderedST
import extensions.random

/**
 * 为二叉查找树添加一个方法height()来计算树的高度
 * 实现两种方案：一种使用递归（用时为线性级别，所需空间和树高度成正比）
 * 一种模仿size()在每个结点种添加一个变量（所需空间为线性级别，查询耗时为常数）
 */
fun <K : Comparable<K>, V : Any> BinaryTreeST<K, V>.height(): Int {
    if (isEmpty()) return 0
    return height(root!!)
}

fun <K : Comparable<K>, V : Any> BinaryTreeST<K, V>.height(node: BinaryTreeST.Node<K, V>): Int {
    var leftHeight = 0
    var rightHeight = 0
    if (node.left != null) {
        leftHeight = height(node.left!!)
    }
    if (node.right != null) {
        rightHeight = height(node.right!!)
    }
    return maxOf(leftHeight, rightHeight) + 1
}

/**
 * 继承BinaryTreeST和BinaryTreeST.Node，在put方法种将所有结点替换为HeightNode
 * 在put、deleteMin、deleteMax、delete方法中重新计算路径上子树的高度
 */
class BinaryTreeHeightST<K : Comparable<K>, V : Any> : BinaryTreeST<K, V>() {
    class HeightNode<K : Comparable<K>, V : Any>(key: K,
                                                 value: V,
                                                 left: Node<K, V>? = null,
                                                 right: Node<K, V>? = null,
                                                 count: Int = 1,
                                                 var height: Int = 1) : Node<K, V>(key, value, left, right, count)

    override fun put(key: K, value: V) {
        if (root == null) {
            //将父类默认的Node替换为HeightNode
            root = HeightNode(key, value)
        } else {
            put(root!!, key, value)
        }
    }

    override fun put(node: Node<K, V>, key: K, value: V) {
        when {
            node.key > key -> {
                if (node.left == null) {
                    node.left = HeightNode(key, value)
                } else {
                    put(node.left!!, key, value)
                }
            }
            node.key < key -> {
                if (node.right == null) {
                    node.right = HeightNode(key, value)
                } else {
                    put(node.right!!, key, value)
                }
            }
            else -> node.value = value
        }
        node.count = size(node.left) + size(node.right) + 1
        if (node is HeightNode) {
            calculateHeight(node)
        }
    }

    fun height(): Int {
        return height(root)
    }

    private fun height(node: Node<K, V>?): Int {
        return if (node is HeightNode) {
            node.height
        } else 0
    }

    private fun calculateHeight(node: HeightNode<K, V>) {
        node.height = maxOf(height(node.left), height(node.right)) + 1
    }

    override fun deleteMin(node: Node<K, V>): Node<K, V>? {
        val result = super.deleteMin(node)
        if (result is HeightNode) {
            calculateHeight(result)
        }
        return result
    }

    override fun deleteMax(node: Node<K, V>): Node<K, V>? {
        val result = super.deleteMax(node)
        if (result is HeightNode) {
            calculateHeight(result)
        }
        return result
    }

    override fun delete(node: Node<K, V>, key: K): Node<K, V>? {
        val result = super.delete(node, key)
        if (result is HeightNode) {
            calculateHeight(result)
        }
        return result
    }
}

fun main() {
    testOrderedST(BinaryTreeHeightST())

    val array = Array(100) { random(10000) }
    val st1 = BinaryTreeST<Int, Int>()
    val st2 = BinaryTreeHeightST<Int, Int>()
    array.forEach {
        st1.put(it, 0)
        st2.put(it, 0)
    }
    println("st1.height=${st1.height()}")
    println("st2.height=${st2.height()}")

    st1.deleteMin()
    st1.deleteMax()
    st2.deleteMin()
    st2.deleteMax()
    println("st1.height=${st1.height()}")
    println("st2.height=${st2.height()}")

    repeat(40) {
        val index = random(array.size)
        val key = array[index]
        if (st1.contains(key)) {
            st1.delete(key)
            st2.delete(key)
        }
    }
    println("st1.height=${st1.height()}")
    println("st2.height=${st2.height()}")

    Array(100) { random(10000) }.forEach {
        st1.put(it, 0)
        st2.put(it, 0)
    }
    println("st1.height=${st1.height()}")
    println("st2.height=${st2.height()}")
}