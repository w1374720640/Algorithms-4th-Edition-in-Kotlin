package chapter3.section5

import chapter3.section3.RedBlackBST
import chapter3.section4.LinearProbingHashST
import edu.princeton.cs.algs4.Queue

/**
 * 列表
 * 实现表3.5.7中的API
 * 提示：使用两个符号表，一个用来快速定位列表中的第i个元素，另一个用来快速根据元素查找
 * （Java的java.util.List包含类似的方法，但它的实现的操作并不都是高效的）
 *
 * 解：这题要求所有的API都是高效的（时间复杂度小于等于O(lgN)）
 * 根据提示，需要使用两个符号表
 * 第一个符号表用来快速定位列表中的第i个元素
 * 就查找操作而言，BinarySearchST、RedBlackBST、LinearProbingHashST都可以满足要求
 * 但是对于指定位置的插入操作，需要修改其他0~N个元素的索引，时间复杂度均大于O(lgN)
 * 这里使用一个取巧的方法：元素的索引不用Int表示，用Double代替，每个元素插入后，索引值不会再改变
 * 当插入新值时，取需要插入位置两侧元素的索引值，平均值就是新元素的索引
 * 用红黑树保存键值对，可以保证每次操作复杂度都是O(lgN)，而且可以支持顺序操作（LinearProbingHashST不行）
 * 第二个符号表用来根据元素查找，因为List可以存放任意类型数据（不需要继承Comparable接口），所以使用LinearProbingHashST实现
 */
class EfficientList<Item : Any> : List<Item> {

    companion object {
        private const val INITIAL_VALUE = 50000.0 // 第一个插入键的索引
        private const val OFFSET = 0.0001 // 当在最左侧或最右侧插入时的偏移量
    }

    private val bst = RedBlackBST<Double, Item>()
    private val hashST = LinearProbingHashST<Item, Double>()

    override fun addFront(item: Item) {
        if (contains(item)) {
            deleteByItem(item)
        }
        val index = if (isEmpty()) INITIAL_VALUE else bst.min() - OFFSET
        bst.put(index, item)
        hashST.put(item, index)
    }

    override fun addBack(item: Item) {
        if (contains(item)) {
            deleteByItem(item)
        }
        val index = if (isEmpty()) INITIAL_VALUE else bst.max() + OFFSET
        bst.put(index, item)
        hashST.put(item, index)
    }

    override fun deleteFront(): Item {
        if (isEmpty()) throw NoSuchElementException()
        val minIndex = bst.min()
        val minItem = bst.get(minIndex)!!
        bst.deleteMin()
        hashST.delete(minItem)
        return minItem
    }

    override fun deleteBack(): Item {
        if (isEmpty()) throw NoSuchElementException()
        val maxIndex = bst.max()
        val maxItem = bst.get(maxIndex)!!
        bst.deleteMax()
        hashST.delete(maxItem)
        return maxItem
    }

    override fun deleteByItem(item: Item) {
        val index = hashST.get(item) ?: throw NoSuchElementException()
        hashST.delete(item)
        bst.delete(index)
    }

    override fun add(i: Int, item: Item) {
        require(i in 0..size())
        if (contains(item)) {
            deleteByItem(item)
        }
        if (i == 0) {
            addFront(item)
            return
        }
        if (i == size()) {
            addBack(item)
            return
        }
        val preIndex = bst.select(i - 1)
        val nextIndex = bst.select(i)
        val index = (preIndex + nextIndex) / 2
        bst.put(index, item)
        hashST.put(item, index)
    }

    override fun deleteByIndex(i: Int) {
        require(i in 0 until size())
        val index = bst.select(i)
        val item = bst.get(index)!!
        bst.delete(index)
        hashST.delete(item)
    }

    override fun contains(item: Item): Boolean {
        return hashST.get(item) != null
    }

    override fun isEmpty(): Boolean {
        return bst.isEmpty() || hashST.isEmpty()
    }

    override fun size(): Int {
        return bst.size()
    }

    override fun iterator(): Iterator<Item> {
        return bst.values().iterator()
    }
}

/**
 * 遍历所有的值
 */
fun <K : Comparable<K>, V : Any> RedBlackBST<K, V>.values(): Iterable<V> {
    val queue = Queue<V>()
    addToQueue(root, queue)
    return queue
}

private fun <K : Comparable<K>, V : Any> addToQueue(node: RedBlackBST.Node<K, V>?, queue: Queue<V>) {
    if (node == null) return
    addToQueue(node.left, queue)
    queue.enqueue(node.value)
    addToQueue(node.right, queue)
}

private fun <T> checkIterator(iterator: Iterator<T>, array: Array<T>) {
    for (i in array.indices) {
        if (!iterator.hasNext() || iterator.next() != array[i]) {
            throw IllegalStateException()
        }
    }
    if (iterator.hasNext()) {
        throw IllegalStateException()
    }
}

fun main() {
    val list = EfficientList<Int>()
    list.addFront(0)
    list.addFront(1)
    list.addFront(2)
    list.addBack(3)
    list.addBack(4)
    list.addBack(5)
    check(list.contains(3))
    check(!list.isEmpty())
    check(list.size() == 6)
    checkIterator(list.iterator(), arrayOf(2, 1, 0, 3, 4, 5))

    list.deleteFront()
    list.deleteBack()
    check(!list.contains(2))
    checkIterator(list.iterator(), arrayOf(1, 0, 3, 4))

    list.deleteByIndex(0)
    checkIterator(list.iterator(), arrayOf(0, 3, 4))
    list.deleteByItem(3)
    checkIterator(list.iterator(), arrayOf(0, 4))
    list.add(1, 6)
    list.add(0, 7)
    checkIterator(list.iterator(), arrayOf(7, 0, 6, 4))

    println("EfficientList check succeed.")
}
