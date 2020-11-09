package chapter3.section2

import chapter3.section1.testOrderedST

/**
 * 为二叉查找树添加一个方法avgCompares()来计算一颗给定的树中的一次随机命中查找平均所需要的比较次数
 * （即树的各结点路径长度之和除以树的大小再加1）
 * 实现两种方案：一种使用递归（用时为线性级别，所需空间和树高成正比）
 * 一种模仿size()在每个结点中添加一个变量（所需空间为线性级别，查询耗时为常数）
 */
fun <K : Comparable<K>, V : Any> BinarySearchTree<K, V>.avgCompares(): Int {
    if (isEmpty()) return 0
    return compares(root!!) / size() + 1
}

fun <K : Comparable<K>, V : Any> BinarySearchTree<K, V>.avgComparesDouble(): Double {
    if (isEmpty()) return 0.0
    return compares(root!!) / size().toDouble() + 1
}

private fun <K : Comparable<K>, V : Any> compares(node: BinarySearchTree.Node<K, V>): Int {
    var leftCompares = 0
    if (node.left != null) {
        leftCompares = compares(node.left!!)
    }
    var rightCompares = 0
    if (node.right != null) {
        rightCompares = compares(node.right!!)
    }
    return leftCompares + rightCompares + node.count - 1
}

class BinarySearchTreeCompares<K : Comparable<K>, V : Any> : BinarySearchTree<K, V>() {
    class ComparesNode<K : Comparable<K>, V : Any>(key: K,
                                                   value: V,
                                                   left: Node<K, V>? = null,
                                                   right: Node<K, V>? = null,
                                                   count: Int = 1,
                                                   var compare: Int = 0) : Node<K, V>(key, value, left, right, count)

    override fun put(key: K, value: V) {
        if (root == null) {
            //将父类默认的Node替换为HeightNode
            root = ComparesNode(key, value)
        } else {
            put(root!!, key, value)
        }
    }

    override fun put(node: Node<K, V>, key: K, value: V) {
        when {
            node.key > key -> {
                if (node.left == null) {
                    node.left = ComparesNode(key, value)
                } else {
                    put(node.left!!, key, value)
                }
            }
            node.key < key -> {
                if (node.right == null) {
                    node.right = ComparesNode(key, value)
                } else {
                    put(node.right!!, key, value)
                }
            }
            else -> node.value = value
        }
        node.count = size(node.left) + size(node.right) + 1
        if (node is ComparesNode) {
            calculateCompare(node)
        }
    }

    fun avgCompares(): Int {
        if (isEmpty()) return 0
        return compare(root) / size() + 1
    }

    private fun compare(node: Node<K, V>?): Int {
        return if (node is ComparesNode) {
            node.compare
        } else 0
    }

    private fun calculateCompare(node: ComparesNode<K, V>) {
        node.compare = compare(node.left) + compare(node.right) + node.count - 1
    }

    override fun deleteMin(node: Node<K, V>): Node<K, V>? {
        val result = super.deleteMin(node)
        if (result is ComparesNode) {
            calculateCompare(result)
        }
        return result
    }

    override fun deleteMax(node: Node<K, V>): Node<K, V>? {
        val result = super.deleteMax(node)
        if (result is ComparesNode) {
            calculateCompare(result)
        }
        return result
    }

    override fun delete(node: Node<K, V>, key: K): Node<K, V>? {
        val result = super.delete(node, key)
        if (result is ComparesNode) {
            calculateCompare(result)
        }
        return result
    }
}

fun main() {
    testOrderedST(BinarySearchTreeCompares())

    val checkArray = arrayOf(
            arrayOf(2, 1, 3) to 1,
            arrayOf(1, 2, 3) to 2,
            arrayOf(5, 2, 7, 1, 3, 4, 6, 8, 9, 10) to 3
    )
    checkArray.forEach { pair ->
        val st1 = BinarySearchTree<Int, Int>()
        val st2 = BinarySearchTreeCompares<Int, Int>()
        pair.first.forEach {
            st1.put(it, 0)
            st2.put(it, 0)
        }
        check(st1.avgCompares() == pair.second)
        check(st2.avgCompares() == pair.second)
    }
    println("check succeed.")
}