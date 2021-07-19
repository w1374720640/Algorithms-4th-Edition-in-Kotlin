package chapter6

import chapter3.section1.OrderedST
import chapter3.section1.testOrderedST
import edu.princeton.cs.algs4.In
import edu.princeton.cs.algs4.Queue
import edu.princeton.cs.algs4.RedBlackBST

/**
 * B+树符号表的实现（课本中说是B-树，实际上是B+树）
 */
@Suppress("UNCHECKED_CAST")
class BTreeST<K : Comparable<K>, V : Any>(private val sentinel: K, private val M: Int) : OrderedST<K, V> {

    init {
        require(M >= 4)
    }

    private var root = Page(true).apply {
        keys[0] = sentinel
        keyNum = 1
        size = 1
    }

    /**
     * B+树的页的API
     *
     * 只有外部页含有存储的值对象，内部页不含值对象
     */
    inner class Page(bottom: Boolean) {
        val keys = arrayOfNulls<Comparable<K>>(M)
        var pages = if (bottom) null else arrayOfNulls<Page>(M)
        var values = if (bottom) arrayOfNulls<Any>(M) else null
        var keyNum = 0
        var nextPage: Page? = null
        var size = 0

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
                        move(j - 1, j)
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
            resize()
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

        fun move(from: Int, to: Int) {
            keys[to] = keys[from]
            if (isExternal()) {
                values!![to] = values!![from]
            } else {
                pages!![to] = pages!![from]
            }
        }

        /**
         * 可能含有键key的子树
         */
        fun next(key: K): Pair<Page, Int> {
            require(!isExternal())
            require(key >= keys[0] as K)
            for (i in indices()) {
                if (key < keys[i]!! as K) {
                    return pages!![i - 1]!! to i - 1
                }
            }
            return pages!![keyNum - 1]!! to keyNum - 1
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
            page.nextPage = nextPage
            nextPage = page
            resize()
            page.resize()
            return page
        }

        fun resize() {
            if (isExternal()) {
                size = keyNum
            } else {
                size = 0
                for (i in indices()) {
                    size += pages!![i]!!.size
                }
            }
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
        root.resize()

    }

    private fun put(p: Page, key: K, value: V): Page? {
        if (p.isExternal()) {
            p.add(key, value)
            p.resize()
        } else {
            val next = p.next(key).first
            val newPage = put(next, key, value)
            if (newPage != null) {
                p.add(newPage)
            }
            p.resize()
        }
        return if (p.isFull()) p.split() else null
    }

    override fun get(key: K): V? {
        return get(root, key)
    }

    private fun get(p: Page, key: K): V? {
        if (p.isExternal()) {
            for (i in p.indices()) {
                if (p.keys[i] == key) return p.values!![i] as V?
            }
            return null
        } else {
            return get(p.next(key).first, key)
        }
    }

    /**
     * 删除一个键时，如果键不存在，抛出异常，如果键存在，找到外部页，将键删除，然后判断：
     * 1、如果当前页的大小在M/2到M-1之间，不做处理，直接结束
     * 2、如果当前页的大小小于M/2，且有兄弟结点大于M/2，则向兄弟结点借一个键
     * 3、如果当前页的大小小于M/2，且没有兄弟结点大于M/2，则将当前页和兄弟结点合并后重新插入父结点
     * 4、返回时递归检查每个父结点的大小是否满足条件
     * 5、最后检查根结点是否是内部结点且子页数量小于2，如果小于2，则将子页内容复制到根结点中
     */
    override fun delete(key: K) {
        delete(root, key)
        if (!root.isExternal() && root.keyNum < 2) {
            val p = root.pages!![0]!!
            root = Page(true)
            for (i in p.indices()) {
                root.keys[i] = p.keys[i]
                root.values!![i] = p.values!![i]
            }
            root.keyNum = p.keyNum
            root.resize()
        }
    }

    private fun delete(p: Page, key: K) {
        if (p.isExternal()) {
            for (i in p.indices()) {
                if (p.keys[i] as K == key) {
                    for (j in i until p.keyNum - 1) {
                        p.move(j + 1, j)
                    }
                    p.keyNum--
                    p.size--
                    return
                }
            }
            throw NoSuchElementException()
        } else {
            val next = p.next(key)
            val page = next.first
            val i = next.second
            delete(page, key)
            checkPageDelete(p, i)
            p.size--
        }
    }

    private fun checkPageDelete(parent: Page, i: Int) {
        require(!parent.isExternal() && i < parent.keyNum)
        val p = parent.pages!![i]!!
        if (p.keyNum >= M / 2) return
        when (i) {
            0 -> {
                val rightPage = parent.pages!![1]!!
                if (rightPage.keyNum > M / 2) {
                    getFromRight(parent, i, p, rightPage)
                } else {
                    mergeRight(parent, i, p, rightPage)
                }
            }
            parent.keyNum - 1 -> {
                val leftPage = parent.pages!![parent.keyNum - 2]!!
                if (leftPage.keyNum > M / 2) {
                    getFromLeft(parent, i, p, leftPage)
                } else {
                    mergeLeft(parent, i, p, leftPage)
                }
            }
            else -> {
                val leftPage = parent.pages!![i - 1]!!
                val rightPage = parent.pages!![i + 1]!!
                when {
                    leftPage.keyNum > M / 2 -> getFromLeft(parent, i, p, leftPage)
                    rightPage.keyNum > M / 2 -> getFromRight(parent, i, p, rightPage)
                    else -> mergeLeft(parent, i, p, leftPage)
                }
            }
        }
    }

    private fun getFromLeft(parent: Page, i: Int, p: Page, leftPage: Page) {
        copy(leftPage, leftPage.keyNum - 1, p, p.keyNum)
        p.keyNum++
        leftPage.keyNum--
        p.size++
        leftPage.size--
    }

    private fun getFromRight(parent: Page, i: Int, p: Page, rightPage: Page) {
        copy(rightPage, 0, p, p.keyNum)
        p.keyNum++
        p.size++
        parent.keys[i + 1] = rightPage.keys[1]
        for (j in 0 until rightPage.keyNum - 1) {
            rightPage.move(j + 1, j)
        }
        rightPage.keyNum--
        rightPage.size--
    }

    private fun mergeLeft(parent: Page, i: Int, p: Page, leftPage: Page) {
        for (j in p.indices()) {
            copy(p, j, leftPage, leftPage.keyNum + j)
        }
        leftPage.keyNum += p.keyNum
        leftPage.size += p.size
        leftPage.nextPage = p.nextPage
        for (j in i until parent.keyNum - 1) {
            parent.move(j + 1, j)
        }
        parent.keyNum--
    }

    private fun mergeRight(parent: Page, i: Int, p: Page, rightPage: Page) {
        for (j in rightPage.indices()) {
            copy(rightPage, j, p, p.keyNum + j)
        }
        p.keyNum += rightPage.keyNum
        p.size += rightPage.size
        p.nextPage = rightPage.nextPage
        for (j in i + 1 until parent.keyNum - 1) {
            parent.move(j + 1, j)
        }
        parent.keyNum--
    }

    private fun copy(fromPage: Page, fromIndex: Int, toPage: Page, toIndex: Int) {
        require((fromPage.isExternal() && toPage.isExternal()) || (!fromPage.isExternal() && !toPage.isExternal()))
        toPage.keys[toIndex] = fromPage.keys[fromIndex]
        if (fromPage.isExternal()) {
            toPage.values!![toIndex] = fromPage.values!![fromIndex]
        } else {
            toPage.pages!![toIndex] = fromPage.pages!![fromIndex]
        }
    }

    override fun contains(key: K): Boolean {
        var p = root
        while (true) {
            if (p.isExternal()) return p.contains(key)
            p = p.next(key).first
        }
    }

    override fun isEmpty(): Boolean {
        return size() == 0
    }

    override fun size(): Int {
        return root.size - 1
    }

    override fun keys(): Iterable<K> {
        val queue = Queue<K>()
        var p: Page? = root
        while (p != null && !p.isExternal()) {
            p = p.pages!![0]!!
        }
        while (p != null) {
            for (i in p.indices()) {
                queue.enqueue(p.keys[i] as K)
            }
            p = p.nextPage
        }
        // 把哨兵键排除在外
        queue.dequeue()
        return queue
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
        return rank(root, key) - 1
    }

    private fun rank(p: Page, key: K): Int {
        if (p.isExternal()) {
            for (i in p.indices()) {
                if (p.keys[i] as K >= key) return i
            }
            return p.size
        } else {
            val next = p.next(key)
            val nextPage = next.first
            val i = next.second
            var count = 0
            for (j in 0 until i) {
                count += p.pages!![j]!!.size
            }
            count += rank(nextPage, key)
            return count
        }
    }

    override fun select(k: Int): K {
        if (k > size()) throw NoSuchElementException()
        return select(root, k + 1)
    }

    private fun select(p: Page, k: Int): K {
        if (p.isExternal()) {
            if (k < p.keyNum) {
                return p.keys[k] as K
            } else throw NoSuchElementException()
        } else {
            var count = 0
            for (i in p.indices()) {
                count += p.pages!![i]!!.size
                if (count > k) {
                    return select(p.pages!![i]!!, k - (count - p.pages!![i]!!.size))
                }
            }
            throw NoSuchElementException()
        }
    }

    override fun deleteMin() {
        deleteMin(root)
        if (!root.isExternal() && root.keyNum < 2) {
            val p = root.pages!![0]!!
            root = Page(true)
            for (i in p.indices()) {
                root.keys[i] = p.keys[i]
                root.values!![i] = p.values!![i]
            }
            root.keyNum = p.keyNum
            root.resize()
        }
    }

    private fun deleteMin(p: Page) {
        if (p.isExternal()) {
            if (p.keyNum > 1) {
                for (j in 1 until p.keyNum - 1) {
                    p.move(j + 1, j)
                }
                p.keyNum--
                p.size--
            } else throw NoSuchElementException()
        } else {
            deleteMin(p.pages!![0]!!)
            checkPageDelete(p, 0)
            p.size--
        }
    }

    override fun deleteMax() {
        deleteMax(root)
        if (!root.isExternal() && root.keyNum < 2) {
            val p = root.pages!![0]!!
            root = Page(true)
            for (i in p.indices()) {
                root.keys[i] = p.keys[i]
                root.values!![i] = p.values!![i]
            }
            root.keyNum = p.keyNum
            root.resize()
        }
    }

    private fun deleteMax(p: Page) {
        if (p.isExternal()) {
            if (p.keyNum > 1) {
                p.keyNum--
                p.size--
            } else throw NoSuchElementException()
        } else {
            deleteMin(p.pages!![p.keyNum - 1]!!)
            checkPageDelete(p, p.keyNum - 1)
            p.size--
        }
    }

    override fun size(low: K, high: K): Int {
        if (low > high) return 0
        return if (contains(high)) {
            rank(high) - rank(low) + 1
        } else {
            rank(high) - rank(low)
        }
    }

    override fun keys(low: K, high: K): Iterable<K> {
        require(high >= low)
        val queue = Queue<K>()
        var p: Page? = root
        while (p != null && !p.isExternal()) {
            p = p.next(low).first
        }
        while (p != null) {
            for (i in p.indices()) {
                if ((p.keys[i] as K) in low..high) {
                    queue.enqueue(p.keys[i] as K)
                }
            }
            if ((p.keys[p.keyNum - 1] as K) >= high) break
            p = p.nextPage
        }
        if (queue.peek() == sentinel) {
            queue.dequeue()
        }
        return queue
    }
}

fun main() {
    val M = 4
    testOrderedST(BTreeST(Int.MIN_VALUE, M))

    // 使用经过充分测试的红黑树来检验B-树的实现是否正确
    // 进行相同的操作，比较两种数据结构的返回值是否相同
    val path = "./data/medTale.txt"
    val array = In(path).readAllStrings()
    // 使用单词的hash值作为键，单词作为值
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
    bst.keys().forEach {
        check(bst.get(it) == bTree.get(it))
    }

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