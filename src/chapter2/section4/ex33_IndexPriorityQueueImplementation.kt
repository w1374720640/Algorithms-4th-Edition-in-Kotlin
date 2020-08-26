package chapter2.section4

import extensions.readInt
import extensions.readString
import extensions.safeCall

/**
 * 索引优先队列的实现
 * 按照2.4.4.6节的描述修改算法2.6来实现索引优先队列API中的基本操作：
 * 使用pq[]保存索引，添加一个数组keys[]来保存元素，
 * 再添加一个数组qp[]来保存pq[]的逆序————qp[i]的值是i在pq[]中的位置（即索引j，pq[j]=i）。
 * 修改算法2.6的代码来维护这些数据结构。
 * 若i不在队列中，则总是令qp[i]=-1并添加一个方法contains()来检测这种情况。
 * 你需要修改辅助函数exch()和less()，但不需要修改sink()和swim()
 *
 * 解：keys[]中保存元素的实际值，元素的位置和索引相同
 * pq[]保存keys[]中元素的索引，用于构造小顶堆，索引从1开始，组成完全二叉树
 * qp[]中保存元素的索引在pq[]中的位置，元素的位置和索引相同
 * 举例：数组最大长度为6，依次插入(1,'b') (3,'a') (5,'c)，三个数组的最终状态为
 * keys: {_, 'b', _, 'a', _, 'c'} 长度为6 (null及-1都用 _ 代替)
 * pq: {_, 3, 1, 5, _, _, _} 长度为7，第一位未使用，其余值连续，索引3对应的值'a'最小，所以排在堆顶
 * qp: {_, 2, _, 1, _, 3} 长度为6，索引1在堆中排在第二位，索引3在堆中排第一位，索引5在堆中排第三位
 * 插入(i,key)时，先将keys[i]赋值为key，再判断索引i原先是否有值，
 * 没有值则size++，pq[size]=i, qp[i]=size 判断是否需要上浮
 * 有值则size不变，判断是否需要上浮或下沉
 * 判断大小时，以pq的值为索引从keys中取值判断
 * 交换i,j时，keys数组不变，交换pq[i]和pq[j]，同时交换qp[pq[i]]和qp[pq[j]]
 */
class HeapIndexMinPriorityQueue<T : Comparable<T>>(private val maxSize: Int) : IndexMinPriorityQueue<T> {
    private val keys = arrayOfNulls<Comparable<T>>(maxSize)
    private var pq = IntArray(maxSize + 1) { -1 }
    private var qp = IntArray(maxSize) { -1 }
    private var size = 0

    override fun get(k: Int): T? {
        checkInRange(k)
        @Suppress("UNCHECKED_CAST")
        return keys[k] as T?
    }

    override fun set(k: Int, item: T) {
        checkInRange(k)
        if (contains(k)) {
            keys[k] = item
            swimOrSink(qp[k])
        } else {
            keys[k] = item
            size++
            pq[size] = k
            qp[k] = size
            swim(size)
        }
    }

    override fun contains(k: Int): Boolean {
        return qp[k] != -1
    }

    override fun delete(k: Int) {
        checkInRange(k)
        if (!contains(k)) {
            throw NoSuchElementException("Priority queue does not include index $k")
        }
        keys[k] = null
        val index = qp[k]
        if (index == size) {
            pq[index] = -1
            qp[k] = -1
            size--
        } else {
            swap(index, size)
            pq[size] = -1
            qp[k] = -1
            size--
            swimOrSink(index)
        }
    }

    override fun min(): T {
        if (isEmpty()) {
            throw NoSuchElementException("Priority queue is empty!")
        }
        @Suppress("UNCHECKED_CAST")
        return keys[pq[1]] as T
    }

    override fun minIndex(): Int {
        if (isEmpty()) {
            throw NoSuchElementException("Priority queue is empty!")
        }
        return pq[1]
    }

    override fun delMin(): Int {
        if (isEmpty()) {
            throw NoSuchElementException("Priority queue is empty!")
        }
        val minIndex = pq[1]
        swap(1, size)
        keys[minIndex] = null
        qp[minIndex] = -1
        pq[size] = -1
        size--
        sink(1)
        return minIndex
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun size(): Int {
        return size
    }

    override fun toString(): String {
        if (isEmpty()) {
            return "Empty priority queue!"
        } else {
            var i = 1
            val builder = StringBuilder()
            while (i <= size) {
                if (i == 1) {
                    builder.append("[")
                }
                builder.append("(")
                        .append(pq[i])
                        .append(",")
                        .append(keys[pq[i]])
                        .append(")")
                if (i == size) {
                    builder.append("]")
                } else {
                    builder.append(",")
                }
                i++
            }
            return builder.toString()
        }
    }

    override fun iterator(): Iterator<T> {
        return object : Iterator<T> {
            private var i = 0

            override fun hasNext(): Boolean {
                return i < size
            }

            override fun next(): T {
                @Suppress("UNCHECKED_CAST")
                return keys[pq[++i]] as T
            }
        }
    }

    /**
     * 根据pq[]中的索引值比较元素大小
     */
    private fun less(i: Int, j: Int): Boolean {
        if (i == j) return false
        @Suppress("UNCHECKED_CAST")
        return keys[pq[i]]!! < keys[pq[j]]!! as T
    }

    /**
     * 交换pq[]中的值
     */
    private fun swap(i: Int, j: Int) {
        if (i == j) return
        val pqi = pq[i]
        val pqj = pq[j]
        pq[i] = pqj
        pq[j] = pqi

        val temp = qp[pqi]
        qp[pqi] = qp[pqj]
        qp[pqj] = temp
    }

    /**
     * 值直接发生变化，不确定要上浮还是下沉
     */
    private fun swimOrSink(i: Int) {
        when {
            i == 1 -> sink(i)
            i * 2 > size -> swim(i)
            less(i, i / 2) -> swim(i)
            else -> sink(i)
        }
    }

    /**
     * 对pq[]中的元素执行上浮操作
     */
    private fun swim(i: Int) {
        var j = i
        while (j > 1 && less(j, j / 2)) {
            swap(j, j / 2)
            j /= 2
        }
    }

    /**
     * 对pq[]中的元素执行下沉操作
     */
    private fun sink(i: Int) {
        var j = i
        while (j * 2 <= size) {
            var k = j * 2
            if (k + 1 <= size && less(k + 1, k)) {
                k++
            }
            if (less(k, j)) {
                swap(k, j)
                j = k
            } else {
                break
            }
        }
    }

    private fun checkInRange(i: Int) {
        if (i !in 0 until maxSize) {
            throw IndexOutOfBoundsException("maxSize=${maxSize} i=$i")
        }
    }
}

fun main() {
    val priorityQueue = HeapIndexMinPriorityQueue<String>(10)
    println("Please input command:")
    println("0: exit, 1: set, 2: get, 3: delete, 4: min, 5: minIndex, 6: delMin 7: isEmpty, 8: size, 9: toString")
    while (true) {
        safeCall {
            when (readInt("command: ")) {
                0 -> return
                1 -> {
                    val index = readInt("set index: ")
                    val value = readString("set value: ")
                    priorityQueue[index] = value
                }
                2 -> {
                    val index = readInt("get index: ")
                    println(priorityQueue[index])
                }
                3 -> {
                    val index = readInt("delete index: ")
                    priorityQueue.delete(index)
                }
                4 -> {
                    val min = priorityQueue.min()
                    println("min=$min")
                }
                5 -> {
                    val minIndex = priorityQueue.minIndex()
                    println("minIndex=$minIndex")
                }
                6 -> {
                    val minIndex = priorityQueue.delMin()
                    println("delMin, index=$minIndex")
                }
                7 -> println("isEmpty=${priorityQueue.isEmpty()}")
                8 -> println("size=${priorityQueue.size()}")
                9 -> println(priorityQueue.toString())
            }
        }
    }
}