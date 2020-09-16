package chapter3.section1

import extensions.inputPrompt
import extensions.readInt
import extensions.readString
import extensions.safeCall

/**
 * 基于数组的有序符号表
 */
class ArrayOrderedST<K : Comparable<K>, V : Any> : OrderedST<K, V> {
    private var keys = Array<Comparable<K>?>(4) { null }
    private var values = Array<Any?>(4) { null }
    private var size = 0

    override fun put(key: K, value: V) {
        val rankSize = rank(key)
        if (rankSize < size && keys[rankSize] == key) {
            values[rankSize] = value
        } else {
            if (needExpansion()) {
                expansion()
            }
            for (i in size - 1 downTo rankSize) {
                keys[i + 1] = keys[i]
                values[i + 1] = values[i]
            }
            keys[rankSize] = key
            values[rankSize] = value
            size++
        }
    }


    override fun get(key: K): V? {
        val rankSize = rank(key)
        if (rankSize < size && keys[rankSize]!! == key) {
            @Suppress("UNCHECKED_CAST")
            return values[rankSize] as V
        }
        return null
    }

    override fun delete(key: K) {
        val rankSize = rank(key)
        if (rankSize < size && keys[rankSize]!! == key) {
            for (i in rankSize until size - 1) {
                keys[i] = keys[i + 1]
                values[i] = keys[i + 1]
            }
            keys[size - 1] = null
            values[size - 1] = null
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
        if (rankSize < size && keys[rankSize] == key) {
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
                        @Suppress("UNCHECKED_CAST")
                        return keys[index++] as K
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
                        highIndex = if (highRankSize < size && keys[highRankSize] == high) {
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
                        @Suppress("UNCHECKED_CAST")
                        return keys[index++] as K
                    }

                }
            }

        }
    }

    override fun min(): K {
        if (isEmpty()) {
            throw NoSuchElementException()
        }
        @Suppress("UNCHECKED_CAST")
        return keys[0] as K
    }

    override fun max(): K {
        if (isEmpty()) {
            throw NoSuchElementException()
        }
        @Suppress("UNCHECKED_CAST")
        return keys[size - 1] as K
    }

    override fun floor(key: K): K {
        val rankSize = rank(key)
        return if (rankSize < size && keys[rankSize] == key) {
            key
        } else {
            if (rankSize == 0) {
                throw NoSuchElementException()
            }
            @Suppress("UNCHECKED_CAST")
            keys[rankSize - 1] as K
        }
    }

    override fun ceiling(key: K): K {
        val rankSize = rank(key)
        if (rankSize == size) {
            throw NoSuchElementException()
        }
        @Suppress("UNCHECKED_CAST")
        return keys[rankSize] as K
    }

    override fun rank(key: K): Int {
        var start = 0
        var end = size - 1
        while (start <= end) {
            val mid = (start + end) / 2
            when {
                keys[mid]!! < key -> start = mid + 1
                keys[mid]!! > key -> end = mid - 1
                else -> return mid
            }
        }
        return start
    }

    override fun select(k: Int): K {
        if (k >= size) throw NoSuchElementException()
        @Suppress("UNCHECKED_CAST")
        return keys[k]!! as K
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
        return if (highRankSize < size && keys[highRankSize] == high) {
            highRankSize - lowRankSize + 1
        } else {
            highRankSize - lowRankSize
        }
    }

    private fun needExpansion() = keys.size == size

    private fun needShrink() = keys.size > 4 && keys.size - 1 >= size * 4

    /**
     * 当数组占满时容量扩大一倍
     */
    private fun expansion() {
        val newSize = keys.size * 2
        val newKeys = arrayOfNulls<Comparable<K>>(newSize)
        val newValues = arrayOfNulls<Any>(newSize)
        repeat(size) {
            newKeys[it] = keys[it]
            newValues[it] = values[it]
        }
        keys = newKeys
        values = newValues
    }

    /**
     * 当数组使用空间小于四分之一时缩小一倍
     */
    private fun shrink() {
        if (keys.size <= 4) return
        val newKeys = arrayOfNulls<Comparable<K>>(keys.size / 2)
        val newValues = arrayOfNulls<Any>(keys.size / 2)
        repeat(size) {
            newKeys[it] = keys[it]
            newValues[it] = values[it]
        }
        keys = newKeys
        values = newValues
    }
}

fun main() {
    inputPrompt()
    val st = ArrayOrderedST<String, String>()
    println("Please input commands:")
    val tips = "0: exit, 1: put, 2: get, 3: delete, 4: contains, 5: isEmpty, 6: size, 7: keys\n" +
            "10: min, 11: max, 12: floor, 13: ceiling, 14: rank, 15: select, 16: deleteMin, 17: deleteMax\n" +
            "18: size(low,high), 19: keys(low,high)"
    println(tips)
    while (true) {
        safeCall {
            when (readInt("command: ")) {
                0 -> return
                1 -> {
                    val key = readString("put: key=")
                    val value = readString("value=")
                    st.put(key, value)
                }
                2 ->println(st.get(readString("get: key=")))
                3 -> st.delete(readString("delete: key="))
                4 -> println(st.contains(readString("contains: key=")))
                5 -> println("isEmpty=${st.isEmpty()}")
                6 -> println("size=${st.size()}")
                7 -> println("keys=${st.keys().joinToString()}")
                10 -> println("min=${st.min()}")
                11 -> println("max=${st.max()}")
                12 -> println("floor=${st.floor(readString("floor: key="))}")
                13 -> println("ceiling=${st.ceiling(readString("ceiling: key="))}")
                14 -> println("rank=${st.rank(readString("rank, key="))}")
                15 -> println("select=${st.select(readInt("select: "))}")
                16 -> {
                    println("deleteMin")
                    st.deleteMin()
                }
                17 -> {
                    println("deleteMax")
                    st.deleteMax()
                }
                18 -> {
                    val low = readString("size(low,high): low=")
                    val high = readString("high=")
                    println("size=${st.size(low, high)}")
                }
                19 -> {
                    val low = readString("keys(low,high): low=")
                    val high = readString("high=")
                    println("keys=${st.keys(low, high).joinToString()}")
                }
                else -> {
                    println(tips)
                }
            }
        }
    }
}