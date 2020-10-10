package chapter3.section2

import chapter3.section1.OrderedST
import chapter3.section1.testOrderedST
import edu.princeton.cs.algs4.Stack

/**
 * 实现一种二叉查找树，不在Node对象中使用计数器，且各个方法使用非递归实现
 */
class BinaryTreeNoCounterAndRecursionST<K : Comparable<K>, V : Any> : OrderedST<K, V> {

    class Node<K : Comparable<K>, V : Any>(val key: K, var value: V, var left: Node<K, V>? = null, var right: Node<K, V>? = null)

    var root: Node<K, V>? = null

    override fun min(): K {
        if (isEmpty()) throw NoSuchElementException()
        var node = root!!
        while (node.left != null) {
            node = node.left!!
        }
        return node.key
    }


    override fun max(): K {
        if (isEmpty()) throw NoSuchElementException()
        var node = root!!
        while (node.right != null) {
            node = node.right!!
        }
        return node.key
    }


    override fun floor(key: K): K {
        if (isEmpty()) throw NoSuchElementException()
        var node = root!!
        var lastLessNode: Node<K, V>? = null
        while (true) {
            when {
                node.key == key -> return key
                node.key < key -> {
                    if (node.right == null) {
                        return node.key
                    }
                    lastLessNode = node
                    node = node.right!!
                }
                else -> {
                    if (node.left == null) {
                        if (lastLessNode == null) {
                            throw NoSuchElementException()
                        } else {
                            return lastLessNode.key
                        }
                    }
                    node = node.left!!
                }
            }
        }
    }

    override fun ceiling(key: K): K {
        if (isEmpty()) throw NoSuchElementException()
        var node = root!!
        var lastGreaterNode: Node<K, V>? = null
        while (true) {
            when {
                node.key == key -> return key
                node.key < key -> {
                    if (node.right == null) {
                        if (lastGreaterNode == null) {
                            throw NoSuchElementException()
                        } else {
                            return lastGreaterNode.key
                        }
                    }
                    node = node.right!!
                }
                else -> {
                    if (node.left == null) return node.key
                    lastGreaterNode = node
                    node = node.left!!
                }
            }
        }
    }

    //练习3.2.12要求舍弃rank()和select()方法，所以其他方法中不会调用这两个方法
    override fun rank(key: K): Int {
        if (isEmpty()) return 0
        var rankSize = 0
        var node = root!!
        while (true) {
            when {
                node.key == key -> return rankSize + size(node.left)
                node.key < key -> {
                    rankSize += size(node.left) + 1
                    if (node.right == null) return rankSize
                    node = node.right!!
                }
                else -> {
                    if (node.left == null) {
                        return rankSize
                    } else {
                        node = node.left!!
                    }
                }
            }
        }
    }

    /**
     * 使用二叉树的非递归前序遍历确定某个结点及所有子结点的大小
     */
    fun size(node: Node<K, V>?): Int {
        if (node == null) return 0
        var size = 0
        val stack = Stack<Node<K, V>>()
        stack.push(node)
        while (!stack.isEmpty) {
            val parent = stack.pop()
            //先放入右结点再放入左结点，取的时候先取左结点再取右结点
            if (parent.right != null) {
                stack.push(parent.right!!)
            }
            if (parent.left != null) {
                stack.push(parent.left!!)
            }
            size++
        }
        return size
    }

    override fun select(k: Int): K {
        if (isEmpty()) throw NoSuchElementException()
        var index = 0
        //使用二叉树的非递归中序遍历查找
        val stack = Stack<Node<K, V>>()
        addAllLeftNode(root!!, stack)
        while (!stack.isEmpty) {
            val node = stack.pop()
            if (index == k) {
                return node.key
            }
            index++
            if (node.right != null) {
                addAllLeftNode(node.right!!, stack)
            }
        }
        throw NoSuchElementException()
    }

    private fun addAllLeftNode(root: Node<K, V>, stack: Stack<Node<K, V>>) {
        var node: Node<K, V>? = root
        while (node != null) {
            stack.push(node)
            node = node.left
        }
    }

    override fun deleteMin() {
        if (isEmpty()) throw NoSuchElementException()
        var node = root!!
        var parent: Node<K, V>? = null
        while (node.left != null) {
            parent = node
            node = node.left!!
        }
        if (parent == null) {
            root = node.right
        } else {
            parent.left = node.right
        }
    }

    override fun deleteMax() {
        if (isEmpty()) throw NoSuchElementException()
        var node = root!!
        var parent: Node<K, V>? = null
        while (node.right != null) {
            parent = node
            node = node.right!!
        }
        if (parent == null) {
            root = node.left
        } else {
            parent.right = node.left
        }
    }

    override fun size(low: K, high: K): Int {
        if (low > high || isEmpty()) return 0
        var total = 0
        val stack = Stack<Node<K, V>>()
        stack.push(root!!)
        while (!stack.isEmpty) {
            val node = stack.pop()
            when {
                node.key < low -> {
                    if (node.right != null) {
                        stack.push(node.right!!)
                    }
                }
                node.key > high -> {
                    if (node.left != null) {
                        stack.push(node.left!!)
                    }
                }
                node.key == low -> {
                    total++
                    if (node.right != null) {
                        stack.push(node.right!!)
                    }
                }
                node.key == high -> {
                    total++
                    if (node.left != null) {
                        stack.push(node.left!!)
                    }
                }
                else -> {
                    total++
                    if (node.left != null) {
                        stack.push(node.left!!)
                    }
                    if (node.right != null) {
                        stack.push(node.right!!)
                    }
                }
            }
        }
        return total
    }

    override fun put(key: K, value: V) {
        if (root == null) {
            root = Node(key, value)
            return
        }
        var node = root!!
        while (true) {
            when {
                node.key == key -> {
                    node.value = value
                    return
                }
                node.key > key -> {
                    if (node.left == null) {
                        node.left = Node(key, value)
                        return
                    } else {
                        node = node.left!!
                    }
                }
                else -> {
                    if (node.right == null) {
                        node.right = Node(key, value)
                        return
                    } else {
                        node = node.right!!
                    }
                }
            }
        }
    }

    override fun get(key: K): V? {
        if (isEmpty()) return null
        var node = root!!
        while (true) {
            when {
                node.key == key -> return node.value
                node.key > key -> {
                    if (node.left == null) return null
                    node = node.left!!
                }
                else -> {
                    if (node.right == null) return null
                    node = node.right!!
                }
            }
        }
    }

    override fun delete(key: K) {
        if (isEmpty()) throw NoSuchElementException()
        var node = root!!
        var parent: Node<K, V>? = null
        while (true) {
            when {
                node.key > key -> {
                    if (node.left == null) throw NoSuchElementException()
                    parent = node
                    node = node.left!!
                }
                node.key < key -> {
                    if (node.right == null) throw NoSuchElementException()
                    parent = node
                    node = node.right!!
                }
                else -> {
                    val newNode: Node<K, V>?
                    if (node.right == null) {
                        newNode = node.left
                    } else {
                        newNode = min(node.right!!)
                        newNode.right = deleteMin(node.right!!)
                        newNode.left = node.left
                    }
                    if (parent == null) {
                        root = newNode
                    } else {
                        //该结点原来是左子结点则新结点依然是左子结点，原来是右子结点则新结点依然是右子结点
                        if (parent.left == node) {
                            parent.left = newNode
                        } else {
                            parent.right = newNode
                        }
                    }
                    return
                }
            }
        }
    }

    private fun min(node: Node<K, V>): Node<K, V> {
        var newNode = node
        while (newNode.left != null) {
            newNode = newNode.left!!
        }
        return newNode
    }

    private fun deleteMin(node: Node<K, V>): Node<K, V>? {
        var childNode = node
        var parent: Node<K, V>? = null
        while (childNode.left != null) {
            parent = childNode
            childNode = childNode.left!!
        }
        if (parent == null) {
            return node.right
        } else {
            parent.left = node.right
            return node
        }
    }

    override fun isEmpty(): Boolean {
        return root == null
    }

    override fun size(): Int {
        return size(root)
    }

    override fun keys(low: K, high: K): Iterable<K> {
        return object : Iterable<K> {
            override fun iterator(): Iterator<K> {
                return object : Iterator<K> {
                    val stack = Stack<Node<K, V>>()

                    init {
                        if (root != null) {
                            addAllLeftNode(root!!)
                        }
                    }

                    private fun addAllLeftNode(root: Node<K, V>) {
                        when {
                            root.key < low -> {
                                var node = root.right
                                while (node != null) {
                                    when {
                                        node.key < low -> node = node.right
                                        node.key == low -> {
                                            stack.push(node)
                                            node = null
                                        }
                                        else -> {
                                            if (node.key in low..high) {
                                                stack.push(node)
                                            }
                                            node = node.left
                                        }
                                    }
                                }
                            }
                            root.key == low -> stack.push(root)
                            else -> {
                                var node: Node<K, V>? = root
                                while (node != null) {
                                    if (node.key in low..high) {
                                        stack.push(node)
                                    }
                                    node = node.left
                                }
                            }
                        }
                    }

                    override fun hasNext(): Boolean {
                        return !stack.isEmpty
                    }

                    override fun next(): K {
                        if (stack.isEmpty) throw NoSuchElementException()
                        val node = stack.pop()
                        if (node.right != null) {
                            addAllLeftNode(node.right!!)
                        }
                        return node.key
                    }

                }
            }

        }
    }

    override fun contains(key: K): Boolean {
        return get(key) != null
    }

    override fun keys(): Iterable<K> {
        return object : Iterable<K> {
            override fun iterator(): Iterator<K> {
                return object : Iterator<K> {
                    val stack = Stack<Node<K, V>>()

                    init {
                        if (root != null) {
                            addAllLeftNode(root!!)
                        }
                    }

                    private fun addAllLeftNode(root: Node<K, V>) {
                        var node: Node<K, V>? = root
                        while (node != null) {
                            stack.push(node)
                            node = node.left
                        }
                    }

                    override fun hasNext(): Boolean {
                        return !stack.isEmpty
                    }

                    override fun next(): K {
                        if (stack.isEmpty) throw NoSuchElementException()
                        val node = stack.pop()
                        if (node.right != null) {
                            addAllLeftNode(node.right!!)
                        }
                        return node.key
                    }

                }
            }
        }
    }
}

fun main() {
    testOrderedST(BinaryTreeNoCounterAndRecursionST())
}