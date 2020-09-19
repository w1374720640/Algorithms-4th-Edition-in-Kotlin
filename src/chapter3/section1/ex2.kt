package chapter3.section1

import extensions.inputPrompt
import extensions.readInt
import extensions.readString
import extensions.safeCall

/**
 * 基于数组的无序符号表
 */
class ArrayST<K : Any, V : Any> : ST<K, V> {
    private var keys = arrayOfNulls<Any>(4)
    private var values = arrayOfNulls<Any>(4)
    private var size = 0

    override fun put(key: K, value: V) {
        for (i in 0 until size) {
            if (keys[i] == key) {
                values[i] = value
                return
            }
        }
        if (needExpansion()) {
            expansion()
        }
        keys[size] = key
        values[size] = value
        size++
    }

    override fun get(key: K): V? {
        for (i in 0 until size) {
            if (keys[i] == key) {
                @Suppress("UNCHECKED_CAST")
                return values[i] as V
            }
        }
        return null
    }

    override fun delete(key: K) {
        for (i in 0 until size) {
            if (keys[i] == key) {
                keys[i] = keys[size - 1]
                keys[size - 1] = null
                size--
                if (needShrink()) {
                    shrink()
                }
                return
            }
        }
        throw NoSuchElementException()
    }

    override fun contains(key: K): Boolean {
        for (i in 0 until size) {
            if (keys[i] == key) {
                return true
            }
        }
        return false
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun size(): Int {
        return size
    }

    override fun keys(): Iterable<K> {
        return object : Iterable<K> {
            override fun iterator(): Iterator<K> {
                return object : Iterator<K> {
                    var i = 0
                    override fun hasNext(): Boolean {
                        return i < size
                    }

                    override fun next(): K {
                        if (i >= size) throw NoSuchElementException()
                        @Suppress("UNCHECKED_CAST")
                        return keys[i++] as K
                    }

                }
            }

        }
    }

    private fun needExpansion() = keys.size == size

    private fun needShrink() = keys.size > 4 && keys.size - 1 >= size * 4

    /**
     * 当数组占满时容量扩大一倍
     */
    private fun expansion() {
        val newSize = keys.size * 2
        val newKeys = arrayOfNulls<Any>(newSize)
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
        val newKeys = arrayOfNulls<Any>(keys.size / 2)
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
    testST(ArrayST())

    inputPrompt()
    val st = ArrayST<String, String>()
    println("Please input commands:")
    println("0: exit, 1: put, 2: get, 3: delete, 4: contains, 5: isEmpty, 6: size, 7: keys")
    while (true) {
        safeCall {
            when (readInt("command: ")) {
                0 -> return
                1 -> {
                    val key = readString("put: key=")
                    val value = readString("value=")
                    st.put(key, value)
                }
                2 -> {
                    val key = readString("get: key=")
                    println(st.get(key))
                }
                3 -> {
                    val key = readString("delete: key=")
                    st.delete(key)
                }
                4 -> {
                    val key = readString("contains: key=")
                    println(st.contains(key))
                }
                5 -> println("isEmpty=${st.isEmpty()}")
                6 -> println("size=${st.size()}")
                7 -> println("keys=${st.keys().joinToString()}")
            }
        }
    }
}