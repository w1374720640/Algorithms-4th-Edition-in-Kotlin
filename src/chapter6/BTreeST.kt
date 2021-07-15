package chapter6

import chapter3.section1.OrderedST
import edu.princeton.cs.algs4.In
import edu.princeton.cs.algs4.Queue
import edu.princeton.cs.algs4.RedBlackBST

/**
 * B-树符号表的实现
 *
 * 只有外部页含有存储的值对象，内部页不含值对象
 */
@Suppress("UNCHECKED_CAST")
class BTreeST<K : Comparable<K>, V : Any>(private val sentinel: K, private val M: Int) : OrderedST<K, V> {
    private var root = Page(true).apply {
        keys[0] = sentinel
        keyNum = 1
    }
    private var size = 0

    /**
     * B-树的页的API
     */
    inner class Page(bottom: Boolean) {
        val keys = arrayOfNulls<Comparable<K>>(M)
        var pages = if (bottom) null else arrayOfNulls<Page>(M)
        var values = if (bottom) arrayOfNulls<Any>(M) else null
        var keyNum = 0


        /**
         * 关闭页
         */
        fun close() {

        }

        /**
         * 将键插入（外部的）页中
         */
        fun add(key: K, value: V) {
            require(isExternal())
            for (i in indices()) {
                if (keys[i] == key) {
                    values!![i] = value
                    return
                } else if (keys[i] as K > key) {
                    for (j in keyNum downTo i + 1) {
                        keys[j] = keys[j - 1]
                        values!![j] = values!![j - 1]
                    }
                    keys[i] = key
                    values!![i] = value
                    keyNum++
                    // 插入一个新键时需要将总数加一
                    size++
                    return
                }
            }
            keys[keyNum] = key
            values!![keyNum] = value
            keyNum++
            size++
        }

        /**
         * 打开p，向这个（内部）页中插入一个条目并将p和p中的最小键相关联
         */
        fun add(p: Page) {
            require(!isExternal())
            val minKey = p.keys[0] as K
            for (i in indices()) {
                if (keys[i] == minKey) {
                    pages!![i] = p
                    return
                } else if (keys[i] as K > minKey) {
                    for (j in keyNum downTo i + 1) {
                        keys[j] = keys[j - 1]
                        pages!![j] = pages!![j - 1]
                    }
                    keys[i] = minKey
                    pages!![i] = p
                    keyNum++ // 插入page时不需要将总数增加
                    return
                }
            }
            keys[keyNum] = minKey
            pages!![keyNum] = p
            keyNum++
        }

        /**
         * 这是一个外部页吗
         */
        fun isExternal(): Boolean {
            return pages == null
        }

        fun indices(): IntRange {
            return 0 until keyNum
        }

        fun contains(key: K): Boolean {
            if (!isExternal()) return false
            for (i in indices()) {
                if (key == keys[i]) return true
            }
            return false
        }

        /**
         * 可能含有键key的子树
         */
        fun next(key: K): Page? {
            if (key < keys[0] as K) return null
            for (i in indices()) {
                if (key < keys[i]!! as K) {
                    return pages?.get(i - 1)
                }
            }
            return pages?.get(keyNum - 1)
        }

        /**
         * 页是否已经溢出
         */
        fun isFull(): Boolean {
            return keyNum == M
        }

        /**
         * 将较大的中间键移动到一个新页中
         */
        fun split(): Page {
            require(keyNum == M)
            val page = Page(isExternal())
            for (i in M / 2 until M) {
                page.keys[i - M / 2] = keys[i]
                keys[i] = null
                if (isExternal()) {
                    page.values!![i - M / 2] = values!![i]
                    values!![i] = null
                } else {
                    page.pages!![i - M / 2] = pages!![i]
                    pages!![i] = null
                }
            }
            keyNum = M / 2
            page.keyNum = M - M / 2
            return page
        }
    }

    override fun put(key: K, value: V) {
        require(key > sentinel)
        val newPage = put(root, key, value)
        if (newPage != null) {
            val oldPage = root
            root = Page(false).apply {
                keys[0] = sentinel
                keyNum = 1
            }
            root.add(oldPage)
            root.add(newPage)
        }

    }

    private fun put(p: Page, key: K, value: V): Page? {
        if (p.isExternal()) {
            p.add(key, value)
        } else {
            val next = p.next(key)
            if (next != null) {
                val newPage = put(next, key, value)
                if (newPage != null) {
                    p.add(newPage)
                }
            }
        }
        return if (p.isFull()) p.split() else null
    }

    override fun get(key: K): V? {
        return get(root, key)
    }

    private fun get(p: Page?, key: K): V? {
        if (p == null) return null
        if (p.isExternal()) {
            for (i in p.indices()) {
                if (p.keys[i] == key) return p.values!![i] as V?
            }
            return null
        } else {
            return get(p.next(key), key)
        }
    }

    override fun delete(key: K) {
        TODO("Not yet implemented")
    }

    override fun contains(key: K): Boolean {
        var p: Page? = root
        while (p != null) {
            if (p.contains(key)) return true
            p = p.next(key)
        }
        return false
    }

    private fun contains(p: Page, key: K): Boolean {
        if (key < p.keys[0]!! as K) return false
        if (p.isExternal()) {
            for (i in 0 until M) {
                if (p.keys[i] == key) return true
            }
            return false
        } else {
            for (i in 0 until M) {
                val k = p.keys[i]!!
                if (k == key) return true
                else if (k > key) return contains(p.pages!![i - 1]!!, key)
            }
            return contains(p.pages!![M - 1]!!, key)
        }
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun size(): Int {
        return size
    }

    override fun keys(): Iterable<K> {
        val queue = Queue<K>()
        keys(queue, root)
        // 把哨兵键排除在外
        queue.dequeue()
        return queue
    }

    private fun keys(queue: Queue<K>, p: Page) {
        if (p.isExternal()) {
            for (i in p.indices()) {
                queue.enqueue(p.keys[i] as K)
            }
        } else {
            for (i in p.indices()) {
                keys(queue, p.pages!![i]!!)
            }
        }
    }

    override fun min(): K {
        if (isEmpty()) throw NoSuchElementException()
        return min(root)
    }

    private fun min(p: Page): K {
        if (p.isExternal()) {
            return (if (p.keys[0] == sentinel) p.keys[1] else p.keys[0]) as K
        } else {
            return min(p.pages!![0]!!)
        }
    }

    override fun max(): K {
        if (isEmpty()) throw NoSuchElementException()
        return max(root)
    }

    private fun max(p: Page): K {
        if (p.isExternal()) {
            return p.keys[p.keyNum - 1] as K
        } else {
            return max(p.pages!![p.keyNum - 1]!!)
        }
    }

    override fun floor(key: K): K {
        require(key > sentinel)
        val k = floor(root, key)
        if (k == sentinel) throw NoSuchElementException()
        return k
    }

    private fun floor(p: Page, key: K): K {
        for (i in p.indices()) {
            if (p.keys[i] == key) return key
            if (p.keys[i] as K > key) {
                return if (p.isExternal()) {
                    p.keys[i - 1] as K
                } else {
                    floor(p.pages!![i - 1]!!, key)
                }
            }
        }
        return if (p.isExternal()) {
            p.keys[p.keyNum - 1] as K
        } else {
            floor(p.pages!![p.keyNum - 1]!!, key)
        }
    }

    override fun ceiling(key: K): K {
        if (key <= sentinel) return min()
        return ceiling(root, key) ?: throw NoSuchElementException()
    }

    private fun ceiling(p: Page, key: K): K? {
        for (i in p.indices()) {
            if (p.keys[i] == key) return key
            if (p.keys[i] as K > key) {
                return if (p.isExternal()) {
                    p.keys[i] as K
                } else {
                    ceiling(p.pages!![i - 1]!!, key) ?: p.keys[i] as K
                }
            }
        }
        return if (p.isExternal()) {
            null
        } else {
            ceiling(p.pages!![p.keyNum - 1]!!, key)
        }
    }

    override fun rank(key: K): Int {
        TODO("Not yet implemented")
    }

    override fun select(k: Int): K {
        TODO("Not yet implemented")
    }

    override fun deleteMin() {
        TODO("Not yet implemented")
    }

    override fun deleteMax() {
        TODO("Not yet implemented")
    }

    override fun size(low: K, high: K): Int {
        TODO("Not yet implemented")
    }

    override fun keys(low: K, high: K): Iterable<K> {
        require(high >= low)
        val queue = Queue<K>()
        keys(queue, root, low, high)
        if (queue.peek() == sentinel) {
            queue.dequeue()
        }
        return queue
    }

    private fun keys(queue: Queue<K>, p: Page, low: K, high: K) {
        if (p.isExternal()) {
            for (i in p.indices()) {
                val k = p.keys[i] as K
                if (k in low..high) {
                    queue.enqueue(k)
                }
            }
        } else {
            var i = 0
            while (i < p.keyNum && (p.keys[i] as K) < low) {
                i++
            }
            if ((i == p.keyNum || p.keys[i] as K > low) && i > 0) {
                keys(queue, p.pages!![i - 1]!!, low, high)
            }
            while (i < p.keyNum && (p.keys[i] as K) <= high) {
                keys(queue, p.pages!![i++]!!, low, high)
            }
        }
    }
}

fun main() {
    val path = "./data/medTale.txt"
    val M = 10
    val array = In(path).readAllStrings()
    val bst = RedBlackBST<Int, String>()
    val bTree = BTreeST<Int, String>(Int.MIN_VALUE, M)
    array.forEach {
        val hash = it.hashCode()
        bst.put(hash, it)
        bTree.put(hash, it)
    }

    check(bst.size() == bTree.size())
    check(bst.contains("wisdom".hashCode()) == bTree.contains("wisdom".hashCode()))
    check(bst.contains("abcde".hashCode()) == bTree.contains("abcde".hashCode()))
    check(bst.get("clearer".hashCode()) == bTree.get("clearer".hashCode()))

    val bstIterator = bst.keys().iterator()
    val bTreeIterator = bTree.keys().iterator()
    while (bstIterator.hasNext()) {
        check(bTreeIterator.hasNext() && bstIterator.next() == bTreeIterator.next())
    }
    check(!bTreeIterator.hasNext())

    check(bst.min() == bTree.min())
    check(bst.max() == bTree.max())
    check(bst.floor(12345) == bTree.floor(12345))
    check(bst.floor("france".hashCode()) == bTree.floor("france".hashCode()))
    check(bst.ceiling(444) == bTree.ceiling(444))
    check(bst.ceiling("guards".hashCode()) == bTree.ceiling("guards".hashCode()))
//    check(bst.size(123, 8888888) == bTree.size(123, 8888888))

    val low = -555555
    val high = 999999
    val bstIterator2 = bst.keys(low, high).iterator()
    val bTreeIterator2 = bTree.keys(low, high).iterator()
    while (bstIterator2.hasNext()) {
        check(bTreeIterator2.hasNext() && bstIterator2.next() == bTreeIterator2.next())
    }
    check(!bTreeIterator2.hasNext())

    println("check succeed.")
}