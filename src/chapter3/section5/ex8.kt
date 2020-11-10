package chapter3.section5

import edu.princeton.cs.algs4.Queue

/**
 * 修改LinearProbingHashST，允许在表中保存重复的键
 * 对于get()方法，返回给定键所关联的任意值
 * 对于delete()方法，删除表中所有和给定键相等的键值对
 *
 * 解：使用一个集合存放一个键对应的所有值
 * 将键和集合组成一个新对象保存在数组的对应位置
 */
class DuplicateKeysLinearProbingHashST<K : Any, V : Any>(var m: Int = 16) : DuplicateKeysST<K, V> {
    class Node<K : Any, V : Any>(val key: K) {
        val set = SequentialSearchSET<V>()

        fun add(value: V) {
            set.add(value)
        }

        fun size(): Int {
            return set.size()
        }

        fun contain(value: V): Boolean {
            return set.contains(value)
        }

        fun getOneValue(): V {
            if (set.isEmpty()) throw NoSuchElementException()
            return set.first!!.key
        }

        fun getAllValues(): Iterable<V> {
            return set.keys()
        }

        override fun equals(other: Any?): Boolean {
            if (other == null) return false
            if (other === this) return true
            if (other !is Node<*, *>) return false
            return key == other.key
        }

        override fun hashCode(): Int {
            return key.hashCode()
        }
    }

    var nodeList = arrayOfNulls<Node<K, V>>(m)
    var n = 0 // 这里的n表示键的数量，而不是键值对的数量

    private fun resize(size: Int) {
        val newHashST = DuplicateKeysLinearProbingHashST<K, V>(size)
        for (i in 0 until m) {
            val node = nodeList[i]
            if (node != null) {
                val allValue = node.getAllValues()
                allValue.forEach {
                    newHashST.put(node.key, it)
                }
            }
        }
        m = size
        nodeList = newHashST.nodeList
    }

    fun hash(key: K): Int {
        return (key.hashCode() and 0x7fffffff) % m
    }

    override fun put(key: K, value: V) {
        if (n >= m / 2) resize(m * 2)
        var index = hash(key)
        var node = nodeList[index]
        while (node != null) {
            if (node.key == key) {
                node.add(value)
                return
            }
            index = (index + 1) % m
            node = nodeList[index]
        }
        node = Node(key)
        node.add(value)
        nodeList[index] = node
        n++
    }

    override fun get(key: K): V? {
        var index = hash(key)
        var node = nodeList[index]
        while (node != null) {
            if (node.key == key) {
                return node.getOneValue()
            }
            index = (index + 1) % m
            node = nodeList[index]
        }
        return null
    }

    override fun getAllValues(key: K): Iterable<V>? {
        var index = hash(key)
        var node = nodeList[index]
        while (node != null) {
            if (node.key == key) {
                return node.getAllValues()
            }
            index = (index + 1) % m
            node = nodeList[index]
        }
        return null
    }

    override fun delete(key: K) {
        var index = hash(key)
        var node = nodeList[index]
        while (node != null) {
            if (node.key == key) {
                var nextIndex = (index + 1) % m
                var nextNode = nodeList[nextIndex]
                while (nextNode != null && hash(nextNode.key) != nextIndex) {
                    nodeList[index] = nextNode
                    index = nextIndex
                    nextIndex = (nextIndex + 1) % m
                    nextNode = nodeList[nextIndex]
                }
                nodeList[index] = null
                n--
                if (m > 16 && n <= m / 8) resize(m / 2)
                return
            }
            index = (index + 1) % m
            node = nodeList[index]
        }
        throw NoSuchElementException()
    }

    override fun contains(key: K): Boolean {
        return get(key) != null
    }

    override fun isEmpty(): Boolean {
        return n == 0
    }

    override fun size(): Int {
        return n
    }

    override fun keys(): Iterable<K> {
        val queue = Queue<K>()
        for (i in 0 until m) {
            val node = nodeList[i]
            if (node != null) {
                queue.enqueue(node.key)
            }
        }
        return queue
    }
}

fun main() {
    testDuplicateKeysST(DuplicateKeysLinearProbingHashST())
}