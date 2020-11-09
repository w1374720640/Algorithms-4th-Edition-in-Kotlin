package chapter3.section3

import chapter3.section1.OrderedST
import chapter3.section1.testOrderedST
import chapter3.section2.BinarySearchTree
import chapter3.section2.drawBST
import edu.princeton.cs.algs4.Queue
import edu.princeton.cs.algs4.Stack
import extensions.random
import extensions.shuffle

/**
 * AVL树
 * AVL树是一种二叉查找树，其中任意结点的两颗子树的高度最多相差1
 * （最早的平衡树算法就是基于使用旋转保持AVL树中子树高度的平衡）
 * 证明将其中由高度为偶数的结点指向高度为奇数的结点的链接设为红色就可以得到一颗（完美平衡的）2-3-4树
 * 其中红色链接可以是右链接
 * 附加题：使用AVL树作为数据结构实现符号表的API
 * 一种方法是在每个结点中保存它的高度并递归调用后使用旋转来根据需要调整这个高度
 * 另一种方法是在树的表示中使用红黑链接并使用类似练习3.3.39和练习3.3.40的moveRedLeft()和moveRedRight()的方法
 *
 * 解：证明略
 * 通过在结点中保存它的高度的方法，使用AVL树作为数据结构实现符号表的API
 * AVL树左子树的高度和右子树的高度最多相差1（可以左侧比右侧高，也可以右侧比左侧高）
 */
class AVLTree<K : Comparable<K>, V : Any> : OrderedST<K, V> {

    var root: Node<K, V>? = null

    class Node<K, V>(val key: K,
                     var value: V,
                     var left: Node<K, V>? = null,
                     var right: Node<K, V>? = null,
                     var count: Int = 1,
                     var height: Int = 1)

    private fun Node<K, V>?.getHeight(): Int {
        return this?.height ?: 0
    }

    private fun Node<K, V>.resizeHeight() {
        this.height = maxOf(this.left.getHeight(), this.right.getHeight()) + 1
    }

    private fun Node<K, V>.resizeCount() {
        this.count = size(this.left) + size(this.right) + 1
    }

    private fun rotateLeft(node: Node<K, V>): Node<K, V> {
        val x = node.right!!
        node.right = x.left
        x.left = node

        node.resizeCount()
        node.resizeHeight()
        x.resizeCount()
        x.resizeHeight()
        return x
    }

    private fun rotateRight(node: Node<K, V>): Node<K, V> {
        val x = node.left!!
        node.left = x.right
        x.right = node

        node.resizeCount()
        node.resizeHeight()
        x.resizeCount()
        x.resizeHeight()
        return x
    }

    /**
     * 当左右子树高度相差大于1时需要平衡AVL树，这时有四种情况
     * 1、node.left.height - node.right.height > 1 && node.left.left.height > node.left.right.height
     *    这时直接对根结点右旋就可以了
     * 2、node.left.height - node.right.height > 1 && node.left.left.height < node.left.right.height
     *    需要先将左子结点左旋，让较短的树在右侧，然后再对根结点右旋
     * 3、node.right.height - node.left.height > 1 && node.right.right.height > node.right.left.height
     *    直接对根结点左旋
     * 4、node.right.height - node.left.height > 1 && node.right.right.height < node.right.left.height
     *    需要先将右子结点右旋，让较短的树在左侧，然后再对根结点左旋
     * 参考：https://zh.wikipedia.org/wiki/AVL%E6%A0%91
     */
    private fun balance(node: Node<K, V>): Node<K, V> {
        val heightDiff = node.left.getHeight() - node.right.getHeight()
        if (heightDiff < -1) {
            if (node.right!!.left.getHeight() > node.right!!.right.getHeight()) {
                node.right = rotateRight(node.right!!)
            }
            return rotateLeft(node)
        }
        if (heightDiff > 1) {
            if (node.left!!.left.getHeight() < node.left!!.right.getHeight()) {
                node.left = rotateLeft(node.left!!)
            }
            return rotateRight(node)
        }
        node.resizeCount()
        node.resizeHeight()
        return node
    }

    override fun put(key: K, value: V) {
        root = put(root, key, value)
    }

    private fun put(node: Node<K, V>?, key: K, value: V): Node<K, V> {
        if (node == null) return Node(key, value)
        when {
            key > node.key -> node.right = put(node.right, key, value)
            key < node.key -> node.left = put(node.left, key, value)
            else -> node.value = value
        }
        return balance(node)
    }

    override fun deleteMin() {
        if (isEmpty()) throw NoSuchElementException()
        root = deleteMin(root!!)
    }

    private fun deleteMin(node: Node<K, V>): Node<K, V>? {
        if (node.left == null) return node.right
        node.left = deleteMin(node.left!!)
        return balance(node)
    }

    override fun deleteMax() {
        if (isEmpty()) throw NoSuchElementException()
        root = deleteMax(root!!)
    }

    private fun deleteMax(node: Node<K, V>): Node<K, V>? {
        if (node.right == null) return node.left
        node.right = deleteMax(node.right!!)
        return balance(node)
    }

    override fun delete(key: K) {
        if (isEmpty()) throw NoSuchElementException()
        root = delete(root!!, key)
    }

    private fun delete(node: Node<K, V>, key: K): Node<K, V>? {
        when {
            key > node.key -> {
                if (node.right == null) {
                    throw NoSuchElementException()
                } else {
                    node.right = delete(node.right!!, key)
                }
            }
            key < node.key -> {
                if (node.left == null) {
                    throw NoSuchElementException()
                } else {
                    node.left = delete(node.left!!, key)
                }
            }
            else -> {
                if (node.right == null) {
                    return node.left
                } else {
                    val rightMinNode = min(node.right!!)
                    node.right = deleteMin(node.right!!)
                    rightMinNode.left = node.left
                    rightMinNode.right = node.right
                    return balance(rightMinNode)
                }
            }
        }
        return balance(node)
    }


    //-------------------------- 以下代码和BinarySearchTree中完全相同 ------------------------------------//

    override fun min(): K {
        if (isEmpty()) throw NoSuchElementException()
        return min(root!!).key
    }

    private fun min(node: Node<K, V>): Node<K, V> {
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

    private fun max(node: Node<K, V>): Node<K, V> {
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

    private fun floor(node: Node<K, V>, key: K): Node<K, V>? {
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

    private fun ceiling(node: Node<K, V>, key: K): Node<K, V>? {
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

    private fun rank(node: Node<K, V>, key: K): Int {
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

    private fun select(node: Node<K, V>, k: Int): Node<K, V> {
        val leftSize = size(node.left)
        return when {
            k == leftSize -> node
            k < leftSize && node.left != null -> select(node.left!!, k)
            k > leftSize && node.right != null -> select(node.right!!, k - leftSize - 1)
            else -> throw NoSuchElementException()
        }
    }

    private fun size(node: Node<K, V>?): Int {
        return node?.count ?: 0
    }

    override fun size(low: K, high: K): Int {
        if (low > high) return 0
        return if (contains(high)) {
            rank(high) - rank(low) + 1
        } else {
            rank(high) - rank(low)
        }
    }

    override fun get(key: K): V? {
        return if (isEmpty()) {
            null
        } else {
            get(root!!, key)?.value
        }
    }

    private fun get(node: Node<K, V>, key: K): Node<K, V>? {
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
        return keys(min(), max())
    }
}

fun AVLTree.Node<*, *>?.getHeight(): Int {
    return this?.height ?: 0
}

/**
 * 测试所有结点的左右子结点高度差是否最多为1
 * 左子结点可以比右子结点高1，右子结点也可以比左子结点高1
 */
fun <K : Comparable<K>> checkIsAVLTree(avlTree: AVLTree<K, *>): Boolean {
    if (avlTree.isEmpty()) return true
    val stack = Stack<AVLTree.Node<K, *>>()
    stack.push(avlTree.root!!)
    while (!stack.isEmpty) {
        val node = stack.pop()
        val leftHeight = node.left.getHeight()
        val rightHeight = node.right.getHeight()
        if (leftHeight - rightHeight !in -1..1) {
            return false
        }
        if (node.right != null) {
            stack.push(node.right)
        }
        if (node.left != null) {
            stack.push(node.left)
        }
    }
    return true
}

/**
 * 测试生成的AVL树是否符合定义要求
 */
fun testAVLTree(avlTree: AVLTree<Int, Int>) {
    require(avlTree.isEmpty())
    val array = IntArray(100) { it }
    array.shuffle()
    array.forEach {
        avlTree.put(it, 0)
    }
    check(avlTree.size() == 100)
    check(checkIsAVLTree(avlTree))

    repeat(10) {
        avlTree.delete(avlTree.select(random(avlTree.size())))
    }
    check(avlTree.size() == 90)
    check(checkIsAVLTree(avlTree))

    repeat(10) {
        avlTree.deleteMin()
    }
    check(avlTree.size() == 80)
    check(checkIsAVLTree(avlTree))

    repeat(10) {
        avlTree.deleteMax()
    }
    check(avlTree.size() == 70)
    check(checkIsAVLTree(avlTree))

    println("AVLTree check succeed.")
}

/**
 * 为了绘制AVL树的图形，必须先将AVL树转换成对应结构的普通二叉查找树
 */
fun <K : Comparable<K>, V : Any> AVLTree<K, V>.adapterToBST(): BinarySearchTree<K, V> {
    val bst = BinarySearchTree<K, V>()
    if (this.isEmpty()) return bst
    val avlStack = Stack<AVLTree.Node<K, V>>()
    val bstStack = Stack<BinarySearchTree.Node<K, V>>()
    avlStack.push(this.root!!)
    val binaryTreeRoot = BinarySearchTree.Node(root!!.key, root!!.value, count = root!!.count)
    bst.root = binaryTreeRoot
    bstStack.push(binaryTreeRoot)

    while (!avlStack.isEmpty) {
        val avlNode = avlStack.pop()
        val bstNode = bstStack.pop()
        if (avlNode.right != null) {
            val bstRightNode = BinarySearchTree.Node(avlNode.right!!.key, avlNode.right!!.value, count = avlNode.right!!.count)
            bstNode.right = bstRightNode
            avlStack.push(avlNode.right)
            bstStack.push(bstRightNode)
        }
        if (avlNode.left != null) {
            val bstLeftNode = BinarySearchTree.Node(avlNode.left!!.key, avlNode.left!!.value, count = avlNode.left!!.count)
            bstNode.left = bstLeftNode
            avlStack.push(avlNode.left)
            bstStack.push(bstLeftNode)
        }
    }
    return bst
}

fun main() {
    testOrderedST(AVLTree())
    testAVLTree(AVLTree())

    val avlTree = AVLTree<Int, Int>()
    val array = IntArray(50) { it }
    array.shuffle()
    array.forEach { avlTree.put(it, 0) }
    repeat(10) {
        avlTree.delete(avlTree.select(random(avlTree.size())))
    }
    val bst = avlTree.adapterToBST()
    drawBST(bst, showKey = false)
}