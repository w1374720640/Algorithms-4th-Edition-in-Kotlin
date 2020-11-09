package chapter3.section2

import chapter3.section1.OrderedST
import chapter3.section1.testOrderedST
import edu.princeton.cs.algs4.Stack
import edu.princeton.cs.algs4.StdRandom
import extensions.spendTimeMillis

/**
 * 数组的表示
 * 开发一个二叉查找树的实现，用三个数组表示一颗树（预先分配为构造函数中指定的最大长度）：
 * 一个数组用来保存键，一个数组用来保存左链接的索引，一个数组用来保存右链接的索引
 * 比较你的程序和标准实现的性能
 *
 * 解：用三个数组无法保存键对应的值，另加一个数组保存键对应的值，顺序和键数组顺序相同，下面的过程省略值数组
 * 假设二叉查找树的结构如下所示
 *                      A
 *       B                   C
 *   D          E
 *          F       G
 *
 * 键数组    ： A  B  C  D  E  F  G
 * 左链接数组： 1  3  -1 -1 5  -1 -1
 * 右链接数组： 2  4  -1 -1 6  -1 -1
 * 二叉查找树的根节点为键数组的第一个值A，左链接数组和右链接数组的内容表示相应位置键的左右子结点索引，-1表示无相应子结点
 *
 * 查找G结点的过程：先和根结点A比较，小于A，找到A的左子结点，索引为1的结点B，和B比较，大于B
 *     找到B的右子结点，索引为4的结点E，和E比较，大于E，找到E的右子结点，索引为6的结点G，和G相等，更新G结点
 *
 * 给结点C添加一个右子结点H的过程：在键数组末尾添加键H，和根节点A比较，大于结点A，找到A的右子结点，索引为2的结点C
 *     和结点C比较，大于结点C，C的右子结点为空，将C位置对应的右链接数组赋值为H的索引，此时三个数组内容如下
 * 键数组    ： A  B  C  D  E  F  G  H
 * 左链接数组： 1  3  -1 -1 5  -1 -1 -1
 * 右链接数组： 2  4  7  -1 6  -1 -1 -1
 *
 * 删除最小结点D的过程：找到D的父结点B，键数组最后一个键H的父结点C，将键数组中的D位置赋值为H，H位置置空
 *     C位置的右链接索引设置为3，B位置的左链接索引设置为-1，此时三个数组内容如下
 * 键数组    ： A  B  C  H  E  F  G
 * 左链接数组： 1  -1 -1 -1 5  -1 -1
 * 右链接数组： 2  4  3  -1 6  -1 -1
 *
 * 删除结点B的过程：因为结点B的右子结点不为空，所以需要先找到B右子树中的最小结点F
 *     调用删除最小结点的方法删除结点F，然后用键F替换键数组中的B，这个过程不需要修改左右链接数组，两个过程如下
 *     删除结点F                                         用键F替换键B
 * 键数组    ： A  B  C  H  E  G                    键数组    ： A  F  C  H  E  G
 * 左链接数组： 1  -1 -1 -1 -1 -1        ->         左链接数组： 1  -1 -1 -1 -1 -1
 * 右链接数组： 2  4  3  -1 5  -1                   右链接数组： 2  4  3  -1 5  -1
 * 此时二叉查找树的结构如下
 *                    A
 *       F                 C
 *            E              H
 *                G
 */
class ArrayBinarySearchTree<K : Comparable<K>, V : Any>(private val maxSize: Int) : OrderedST<K, V> {
    private val keys = arrayOfNulls<Comparable<K>>(maxSize)
    private val values = arrayOfNulls<Any>(maxSize)
    private val leftIndexList = IntArray(maxSize) { -1 }
    private val rightIndexList = IntArray(maxSize) { -1 }
    private var size = 0

    init {
        require(maxSize > 0)
    }

    override fun min(): K {
        if (isEmpty()) throw NoSuchElementException()
        return keys[minIndex(0)].transformKey()
    }

    private fun minIndex(index: Int): Int {
        val leftIndex = leftIndexList[index]
        return if (leftIndex == -1) {
            index
        } else {
            minIndex(leftIndex)
        }
    }

    override fun max(): K {
        if (isEmpty()) throw NoSuchElementException()
        return keys[maxIndex(0)].transformKey()
    }

    private fun maxIndex(index: Int): Int {
        val rightIndex = rightIndexList[index]
        return if (rightIndex == -1) {
            index
        } else {
            maxIndex(rightIndex)
        }
    }

    override fun floor(key: K): K {
        if (isEmpty()) throw NoSuchElementException()
        return floor(0, key) ?: throw NoSuchElementException()
    }

    private fun floor(index: Int, key: K): K? {
        val compareKey = keys[index].transformKey()
        return when {
            compareKey < key -> {
                val rightIndex = rightIndexList[index]
                if (rightIndex == -1) {
                    compareKey
                } else {
                    floor(rightIndex, key) ?: compareKey
                }
            }
            compareKey > key -> {
                val leftIndex = leftIndexList[index]
                if (leftIndex == -1) {
                    null
                } else {
                    floor(leftIndex, key)
                }
            }
            else -> compareKey
        }
    }

    override fun ceiling(key: K): K {
        if (isEmpty()) throw NoSuchElementException()
        return ceiling(0, key) ?: throw NoSuchElementException()
    }

    private fun ceiling(index: Int, key: K): K? {
        val compareKey = keys[index].transformKey()
        return when {
            compareKey > key -> {
                val leftIndex = leftIndexList[index]
                if (leftIndex == -1) {
                    compareKey
                } else {
                    ceiling(leftIndex, key) ?: compareKey
                }
            }
            compareKey < key -> {
                val rightIndex = rightIndexList[index]
                if (rightIndex == -1) {
                    null
                } else {
                    ceiling(rightIndex, key)
                }
            }
            else -> compareKey
        }
    }

    override fun rank(key: K): Int {
        return if (isEmpty()) 0 else rank(0, key)
    }

    //某个根节点及其所有子结点的数量
    private fun sizeOf(index: Int): Int {
        val leftIndex = leftIndexList[index]
        val leftSize = if (leftIndex == -1) 0 else sizeOf(leftIndex)
        val rightIndex = rightIndexList[index]
        val rightSize = if (rightIndex == -1) 0 else sizeOf(rightIndex)
        return leftSize + rightSize + 1
    }

    private fun rank(index: Int, key: K): Int {
        val compareKey = keys[index].transformKey()
        return when {
            compareKey < key -> {
                val leftIndex = leftIndexList[index]
                val rightIndex = rightIndexList[index]
                1 + (if (leftIndex == -1) 0 else sizeOf(leftIndex)) + (if (rightIndex == -1) 0 else rank(rightIndex, key))
            }
            compareKey > key -> {
                val leftIndex = leftIndexList[index]
                if (leftIndex == -1) 0 else rank(leftIndex, key)
            }
            else -> {
                val leftIndex = leftIndexList[index]
                if (leftIndex == -1) 0 else sizeOf(leftIndex)
            }
        }
    }

    //非递归中序遍历二叉查找树
    override fun select(k: Int): K {
        if (isEmpty()) throw NoSuchElementException()
        var count = 0
        val stack = Stack<Int>()
        addAllLeftNode(0, stack)
        while (!stack.isEmpty) {
            val index = stack.pop()
            if (count == k) return keys[index].transformKey()
            count++
            val rightIndex = rightIndexList[index]
            if (rightIndex != -1) {
                addAllLeftNode(rightIndex, stack)
            }
        }
        throw NoSuchElementException()
    }

    private fun addAllLeftNode(rootIndex: Int, stack: Stack<Int>) {
        var index = rootIndex
        while (index != -1) {
            stack.push(index)
            index = leftIndexList[index]
        }
    }

    override fun deleteMin() {
        if (isEmpty()) throw NoSuchElementException()
        var index = minIndex(0)
        val parentInfo = getParentInfo(keys[index].transformKey())
        val rightIndex = rightIndexList[index]
        val lastIndex = size - 1
        val lastKey = keys[lastIndex].transformKey()
        val lastParentInfo = getParentInfo(lastKey)

        if (rightIndex == -1) {
            //如果最小结点右侧为空，直接将父结点的左子结点置空
            if (parentInfo.index != -1) {
                leftIndexList[parentInfo.index] = -1
            }
        } else {
            //如果最小结点右侧非空，将右子结点信息复制到该结点位置
            copy(rightIndex, index)
            if (lastParentInfo.index == rightIndex) {
                lastParentInfo.index = index
            }
            index = rightIndex
        }

        //将键数组中的最后一条记录复制到需要删除的结点位置，可能是原最小结点，也可能是原最小结点的右结点
        if (index != lastIndex) {
            copy(lastIndex, index)
            //将键数组中最后一条记录的父结点的左子结点或右子结点重新指向
            if (lastParentInfo.index != -1) {
                if (lastParentInfo.isLeft) {
                    leftIndexList[lastParentInfo.index] = index
                } else {
                    rightIndexList[lastParentInfo.index] = index
                }
            }
        }

        //将最后一个位置置空，避免移动所有元素
        reset(lastIndex)
        size--
    }

    //将一个索引位置的数据复制到另一个索引对应的位置中
    private fun copy(fromIndex: Int, toIndex: Int) {
        keys[toIndex] = keys[fromIndex]
        values[toIndex] = values[fromIndex]
        leftIndexList[toIndex] = leftIndexList[fromIndex]
        rightIndexList[toIndex] = rightIndexList[fromIndex]
    }

    //重置一个位置的数据
    private fun reset(index: Int) {
        keys[index] = null
        values[index] = null
        leftIndexList[index] = -1
        rightIndexList[index] = -1
    }

    private class ParentInfo(var index: Int, var isLeft: Boolean)

    /**
     * 获取一个键的父结点索引，以及该结点是否是左子结点
     */
    private fun getParentInfo(key: K): ParentInfo {
        if (isEmpty()) return ParentInfo(-1, false)
        var parentIndex = -1
        var index = 0
        var isLeft = true
        while (index != -1) {
            val compareKey = keys[index].transformKey()
            when {
                compareKey > key -> {
                    parentIndex = index
                    index = leftIndexList[index]
                    isLeft = true
                }
                compareKey < key -> {
                    parentIndex = index
                    index = rightIndexList[index]
                    isLeft = false
                }
                else -> return ParentInfo(parentIndex, isLeft)
            }
        }
        throw IllegalStateException()
    }

    override fun deleteMax() {
        if (isEmpty()) throw NoSuchElementException()
        var index = maxIndex(0)
        val parentInfo = getParentInfo(keys[index].transformKey())
        val leftIndex = leftIndexList[index]
        val lastIndex = size - 1
        val lastKey = keys[lastIndex].transformKey()
        val lastParentInfo = getParentInfo(lastKey)

        if (leftIndex == -1) {
            //如果最小结点左侧为空，直接将父结点的右子结点置空
            if (parentInfo.index != -1) {
                rightIndexList[parentInfo.index] = -1
            }
        } else {
            //如果最小结点左侧非空，将左子结点信息复制到该结点位置
            copy(leftIndex, index)
            if (lastParentInfo.index == leftIndex) {
                lastParentInfo.index = index
            }
            index = leftIndex
        }

        //将键数组中的最后一条记录复制到需要删除的结点位置，可能是原最大结点，也可能是原最大结点的左结点
        if (index != lastIndex) {
            copy(lastIndex, index)
            //将键数组中最后一条记录的父结点的左子结点或右子结点重新指向
            if (lastParentInfo.index != -1) {
                if (lastParentInfo.isLeft) {
                    leftIndexList[lastParentInfo.index] = index
                } else {
                    rightIndexList[lastParentInfo.index] = index
                }
            }
        }

        //将最后一个位置置空，避免移动所有元素
        reset(lastIndex)
        size--
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
        if (isEmpty()) {
            keys[0] = key
            values[0] = value
            size++
        } else {
            put(0, key, value)
        }
    }

    private fun put(index: Int, key: K, value: V) {
        val compareKey = keys[index].transformKey()
        when {
            compareKey > key -> {
                val leftIndex = leftIndexList[index]
                if (leftIndex == -1) {
                    require(size < maxSize)
                    keys[size] = key
                    values[size] = value
                    leftIndexList[index] = size
                    size++
                } else {
                    put(leftIndex, key, value)
                }
            }
            compareKey < key -> {
                val rightIndex = rightIndexList[index]
                if (rightIndex == -1) {
                    require(size < maxSize)
                    keys[size] = key
                    values[size] = value
                    rightIndexList[index] = size
                    size++
                } else {
                    put(rightIndex, key, value)
                }
            }
            else -> values[index] = value
        }
    }

    override fun get(key: K): V? {
        if (isEmpty()) return null
        val index = getIndex(0, key)
        return if (index == -1) null else values[index].transformValue()
    }

    private fun Comparable<K>?.transformKey(): K {
        @Suppress("UNCHECKED_CAST")
        return if (this == null) {
            throw IllegalStateException()
        } else {
            this as K
        }
    }

    private fun Any?.transformValue(): V? {
        @Suppress("UNCHECKED_CAST")
        return this as V?
    }

    private fun getIndex(index: Int, key: K): Int {
        require(index in 0 until size)
        val compareKey = keys[index].transformKey()
        when {
            compareKey > key -> {
                val leftIndex = leftIndexList[index]
                return if (leftIndex == -1) {
                    -1
                } else {
                    getIndex(leftIndex, key)
                }
            }
            compareKey < key -> {
                val rightIndex = rightIndexList[index]
                return if (rightIndex == -1) {
                    -1
                } else {
                    getIndex(rightIndex, key)
                }
            }
            else -> return index
        }
    }

    override fun delete(key: K) {
        if (isEmpty()) throw NoSuchElementException()
        var index = getIndex(0, key)
        if (index == -1) throw NoSuchElementException()
        val parentInfo = getParentInfo(key)
        val lastIndex = size - 1
        val lastKey = keys[lastIndex].transformKey()
        val lastParentInfo = getParentInfo(lastKey)

        val rightIndex = rightIndexList[index]
        if (rightIndex == -1) {
            val leftIndex = leftIndexList[index]
            if (leftIndex == -1) {
                if (parentInfo.index != -1) {
                    //需要删除的结点没有子结点，需要将该结点的父结点对应的子结点删除
                    if (parentInfo.isLeft) {
                        leftIndexList[parentInfo.index] = -1
                    } else {
                        rightIndexList[parentInfo.index] = -1
                    }
                }
            } else {
                copy(leftIndex, index)
                if (lastParentInfo.index == leftIndex) {
                    lastParentInfo.index = index
                }
                index = leftIndex
            }
        } else {
            val minIndex = minIndex(rightIndex)
            val minParentInfo = getParentInfo(keys[minIndex].transformKey())
            val minRightIndex = rightIndexList[minIndex]

            keys[index] = keys[minIndex]
            values[index] = values[minIndex]
            if (lastParentInfo.index == minIndex) {
                lastParentInfo.index = index
            }
            index = minIndex
            if (minRightIndex == -1) {
                if (minParentInfo.index != -1) {
                    //和全局最小值不同，minIndex可能为右子结点
                    if (minParentInfo.isLeft) {
                        leftIndexList[minParentInfo.index] = -1
                    } else {
                        rightIndexList[minParentInfo.index] = -1
                    }
                }
            } else {
                copy(minRightIndex, minIndex)
                if (lastParentInfo.index == minRightIndex) {
                    lastParentInfo.index = minIndex
                }
                index = minRightIndex
            }
        }

        //将键数组中的最后一条记录复制到需要删除的结点位置，可能是原最小结点，也可能是原最小结点的右结点
        if (index != lastIndex) {
            copy(lastIndex, index)
            //将键数组中最后一条记录的父结点的左子结点或右子结点重新指向
            if (lastParentInfo.index != -1) {
                if (lastParentInfo.isLeft) {
                    leftIndexList[lastParentInfo.index] = index
                } else {
                    rightIndexList[lastParentInfo.index] = index
                }
            }
        }

        //将最后一个位置置空，避免移动所有元素
        reset(lastIndex)
        size--
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun size(): Int {
        return size
    }

    override fun keys(low: K, high: K): Iterable<K> {
        return object : Iterable<K> {
            override fun iterator(): Iterator<K> {
                return object : Iterator<K> {
                    val stack = Stack<Int>()

                    init {
                        if (!isEmpty()) {
                            addLeftNodeInRange(0, stack)
                        }
                    }

                    private fun addLeftNodeInRange(rootIndex: Int, stack: Stack<Int>) {
                        val compareKey = keys[rootIndex].transformKey()
                        when {
                            compareKey < low -> {
                                var index = rightIndexList[rootIndex]
                                while (index != -1) {
                                    val leftKey = keys[index].transformKey()
                                    when {
                                        leftKey < low -> index = rightIndexList[index]
                                        leftKey == low -> {
                                            stack.push(index)
                                            index = -1
                                        }
                                        else -> {
                                            if (leftKey in low..high) {
                                                stack.push(index)
                                            }
                                            index = leftIndexList[index]
                                        }
                                    }
                                }
                            }
                            compareKey == low -> stack.push(rootIndex)
                            else -> {
                                var index = rootIndex
                                while (index != -1) {
                                    val leftKey = keys[index].transformKey()
                                    if (leftKey in low..high) {
                                        stack.push(index)
                                    }
                                    index = leftIndexList[index]
                                }
                            }
                        }
                    }

                    override fun hasNext(): Boolean {
                        return !stack.isEmpty
                    }

                    override fun next(): K {
                        if (stack.isEmpty) throw NoSuchElementException()
                        val index = stack.pop()
                        val rightIndex = rightIndexList[index]
                        if (rightIndex != -1) {
                            addLeftNodeInRange(rightIndex, stack)
                        }
                        return keys[index].transformKey()
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
                    val stack = Stack<Int>()

                    init {
                        if (!isEmpty()) {
                            addAllLeftNode(0, stack)
                        }
                    }

                    override fun hasNext(): Boolean {
                        return !stack.isEmpty
                    }

                    override fun next(): K {
                        if (stack.isEmpty) throw NoSuchElementException()
                        val index = stack.pop()
                        val rightIndex = rightIndexList[index]
                        if (rightIndex != -1) {
                            addAllLeftNode(rightIndex, stack)
                        }
                        return keys[index].transformKey()
                    }
                }
            }

        }
    }
}

fun main() {
    testOrderedST(ArrayBinarySearchTree(10))

    var size = 10000
    repeat(8) {
        val array = IntArray(size) { it }
        StdRandom.shuffle(array)
        val stdST = BinarySearchTree<Int, Int>()
        val arrayST = ArrayBinarySearchTree<Int, Int>(size)

        val stdPutTime = spendTimeMillis {
            array.forEach {
                stdST.put(it, 0)
            }
        }
        val arrayPutTime = spendTimeMillis {
            array.forEach {
                arrayST.put(it, 0)
            }
        }
        check(stdST.size() == size && arrayST.size() == size)

        val stdDeleteMinAndMaxTime = spendTimeMillis {
            repeat(size) {
                stdST.deleteMin()
            }
            while (!stdST.isEmpty()) {
                stdST.deleteMax()
            }
        }
        val arrayDeleteMinAndMaxTime = spendTimeMillis {
            repeat(size) {
                arrayST.deleteMin()
            }
            while (!stdST.isEmpty()) {
                arrayST.deleteMax()
            }
        }

        //重新添加，供delete方法删除
        array.forEach {
            stdST.put(it, 0)
            arrayST.put(it, 0)
        }
        StdRandom.shuffle(array)
        val stdGetTime = spendTimeMillis {
            array.forEach {
                stdST.get(it)
            }
        }
        val arrayGetTime = spendTimeMillis {
            array.forEach {
                arrayST.get(it)
            }
        }
        val stdDeleteTime = spendTimeMillis {
            array.forEach {
                stdST.delete(it)
            }
        }
        val arrayDeleteTime = spendTimeMillis {
            array.forEach {
                arrayST.delete(it)
            }
        }
        check(stdST.isEmpty() && arrayST.isEmpty())

        println("size=$size")
        println("std  : put:$stdPutTime ms  get:$stdGetTime ms  deleteMinAndMax:$stdDeleteMinAndMaxTime ms  delete:$stdDeleteTime ms")
        println("array: put:$arrayPutTime ms  get:$arrayGetTime ms  deleteMinAndMax:$arrayDeleteMinAndMaxTime ms  delete:$arrayDeleteTime ms")

        size *= 2
    }
}