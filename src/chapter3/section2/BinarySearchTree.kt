package chapter3.section2

import chapter3.section1.OrderedST
import chapter3.section1.testOrderedST
import edu.princeton.cs.algs4.Queue

/**
 * 基于二叉查找树实现的有序符号表
 */
open class BinarySearchTree<K : Comparable<K>, V : Any> : OrderedST<K, V> {

    open class Node<K : Comparable<K>, V : Any>(val key: K,
                                                var value: V,
                                                var left: Node<K, V>? = null,
                                                var right: Node<K, V>? = null,
                                                var count: Int = 1) : Comparable<Node<K, V>> {
        override fun compareTo(other: Node<K, V>): Int {
            return key.compareTo(other.key)
        }

        override fun equals(other: Any?): Boolean {
            if (other == null) return false
            if (this === other) return true
            if (other !is Node<*, *>) return false
            return key == other.key
        }

        override fun hashCode(): Int {
            return key.hashCode()
        }
    }

    var root: Node<K, V>? = null

    override fun min(): K {
        if (isEmpty()) throw NoSuchElementException()
        return min(root!!).key
    }

    protected open fun min(node: Node<K, V>): Node<K, V> {
        return if (node.left == null) {
            node
        } else {
            min(node.left!!)
        }
    }

    override fun max(): K {
        if (isEmpty()) throw NoSuchElementException()
        return max(root!!).key
    }

    protected open fun max(node: Node<K, V>): Node<K, V> {
        return if (node.right == null) {
            node
        } else {
            max(node.right!!)
        }
    }

    override fun floor(key: K): K {
        if (isEmpty()) throw NoSuchElementException()
        val node = floor(root!!, key) ?: throw NoSuchElementException()
        return node.key
    }

    protected open fun floor(node: Node<K, V>, key: K): Node<K, V>? {
        return when {
            node.key < key -> {
                if (node.right == null) {
                    node
                } else {
                    //若node.key < key则必定存在小于等于key的键，右侧未找到则返回当前结点
                    floor(node.right!!, key) ?: node
                }
            }
            node.key > key -> {
                if (node.left == null) {
                    null
                } else {
                    floor(node.left!!, key)
                }
            }
            else -> node
        }
    }

    override fun ceiling(key: K): K {
        if (isEmpty()) throw NoSuchElementException()
        val node = ceiling(root!!, key) ?: throw NoSuchElementException()
        return node.key
    }

    protected open fun ceiling(node: Node<K, V>, key: K): Node<K, V>? {
        return when {
            node.key > key -> {
                if (node.left == null) {
                    node
                } else {
                    ceiling(node.left!!, key) ?: node
                }
            }
            node.key < key -> {
                if (node.right == null) {
                    null
                } else {
                    ceiling(node.right!!, key)
                }
            }
            else -> node
        }
    }

    override fun rank(key: K): Int {
        return if (isEmpty()) {
            0
        } else {
            rank(root!!, key)
        }
    }

    protected open fun rank(node: Node<K, V>, key: K): Int {
        return when {
            node.key < key -> {
                when {
                    node.left == null && node.right == null -> 1
                    node.left == null -> 1 + rank(node.right!!, key)
                    node.right == null -> 1 + size(node.left)
                    else -> 1 + size(node.left) + rank(node.right!!, key)
                }
            }
            node.key > key -> {
                when {
                    node.left == null -> 0
                    else -> rank(node.left!!, key)
                }
            }
            else -> size(node.left)
        }
    }

    override fun select(k: Int): K {
        if (isEmpty()) throw NoSuchElementException()
        return select(root!!, k).key
    }

    protected open fun select(node: Node<K, V>, k: Int): Node<K, V> {
        val leftSize = size(node.left)
        return when {
            k == leftSize -> node
            k < leftSize && node.left != null -> select(node.left!!, k)
            k > leftSize && node.right != null -> select(node.right!!, k - leftSize - 1)
            else -> throw NoSuchElementException()
        }
    }

    protected open fun size(node: Node<K, V>?): Int {
        return node?.count ?: 0
    }

    override fun deleteMin() {
        if (isEmpty()) throw NoSuchElementException()
        root = deleteMin(root!!)
    }

    protected open fun deleteMin(node: Node<K, V>): Node<K, V>? {
        if (node.left == null) return node.right
        node.left = deleteMin(node.left!!)
        node.count = size(node.left) + size(node.right) + 1
        return node
    }

    override fun deleteMax() {
        if (isEmpty()) throw NoSuchElementException()
        root = deleteMax(root!!)
    }

    protected open fun deleteMax(node: Node<K, V>): Node<K, V>? {
        if (node.right == null) return node.left
        node.right = deleteMax(node.right!!)
        node.count = size(node.left) + size(node.right) + 1
        return node
    }

    override fun size(low: K, high: K): Int {
        if (low > high) return 0
        return if (contains(high)) {
            rank(high) - rank(low) + 1
        } else {
            rank(high) - rank(low)
        }
    }

    override fun put(key: K, value: V) {
        if (root == null) {
            root = Node(key, value)
        } else {
            put(root!!, key, value)
        }
    }

    protected open fun put(node: Node<K, V>, key: K, value: V) {
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
            else -> node.value = value
        }
        node.count = size(node.left) + size(node.right) + 1
    }

    override fun get(key: K): V? {
        return if (isEmpty()) {
            null
        } else {
            get(root!!, key)?.value
        }
    }

    protected open fun get(node: Node<K, V>, key: K): Node<K, V>? {
        return when {
            node.key < key -> {
                if (node.right == null) {
                    null
                } else {
                    get(node.right!!, key)
                }
            }
            node.key > key -> {
                if (node.left == null) {
                    null
                } else {
                    get(node.left!!, key)
                }
            }
            else -> node
        }
    }

    override fun delete(key: K) {
        if (isEmpty()) throw NoSuchElementException()
        root = delete(root!!, key)
    }

    protected open fun delete(node: Node<K, V>, key: K): Node<K, V>? {
        when {
            node.key > key -> {
                if (node.left == null) {
                    throw NoSuchElementException()
                } else {
                    node.left = delete(node.left!!, key)
                    node.count = size(node.left) + size(node.right) + 1
                }
            }
            node.key < key -> {
                if (node.right == null) {
                    throw NoSuchElementException()
                } else {
                    node.right = delete(node.right!!, key)
                    node.count = size(node.left) + size(node.right) + 1
                }
            }
            else -> {
                return if (node.right == null) {
                    node.left
                } else {
                    val rightMinNode = min(node.right!!)
                    rightMinNode.right = deleteMin(node.right!!)
                    rightMinNode.left = node.left
                    rightMinNode.count = size(rightMinNode.left) + size(rightMinNode.right) + 1
                    rightMinNode
                }
            }
        }
        return node
    }

    override fun isEmpty(): Boolean {
        return root == null
    }

    override fun size(): Int {
        return if (isEmpty()) {
            0
        } else {
            root!!.count
        }
    }

    override fun keys(low: K, high: K): Iterable<K> {
        val queue = Queue<K>()
        addToQueue(root, queue, low, high)
        return queue
    }

    private fun addToQueue(node: Node<K, V>?, queue: Queue<K>, low: K, high: K) {
        if (node == null) return
        if (node.key > low) {
            addToQueue(node.left, queue, low, high)
        }
        if (node.key in low..high) {
            queue.enqueue(node.key)
        }
        if (node.key < high) {
            addToQueue(node.right, queue, low, high)
        }
    }

    override fun contains(key: K): Boolean {
        return get(key) != null
    }

    override fun keys(): Iterable<K> {
        if (isEmpty()) return Queue<K>()
        return keys(min(), max())
    }
}

fun main() {
    testOrderedST(BinarySearchTree())
}