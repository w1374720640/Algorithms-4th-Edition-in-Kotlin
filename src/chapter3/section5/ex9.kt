package chapter3.section5

import chapter3.section2.BinarySearchTree

/**
 * 修改二叉查找树BST，允许在树中保存重复的键
 * 对于get()方法，返回给定键所关联的任意值，对于delete()方法，删除树中所有和给顶尖相等的结点
 */
class DuplicateKeysBST<K : Comparable<K>, V : Any> : BinarySearchTree<K, V>(), DuplicateKeysST<K, V> {
    class Node<K : Comparable<K>, V : Any>(
            key: K,
            value: V, // 创建结点时的Value
            left: BinarySearchTree.Node<K, V>? = null,
            right: BinarySearchTree.Node<K, V>? = null,
            count: Int = 1,
            val set: SequentialSearchSET<V> = SequentialSearchSET() // 所有的Value集合
    ) : BinarySearchTree.Node<K, V>(key, value, left, right, count) {
        init {
            set.add(value)
        }
    }

    override fun getAllValues(key: K): Iterable<V>? {
        if (isEmpty()) return null
        val node = get(root!!, key)
        return (node as? Node<K, V>)?.set
    }

    override fun put(key: K, value: V) {
        if (root == null) {
            root = Node(key, value)
        } else {
            put(root!!, key, value)
        }
    }

    override fun put(node: BinarySearchTree.Node<K, V>, key: K, value: V) {
        when {
            node.key > key -> {
                if (node.left == null) {
                    node.left = Node(key, value)
                } else {
                    put(node.left!!, key, value)
                }
            }
            node.key < key -> {
                if (node.right == null) {
                    node.right = Node(key, value)
                } else {
                    put(node.right!!, key, value)
                }
            }
            else -> (node as Node<K, V>).set.add(value)
        }
        node.count = size(node.left) + size(node.right) + 1
    }
}

fun main() {
    testDuplicateKeysST(DuplicateKeysBST())
}