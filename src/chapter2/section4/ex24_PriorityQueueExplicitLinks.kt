package chapter2.section4

import chapter2.section1.cornerCases
import edu.princeton.cs.algs4.Queue
import extensions.readInt
import extensions.safeCall

/**
 * 使用链接的优先队列
 * 用堆有序的二叉树实现一个优先队列，但使用链表结构替代数组
 * 每个结点都需要三个链接：两个向下，一个向上
 * 你的实现即使在无法预知队列大小的情况下也能保证优先队列的基本操作所需的时间为对数级别
 *
 * 解：使用链表构建完全二叉堆时，上浮和下沉操作都比数组更容易实现，因为不用计算索引了
 * 但是插入元素时，很难找到应该插入的位置，删除时，很难找到上一个值的位置，需要遍历二叉树
 * 解决方法：
 * 1、类中记录根节点和最后一个结点
 * 2、插入新值时，从最后一个结点向上遍历，如果自己是左子结点，将新值添加到右子节点的位置，
 *    否则查找父节点，直到该结点是左子节点或是根节点，
 *    如果该结点是左子节点，则找到右子节点的最左侧叶子结点，插入左子结点
 *    如果该结点是根节点，则遍历根节点的左子节点，找到最左侧的位置插入
 *    最多需要查找2lgN次
 * 3、删除最大值时，和插入新值类似，只不过遍历方向相反，找到左侧最后一个值或上一行的最后一个值
 * 插入的三种情况（↑表示新插入的位置）：
 *              a                            a                         a
 *        b            c                b            c             b           c
 *     d    e     f       g         d     e      f     g       d     e     f      g
 *   h  i j  ↑                    h   i  j  k  ↑            ↑
 */
class LinkedHeapMaxPriorityQueue<T : Comparable<T>> : MaxPriorityQueue<T> {
    private var size = 0
    private var root: Node<T>? = null
    private var last: Node<T>? = null

    private class Node<E : Comparable<E>>(var item: E,
                                          var parent: Node<E>? = null,
                                          var leftChild: Node<E>? = null,
                                          var rightChild: Node<E>? = null) : Comparable<Node<E>> {
        fun isRoot() = parent == null
        fun hasChild() = leftChild != null || rightChild != null
        fun isLeftChild() = parent != null && this === parent?.leftChild
        fun isRightChild() = parent != null && this === parent?.rightChild

        override fun compareTo(other: Node<E>): Int {
            return item.compareTo(other.item)
        }
    }

    override fun insert(value: T) {
        val newNode = createNewNode(value)
        swim(newNode)
    }

    override fun max(): T {
        if (isEmpty()) throw NoSuchElementException("Priority Queue is empty!")
        return root!!.item
    }

    override fun delMax(): T {
        val max = max()
        if (size == 1) {
            root = null
            last = null
            size = 0
        } else {
            val secondLast = getSecondLast()
            swap(root!!, last!!)
            if (last!!.isLeftChild()) {
                last!!.parent!!.leftChild = null
            } else {
                last!!.parent!!.rightChild = null
            }
            last!!.parent = null
            last = secondLast
            size--
            sink(root!!)
        }
        return max
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun size(): Int {
        return size
    }

    override fun iterator(): Iterator<T> {
        //对二叉树使用广度优先的非递归遍历
        return object : Iterator<T> {
            private val queue = Queue<Node<T>>()

            init {
                if (root != null) {
                    queue.enqueue(root)
                }
            }

            override fun hasNext(): Boolean {
                return !queue.isEmpty
            }

            override fun next(): T {
                val node = queue.dequeue()
                if (node.leftChild != null) {
                    queue.enqueue(node.leftChild)
                }
                if (node.rightChild != null) {
                    queue.enqueue(node.rightChild)
                }
                return node.item
            }

        }
    }

    private fun swap(a: Node<T>, b: Node<T>) {
        val temp = a.item
        a.item = b.item
        b.item = temp
    }

    private fun createNewNode(item: T): Node<T> {
        var node = last
        //空树
        if (node == null) {
            node = Node(item)
            root = node
            last = node
            size = 1
            return node
        }
        var parent = node.parent
        val newNode: Node<T>
        //只有一个根结点
        if (parent == null) {
            newNode = Node(item, parent = node)
            node.leftChild = newNode
        } else if (node.isLeftChild()) {
            //最后一个结点是左结点，则新结点直接放在父节点的右子结点位置
            newNode = Node(item, parent = parent)
            parent.rightChild = newNode
        } else {
            //从父结点向上查找，找到第一个不是右子结点的结点
            while (node != null && parent != null) {
                if (node.isLeftChild()) {
                    break
                } else {
                    node = parent
                    parent = node.parent
                }
            }
            //如果所有父结点都是右子结点，说明二叉树已满，需要加一层
            if (parent == null) {
                parent = getLeftmostChild(root!!)
            } else {
                //如果找到某个结点是左子结点，则应该添加到右子结点的最左侧叶子结点
                parent = getLeftmostChild(parent.rightChild!!)
            }
            newNode = Node(item, parent = parent)
            parent.leftChild = newNode
        }
        last = newNode
        size++
        return newNode
    }

    private fun getLeftmostChild(node: Node<T>): Node<T> {
        var parent = node
        while (parent.leftChild != null) {
            parent = parent.leftChild!!
        }
        return parent
    }

    private fun getRightmostChild(node: Node<T>): Node<T> {
        var parent = node
        while (parent.rightChild != null) {
            parent = parent.rightChild!!
        }
        return parent
    }

    private fun getSecondLast(): Node<T> {
        var node = last
        if (node == null) {
            throw NoSuchElementException()
        }
        if (node.isRightChild()) {
            return node.parent!!.leftChild!!
        }
        var parent = node.parent
        while (node != null && parent != null) {
            if (node.isRightChild()) {
                break
            } else {
                node = parent
                parent = node.parent
            }
        }
        return if (parent == null) {
            getRightmostChild(root!!)
        } else {
            getRightmostChild(parent.leftChild!!)
        }

    }

    /**
     * 上浮
     */
    private fun swim(node: Node<T>) {
        var child = node
        var parent = node.parent
        while (parent != null && child > parent) {
            swap(child, parent)
            child = parent
            parent = parent.parent
        }
    }

    /**
     * 下沉
     */
    private fun sink(node: Node<T>) {
        var parent = node
        var leftChild = node.leftChild
        var rightChild = node.rightChild
        while (leftChild != null) {
            if (rightChild != null && leftChild < rightChild) {
                leftChild = rightChild
            }
            if (parent < leftChild) {
                swap(parent, leftChild)
                parent = leftChild
                leftChild = parent.leftChild
                rightChild = parent.rightChild
            } else {
                break
            }
        }
    }
}

fun main() {
    //检查实现的优先队列是否正确
    cornerCases { array ->
        val priorityQueue = LinkedHeapMaxPriorityQueue<Double>()
        array.forEach { priorityQueue.insert(it) }
        for (i in array.size - 1 downTo 0) {
            array[i] = priorityQueue.delMax()
        }
    }
    val priorityQueue = LinkedHeapMaxPriorityQueue<Int>()
    println("Please input command:")
    println("0: exit, 1: insert, 2: max, 3: delMax, 4: isEmpty, 5: size, 6: joinToString")
    while (true) {
        safeCall {
            when (readInt("command: ")) {
                0 -> return
                1 -> {
                    val value = readInt("insert value: ")
                    priorityQueue.insert(value)
                }
                2 -> println(priorityQueue.max())
                3 -> println(priorityQueue.delMax())
                4 -> println("isEmpty=${priorityQueue.isEmpty()}")
                5 -> println("size=${priorityQueue.size()}")
                6 -> println(priorityQueue.joinToString())
            }
        }
    }
}