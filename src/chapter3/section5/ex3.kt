package chapter3.section5

import edu.princeton.cs.algs4.Queue

/**
 * 删除[chapter3.section1.BinarySearchST]中和值相关的所有代码来实现BinarySearchSET
 */
@Suppress("UNCHECKED_CAST")
class BinarySearchSET<K: Comparable<K>> : OrderedSET<K> {
    var keys = arrayOfNulls<Any>(4)
    var size = 0

    private fun resize(size: Int) {
        val newKeys = arrayOfNulls<Any>(size)
        for (i in keys.indices) {
            newKeys[i] = keys[i]
        }
        keys = newKeys
    }

    override fun add(key: K) {
        val index = rank(key)
        if (index < size && keys[index] as K == key) return
        if (size == keys.size) resize(size * 2)
        for (i in size downTo index + 1) {
            keys[i] = keys[i - 1]
        }
        keys[index] = key
        size++
    }

    override fun delete(key: K) {
        val index = rank(key)
        if (index < size && keys[index] as K == key) {
            for (i in index..size - 2) {
                keys[i] = keys[i + 1]
            }
            keys[size - 1] = null
            size--
            if (keys.size > 4 && keys.size >= size * 4) {
                resize(keys.size / 2)
            }
        } else {
            throw NoSuchElementException()
        }
    }

    override fun contains(key: K): Boolean {
        val index = rank(key)
        return index < size && keys[index] as K == key
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun size(): Int {
        return size
    }

    override fun keys(): Iterable<K> {
        val queue = Queue<K>()
        for (i in 0 until size) {
            queue.enqueue(keys[i] as K)
        }
        return queue
    }

    override fun min(): K {
        if (isEmpty()) throw NoSuchElementException()
        return keys[0] as K
    }

    override fun max(): K {
        if (isEmpty()) throw NoSuchElementException()
        return keys[size - 1] as K
    }

    override fun floor(key: K): K {
        val index = rank(key)
        return when {
            index == size -> keys[size - 1] as K
            keys[index] == key -> keys[index] as K
            index == 0 -> throw NoSuchElementException()
            else -> keys[index - 1] as K
        }
    }

    override fun ceiling(key: K): K {
        val index = rank(key)
        if (index == size) {
            throw NoSuchElementException()
        } else {
            return keys[index] as K
        }
    }

    override fun rank(key: K): Int {
        if (size == 0) return 0
        var start = 0
        var end = size - 1
        while (start <= end) {
            val mid = (start + end) / 2
            val compareResult = key.compareTo(keys[mid] as K)
            when  {
                compareResult > 0 -> start = mid + 1
                compareResult < 0 -> end = mid - 1
                else -> return mid
            }
        }
        return start
    }

    override fun select(k: Int): K {
        if (k < 0 || k >= size) throw NoSuchElementException()
        return keys[k] as K
    }

    override fun deleteMin() {
        if (isEmpty()) throw NoSuchElementException()
        for (i in 0..size - 2) {
            keys[i] = keys[i + 1]
        }
        keys[size - 1] = null
        size--
    }

    override fun deleteMax() {
        if (isEmpty()) throw NoSuchElementException()
        keys[size - 1] = null
        size--
    }

    override fun size(low: K, high: K): Int {
        val lowIndex = rank(low)
        val highIndex = rank(high)
        return if (highIndex < size && keys[highIndex] == high) {
            highIndex - lowIndex + 1
        } else {
            highIndex - lowIndex
        }
    }

    override fun keys(low: K, high: K): Iterable<K> {
        val queue = Queue<K>()
        if (low > high) return queue
        val lowIndex = rank(low)
        val highIndex = rank(high)
        var containHigh = false
        if (highIndex < size && keys[highIndex] == high) {
            containHigh = true
        }
        for (i in lowIndex..if (containHigh) highIndex else highIndex - 1) {
            queue.enqueue(keys[i] as K)
        }
        return queue
    }
}

fun main() {
    testOrderedSET(BinarySearchSET())
}