package chapter3.section5

import chapter3.section3.RedBlackBST

/**
 * 修改红黑树RedBlackBST，允许在树中保存重复的键
 * 对于get()方法，返回给定键所关联的任意值，对于delete()方法，删除树中所有和给顶尖相等的结点
 */
class DuplicateKeysRedBlackBST<K : Comparable<K>, V : Any> : RedBlackBST<K, V>(), DuplicateKeysST<K, V> {
    class Node<K : Comparable<K>, V : Any>(
            key: K,
            value: V,
            left: RedBlackBST.Node<K, V>? = null,
            right: RedBlackBST.Node<K, V>? = null,
            count: Int = 1,
            color: Boolean = RED,
            val set: SequentialSearchSET<V> = SequentialSearchSET()
    ) : RedBlackBST.Node<K, V>(key, value, left, right, count, color) {
        init {
            set.add(value)
        }
    }

    override fun getAllValues(key: K): Iterable<V>? {
        if (isEmpty()) return null
        val node = get(root!!, key)
        return (node as? Node)?.set?.keys()
    }

    override fun put(key: K, value: V) {
        if (root == null) {
            root = Node(key, value, color = BLACK)
        } else {
            root = put(root, key, value)
            root!!.color = BLACK
        }
    }

    override fun put(node: RedBlackBST.Node<K, V>?, key: K, value: V): RedBlackBST.Node<K, V> {
        if (node == null) return Node(key, value)
        when {
            key < node.key -> node.left = put(node.left, key, value)
            key > node.key -> node.right = put(node.right, key, value)
            else -> (node as Node).set.add(value)
        }

        var h: RedBlackBST.Node<K, V> = node
        if (!h.left.isRed() && h.right.isRed()) h = rotateLeft(h)
        if (h.left.isRed() && h.left?.left.isRed()) h = rotateRight(h)
        if (h.left.isRed() && h.right.isRed()) flipColors(h)

        resize(h)
        return h
    }
}

fun main() {
    testDuplicateKeysST(DuplicateKeysRedBlackBST())
}