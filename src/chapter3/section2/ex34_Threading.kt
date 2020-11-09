package chapter3.section2

import chapter3.section1.testOrderedST
import extensions.random
import extensions.shuffle

/**
 * 线性符号表
 * 你的目标是实现一个扩展的符号表ThreadedST，支持以下两个运行时间为常数的操作
 * Key next(Key key)，key的下一个键（若key为最大键则返回空）
 * Key prev(Key key)，key的上一个键（若key为最小键则返回空）
 * 要做到这一点需要在结点中增加pred和succ两个变量来保存结点的前趋和后继结点
 * 并相应修改put()、deleteMin()、deleteMax()和delete()方法来维护这两个变量
 *
 * 解：next(): 先通过floor方法找到键小于等于key的结点，未找到则返回最左侧结点，否则返回该结点的succ变量
 * prev(): 先通过ceiling方法找到键大于等于key的结点，未找到则返回最右侧结点，否则返回该结点的pred变量
 * put()方法在添加新结点时需要修改父结点 前趋结点的后继结点或后继结点的前趋结点
 * deleteMin()、deleteMax()和delete()方法需要修改被删除结点 前趋结点的后继结点和后继结点的前趋结点
 * 注意：next()和prev()方法运行时间为对数级而不是常数，因为根据key查找结点的方法是对数级
 */
class ThreadedST<K : Comparable<K>, V : Any> : BinarySearchTree<K, V>() {

    class ThreadedNode<K : Comparable<K>, V : Any>(
            key: K,
            value: V,
            left: Node<K, V>? = null,
            right: Node<K, V>? = null,
            count: Int = 1,
            var pred: Node<K, V>? = null, //前趋结点
            var succ: Node<K, V>? = null //后继结点
    ) : BinarySearchTree.Node<K, V>(key, value, left, right, count)

    //强制类型转换的封装方法
    private fun Node<K, V>.transform(): ThreadedNode<K, V> {
        return this as ThreadedNode<K, V>
    }

    fun next(key: K): K? {
        if (root == null) return null
        val floorNode = floor(root!!, key)
        return if (floorNode == null) {
            min()
        } else {
            floorNode.transform().succ?.key
        }
    }

    fun prev(key: K): K? {
        if (root == null) return null
        val ceilingNode = ceiling(root!!, key)
        return if (ceilingNode == null) {
            max()
        } else {
            ceilingNode.transform().pred?.key
        }
    }

    override fun put(key: K, value: V) {
        if (root == null) {
            root = ThreadedNode(key, value)
        } else {
            put(root!!, key, value)
        }
    }

    override fun put(node: Node<K, V>, key: K, value: V) {
        when {
            node.key > key -> {
                if (node.left == null) {
                    node.left = ThreadedNode(key, value, pred = node.transform().pred, succ = node)
                    node.transform().pred?.transform()?.succ = node.left
                    node.transform().pred = node.left
                } else {
                    put(node.left!!, key, value)
                }
            }
            node.key < key -> {
                if (node.right == null) {
                    node.right = ThreadedNode(key, value, pred = node, succ = node.transform().succ)
                    node.transform().succ?.transform()?.pred = node.right
                    node.transform().succ = node.right
                } else {
                    put(node.right!!, key, value)
                }
            }
            else -> node.value = value
        }
        node.count = size(node.left) + size(node.right) + 1
    }

    override fun deleteMin(node: Node<K, V>): Node<K, V>? {
        if (node.left == null) {
            node.transform().pred?.transform()?.succ = node.transform().succ
            node.transform().succ?.transform()?.pred = node.transform().pred
            return node.right
        }
        node.left = deleteMin(node.left!!)
        node.count = size(node.left) + size(node.right) + 1
        return node
    }

    override fun deleteMax(node: Node<K, V>): Node<K, V>? {
        if (node.right == null) {
            node.transform().pred?.transform()?.succ = node.transform().succ
            node.transform().succ?.transform()?.pred = node.transform().pred
            return node.left
        }
        node.right = deleteMax(node.right!!)
        node.count = size(node.left) + size(node.right) + 1
        return node
    }

    override fun delete(node: Node<K, V>, key: K): Node<K, V>? {
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
                //必须先解除删除结点的前后关系，再将右侧最小值提升到空位
                node.transform().pred?.transform()?.succ = node.transform().succ
                node.transform().succ?.transform()?.pred = node.transform().pred
                return if (node.right == null) {
                    node.left
                } else {
                    //右子结点不为空，则右侧最小值为该结点的后继结点
                    val rightMinNode = node.transform().succ!!
                    rightMinNode.right = deleteMin(node.right!!)
                    //deleteMin方法解除了rightMinNode结点与其他结点的关系，需要重新添加上
                    rightMinNode.transform().pred?.transform()?.succ = rightMinNode
                    rightMinNode.transform().succ?.transform()?.pred = rightMinNode
                    rightMinNode.left = node.left
                    rightMinNode.count = size(rightMinNode.left) + size(rightMinNode.right) + 1
                    rightMinNode
                }
            }
        }
        return node
    }
}

private fun testThreadedST() {
    val size = 100
    val array = Array(size) { it }
    array.shuffle()
    val st = ThreadedST<Int, Int>()
    array.forEach {
        st.put(it, 0)
    }
    var min = st.min()
    var next = st.next(min)
    var count = 1
    while (next != null) {
        if (min >= next) throw IllegalStateException("Check failed.")
        min = next
        next = st.next(min)
        count++
    }
    check(count == size)

    var max = st.max()
    next = st.prev(max)
    count = 1
    while (next != null) {
        if (max <= next) throw IllegalStateException("Check failed.")
        max = next
        next = st.prev(max)
        count++
    }
    check(count == size)

    st.deleteMin()
    st.deleteMin()
    st.deleteMin()
    st.deleteMax()
    st.deleteMax()
    st.deleteMax()
    repeat(10) {
        val key = random(size)
        if (st.contains(key)) {
            st.delete(key)
        }
    }

    min = st.min()
    next = st.next(min)
    count = 1
    while (next != null) {
        if (min >= next) throw IllegalStateException("Check failed.")
        min = next
        next = st.next(min)
        count++
    }
    check(count == st.size())

    max = st.max()
    next = st.prev(max)
    count = 1
    while (next != null) {
        if (max <= next) throw IllegalStateException("Check failed.")
        max = next
        next = st.prev(max)
        count++
    }
    check(count == st.size())
    println("ThreadedST check succeed.")
}

fun main() {
    testOrderedST(ThreadedST())
    testThreadedST()
}