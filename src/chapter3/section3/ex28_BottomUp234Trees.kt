package chapter3.section3

import extensions.shuffle

/**
 * 自底向上的2-3-4树
 * 使用平衡2-3-4树作为数据结构实现符号表的基本API
 * 在树的表示中使用红黑链接并用和算法3.4相同的递归方式实现自底向上的插入
 * 你的插入应该只需要分解查找路径底部的4-结点
 *
 * 解：这题并没有正确实现，如果只分解底部4-结点会出现连续红色结点，参考main函数中绘制出来的图形
 */
class BottomUp234Tree<K : Comparable<K>, V : Any> : RedBlackBST<K, V>() {

    override fun put(key: K, value: V) {
        if (isEmpty()) {
            root = Node(key, value, color = BLACK)
        } else {
            root = put(root, key, value)
            root!!.color = BLACK
        }
    }

    override fun put(node: Node<K, V>?, key: K, value: V): Node<K, V> {
        if (node == null) return Node(key, value)
        var h: Node<K, V> = node

        when {
            key > h.key -> h.right = put(h.right, key, value)
            key < h.key -> h.left = put(h.left, key, value)
            else -> h.value = value
        }

        if (!h.left.isRed() && h.right.isRed()) h = rotateLeft(h)
        if (h.left.isRed() && h.left?.left.isRed()) h = rotateRight(h)
        if (h.left.isLeafNode() && h.right.isLeafNode()) {
            if (h.left.isRed() && h.right.isRed()) {
                flipColors(h)
            }
        }

        resize(h)
        return h
    }

    /**
     * 判断该结点是否是叶子结点
     */
    private fun Node<K, V>?.isLeafNode(): Boolean {
        return this != null && this.left == null && this.right == null
    }
}

fun main() {
    val array = IntArray(50) { it }
    array.shuffle()
    val bottomUp234Tree = BottomUp234Tree<Int, Int>()
    array.forEach {
        bottomUp234Tree.put(it, 0)
    }
    drawRedBlackBST(bottomUp234Tree, showKey = false)
}