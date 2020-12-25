package chapter3.section3

import chapter3.section1.OrderedST
import chapter3.section1.testOrderedST
import edu.princeton.cs.algs4.Queue
import extensions.shuffle

/**
 * 基于红黑树实现的有序符号表
 * 除了put()、deleteMin()、deleteMax()、delete()这几个方法外，其余方法的实现和二叉查找树相同
 */
open class RedBlackBST<K : Comparable<K>, V : Any> : OrderedST<K, V> {
    companion object {
        const val RED = true
        const val BLACK = false
    }

    open class Node<K, V>(var key: K,
                          var value: V,
                          var left: Node<K, V>? = null,
                          var right: Node<K, V>? = null,
                          var count: Int = 1,
                          var color: Boolean = RED)

    protected open fun Node<K, V>?.isRed(): Boolean {
        if (this == null) return false
        return color == RED
    }

    var root: Node<K, V>? = null

    /**
     * 左旋，不改变有序性和完美平衡
     * 用于将右侧的红色链接调换到左侧，红色链接逆时针旋转，返回右子结点
     */
    protected open fun rotateLeft(node: Node<K, V>): Node<K, V> {
        require(node.right != null && node.right.isRed())
        val x = node.right!!
        x.color = node.color
        node.right = x.left
        node.color = RED
        x.left = node
        x.count = node.count
        resize(node)
        return x
    }

    /**
     * 右旋，不改变有序性和完美平衡
     * 用于将左侧的红色链接调换到右侧，红色链接顺时针旋转，返回左子结点
     */
    protected open fun rotateRight(node: Node<K, V>): Node<K, V> {
        require(node.left != null && node.left.isRed())
        val x = node.left!!
        x.color = node.color
        node.left = x.right
        node.color = RED
        x.right = node
        x.count = node.count
        resize(node)
        return x
    }

    /**
     * 翻转根节点和左右子结点的颜色，不改变有序性和完美平衡
     * 要求根结点为红色，左右子结点都为黑色，或根结点为黑色，左右子结点为红色
     * 和正文中给出的代码不同
     */
    protected open fun flipColors(node: Node<K, V>) {
        require(node.left != null && node.right != null)
        require((!node.isRed() && node.left.isRed() && node.right.isRed())
                || (node.isRed() && !node.left.isRed() && !node.right.isRed()))
        node.left!!.color = !node.left!!.color
        node.right!!.color = !node.right!!.color
        node.color = !node.color
    }

    protected open fun resize(node: Node<K, V>) {
        node.count = size(node.left) + size(node.right) + 1
    }

    override fun put(key: K, value: V) {
        if (root == null) {
            root = Node(key, value, color = BLACK)
        } else {
            root = put(root, key, value)
            root!!.color = BLACK
        }
    }

    /**
     * 先在对应位置插入红色结点，再递归向上平衡红黑树
     */
    protected open fun put(node: Node<K, V>?, key: K, value: V): Node<K, V> {
        if (node == null) return Node(key, value)
        when {
            key < node.key -> node.left = put(node.left, key, value)
            key > node.key -> node.right = put(node.right, key, value)
            else -> node.value = value
        }

        var h: Node<K, V> = node
        if (!h.left.isRed() && h.right.isRed()) h = rotateLeft(h)
        if (h.left.isRed() && h.left?.left.isRed()) h = rotateRight(h)
        if (h.left.isRed() && h.right.isRed()) flipColors(h)

        resize(h)
        return h
    }

    /**
     * 让左子结点变为红色，不改变有序性和完美平衡
     * 和rotateLeft()方法的前置条件不同，在删除结点时将2-3树临时变更为2-3-4树
     * 练习3.3.39中给出的答案少了一个flipColors()操作
     */
    protected open fun moveRedLeft(node: Node<K, V>): Node<K, V> {
        require(node.isRed() && !node.left.isRed() && !node.left?.left.isRed())
        var h = node
        //父结点与左右子结点组成4-结点
        flipColors(h)
        if (h.right?.left.isRed()) {
            //如果右结点的左子结点为红色，则组成的是5-结点而不是4-结点，需要变换
            //父结点与左子结点组成3-结点，右子结点的左子结点替代原父结点的位置
            h.right = rotateRight(h.right!!)
            h = rotateLeft(h)
            flipColors(h)
        }
        return h
    }

    /**
     * 让右子结点变为红色，不改变有序性和完美平衡
     * 练习3.3.40中给出的答案少了一个flipColors()也就算了，还多了一个取反操作，应该为if(isRed(h.left.left))
     */
    protected open fun moveRedRight(node: Node<K, V>): Node<K, V> {
        require(node.isRed() && !node.right.isRed() && !node.right?.left.isRed())
        var h = node
        //父结点与左右子结点组成4-结点
        flipColors(h)
        if (h.left?.left.isRed()) {
            //如果左子结点的左子结点为红色，则组成的是5-结点而不是4-结点，需要变换
            //父结点与右子结点组成3-结点，左子结点替代原父结点的位置
            h = rotateRight(h)
            flipColors(h)
        }
        return h
    }

    /**
     * 删除结点后向上递归调整平衡
     */
    protected open fun balance(node: Node<K, V>): Node<K, V> {
        var h = node
        if (h.right.isRed()) h = rotateLeft(h)

        if (h.left.isRed() && h.left?.left.isRed()) h = rotateRight(h)
        if (h.left.isRed() && h.right.isRed()) flipColors(h)

        resize(h)
        return h
    }

    override fun deleteMin() {
        if (isEmpty()) throw NoSuchElementException()
        if (!root?.left.isRed() && !root?.right.isRed()) root!!.color = RED
        root = deleteMin(root!!)
        if (!isEmpty()) root!!.color = BLACK
    }

    protected open fun deleteMin(node: Node<K, V>): Node<K, V>? {
        var h = node
        if (h.left == null) return null
        if (!h.left.isRed() && !h.left?.left.isRed()) h = moveRedLeft(h)
        h.left = deleteMin(h.left!!)
        return balance(h)
    }

    override fun deleteMax() {
        if (isEmpty()) throw NoSuchElementException()
        if (!root!!.left.isRed() && !root!!.right.isRed()) root!!.color = RED
        root = deleteMax(root!!)
        if (!isEmpty()) root!!.color = BLACK
    }

    protected open fun deleteMax(node: Node<K, V>): Node<K, V>? {
        var h = node
        //因为正常的红黑树红色链接只可能在左侧，所以deleteMin()方法没有这行代码
        if (h.left.isRed()) h = rotateRight(h)
        if (h.right == null) return null
        if (!h.right.isRed() && !h.right?.left.isRed()) h = moveRedRight(h)
        h.right = deleteMax(h.right!!)
        return balance(h)
    }

    override fun delete(key: K) {
        if (!contains(key)) throw NoSuchElementException()
        if (!root!!.left.isRed() && !root!!.right.isRed()) root!!.color = RED
        root = delete(root!!, key)
        if (!isEmpty()) root!!.color = BLACK
    }

    protected open fun delete(node: Node<K, V>, key: K): Node<K, V>? {
        var h = node
        if (key < h.key) {
            if (!h.left.isRed() && !h.left?.left.isRed()) h = moveRedLeft(h)
            h.left = delete(h.left!!, key)
        } else {
            if (h.left.isRed()) h = rotateRight(h)
            if (key == h.key && h.right == null) return null
            if (!h.right.isRed() && !h.right?.left.isRed()) h = moveRedRight(h)
            if (key == h.key) {
                val rightMinNode = get(h.right!!, min(h.right!!).key)!!
                h.value = rightMinNode.value
                h.key = rightMinNode.key
                h.right = deleteMin(h.right!!)
            } else {
                h.right = delete(h.right!!, key)
            }
        }
        return balance(h)
    }


//-------------------------- 以下代码和BinarySearchTree中完全相同 ------------------------------------//

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
    testOrderedST(RedBlackBST())
    testRedBlackBST(RedBlackBST())

    val array = IntArray(20) { it }
    array.shuffle()
    val bst = RedBlackBST<Int, Int>()
    array.forEach {
        bst.put(it, 0)
    }
    drawRedBlackBST(bst)
}