package chapter3.section1

import chapter2.section2.topDownMergeSort

/**
 * 修改ArrayOrderedST，用一个Item对象的数组而非两个平行数组来保存键和值
 * 添加一个构造函数，接受一个Item的数组为参数并将其归并排序
 */
class OneArrayOrderedST<K : Comparable<K>, V : Any>() : OrderedST<K, V> {
    class Item<K : Comparable<K>, V : Any>(val key: K, var value: V) : Comparable<Item<K, V>> {
        override fun compareTo(other: Item<K, V>): Int {
            return key.compareTo(other.key)
        }
    }

    private var items = arrayOfNulls<Item<K, V>>(4)
    private var size = 0

    constructor(array: Array<Item<K, V>>) : this() {
        topDownMergeSort(array)
        items = arrayOfNulls(array.size)
        for (i in array.indices) {
            items[i] = array[i]
        }
    }

    override fun put(key: K, value: V) {
        val rankSize = rank(key)
        if (rankSize < size && items[rankSize]!!.key == key) {
            items[rankSize]!!.value = value
        } else {
            if (needExpansion()) {
                expansion()
            }
            for (i in size - 1 downTo rankSize) {
                items[i + 1] = items[i]
            }
            items[rankSize] = Item(key, value)
            size++
        }
    }


    override fun get(key: K): V? {
        val rankSize = rank(key)
        if (rankSize < size && items[rankSize]!!.key == key) {
            return items[rankSize]!!.value
        }
        return null
    }

    override fun delete(key: K) {
        val rankSize = rank(key)
        if (rankSize < size && items[rankSize]!!.key == key) {
            for (i in rankSize until size - 1) {
                items[i] = items[i + 1]
            }
            items[size - 1] = null
            size--
            if (needShrink()) {
                shrink()
            }
        } else {
            throw NoSuchElementException()
        }
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun size(): Int {
        return size
    }

    override fun contains(key: K): Boolean {
        val rankSize = rank(key)
        if (rankSize < size && items[rankSize]!!.key == key) {
            return true
        }
        return false
    }

    override fun keys(): Iterable<K> {
        return object : Iterable<K> {
            override fun iterator(): Iterator<K> {
                return object : Iterator<K> {
                    var index = 0

                    override fun hasNext(): Boolean {
                        return index < size
                    }

                    override fun next(): K {
                        if (index >= size) throw NoSuchElementException()
                        return items[index++]!!.key
                    }

                }
            }

        }
    }

    override fun keys(low: K, high: K): Iterable<K> {
        return object : Iterable<K> {
            override fun iterator(): Iterator<K> {
                return object : Iterator<K> {
                    var index: Int
                    var highIndex: Int

                    init {
                        val lowRankSize = rank(low)
                        index = lowRankSize
                        val highRankSize = rank(high)
                        highIndex = if (highRankSize < size && items[highRankSize] != null && items[highRankSize]!!.key == high) {
                            highRankSize
                        } else {
                            highRankSize - 1
                        }
                    }

                    override fun hasNext(): Boolean {
                        return index <= highIndex
                    }

                    override fun next(): K {
                        if (index > highIndex) throw NoSuchElementException()
                        return items[index++]!!.key
                    }

                }
            }

        }
    }

    override fun min(): K {
        if (isEmpty()) {
            throw NoSuchElementException()
        }
        return items[0]!!.key
    }

    override fun max(): K {
        if (isEmpty()) {
            throw NoSuchElementException()
        }
        return items[size - 1]!!.key
    }

    override fun floor(key: K): K {
        val rankSize = rank(key)
        return if (rankSize < size && items[rankSize]!!.key == key) {
            key
        } else {
            if (rankSize == 0) {
                throw NoSuchElementException()
            }
            items[rankSize - 1]!!.key
        }
    }

    override fun ceiling(key: K): K {
        val rankSize = rank(key)
        if (rankSize == size) {
            throw NoSuchElementException()
        }
        return items[rankSize]!!.key
    }

    override fun rank(key: K): Int {
        var start = 0
        var end = size - 1
        while (start <= end) {
            val mid = (start + end) / 2
            when {
                items[mid]!!.key < key -> start = mid + 1
                items[mid]!!.key > key -> end = mid - 1
                else -> return mid
            }
        }
        return start
    }

    override fun select(k: Int): K {
        if (k < 0 || k >= size) throw NoSuchElementException()
        return items[k]!!.key
    }

    override fun deleteMin() {
        delete(min())
    }

    override fun deleteMax() {
        delete(max())
    }

    override fun size(low: K, high: K): Int {
        if (low > high) return 0
        val lowRankSize = rank(low)
        val highRankSize = rank(high)
        return if (highRankSize < size && items[highRankSize] != null && items[highRankSize]!!.key == high) {
            highRankSize - lowRankSize + 1
        } else {
            highRankSize - lowRankSize
        }
    }

    private fun needExpansion() = items.size == size

    private fun needShrink() = items.size > 4 && items.size - 1 >= size * 4

    /**
     * 当数组占满时容量扩大一倍
     */
    private fun expansion() {
        val newSize = items.size * 2
        val newItems = arrayOfNulls<Item<K, V>>(newSize)
        repeat(size) {
            newItems[it] = items[it]
        }
        items = newItems
    }

    /**
     * 当数组使用空间小于四分之一时缩小一倍
     */
    private fun shrink() {
        if (items.size <= 4) return
        val newItems = arrayOfNulls<Item<K, V>>(items.size / 2)
        repeat(size) {
            newItems[it] = items[it]
        }
        items = newItems
    }
}

fun main() {
    testOrderedST(OneArrayOrderedST())
}