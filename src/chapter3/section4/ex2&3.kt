package chapter3.section4

import chapter3.section1.ST
import chapter3.section1.testST
import edu.princeton.cs.algs4.Queue
import extensions.random

/**
 * 3.4.2 重新实现SeparateChainingHashST，直接使用SequentialSearchST中链表部分的代码
 * 3.4.3 修改你为上一道练习给出的实现，为每个键值对添加一个整型变量，将其值设为插入该键值对时散列表中元素的数量
 * 实现一个方法，将该变量的值大于给定整数k的键（及其相应的值）全部删除
 * 注意：这个额外的功能在为编译器实现符号表时很有用
 */
class RecordNumHashST<K : Any, V : Any>(var m: Int = 4) : ST<K, V> {

    //num表示插入该键值对时散列表中元素的数量
    class Node<K : Any, V : Any>(val key: K, var value: V, var num: Int, var next: Node<K, V>? = null)

    private var n = 0 //n为键值对总数，m为散列表大小
    private var stRootArray = arrayOfNulls<Node<K, V>>(m)

    init {
        require(m > 0)
    }

    fun hash(key: K): Int {
        return (key.hashCode() and 0x7fffffff) % m
    }

    fun resize(size: Int) {
        val newHashST = RecordNumHashST<K, V>(size)
        for (i in stRootArray.indices) {
            var node = stRootArray[i]
            while (node != null) {
                newHashST.put(node.key, node.value, node.num)
                node = node.next
            }
        }
        m = newHashST.m
        stRootArray = newHashST.stRootArray
    }

    override fun put(key: K, value: V) {
        put(key, value, n)
    }

    private fun put(key: K, value: V, num: Int) {
        if (n >= 8 * m) resize(2 * m)

        val index = hash(key)
        var node = stRootArray[index]
        while (node != null) {
            if (node.key == key) {
                node.value = value
                node.num = num
                return
            } else {
                node = node.next
            }
        }
        stRootArray[index] = Node(key, value, num, stRootArray[index])
        n++
    }

    override fun get(key: K): V? {
        val index = hash(key)
        var node = stRootArray[index]
        while (node != null) {
            if (node.key == key) return node.value
            node = node.next
        }
        return null
    }

    override fun delete(key: K) {
        val index = hash(key)
        var node: Node<K, V> = stRootArray[index] ?: throw NoSuchElementException()
        if (node.key == key) {
            stRootArray[index] = node.next
            n--
            if (m >= 4 && n <= 2 * m) resize(m / 2)
            return
        }
        var nextNode = node.next
        while (nextNode != null) {
            if (nextNode.key == key) {
                node.next = nextNode.next
                n--
                if (m >= 4 && n <= 2 * m) resize(m / 2)
                return
            } else {
                node = nextNode
                nextNode = nextNode.next
            }
        }
        throw NoSuchElementException()
    }

    /**
     * 将所有键值对中，num变量大于给定整数k的键值对全部删除
     */
    fun deleteNumGreaterThan(k: Int) {
        for (i in stRootArray.indices) {
            var root = stRootArray[i]
            while (root != null) {
                //如果每个子链表的根结点num变量大于k，对根结点重新赋值
                if (root.num > k) {
                    root = root.next
                    stRootArray[i] = root
                    n--
                    continue
                }

                //非根结点的结点num变量大于k，重新拼接结点
                var node: Node<K, V> = root
                var nextNode = node.next

                //对root重新赋值，退出第二层循环
                root = null

                while (nextNode != null) {
                    if (nextNode.num > k) {
                        nextNode = nextNode.next
                        node.next = nextNode
                        n--
                    } else {
                        node = nextNode
                        nextNode = nextNode.next
                    }
                }
            }
        }
        //所有元素都删除后再重新计算大小
        if (m >= 4 && n <= 2 * m) resize(m / 2)
    }

    override fun contains(key: K): Boolean {
        return get(key) != null
    }

    override fun isEmpty(): Boolean {
        return size() == 0
    }

    override fun size(): Int {
        return n
    }

    override fun keys(): Iterable<K> {
        val queue = Queue<K>()
        for (i in stRootArray.indices) {
            var node = stRootArray[i]
            while (node != null) {
                queue.enqueue(node.key)
                node = node.next
            }
        }
        return queue
    }

    fun joinToString(): String {
        val stringBuilder = StringBuilder("m=${m} n=${n}")
        for (i in stRootArray.indices) {
            stringBuilder.append("\n")
                    .append("i=$i ")
            var node = stRootArray[i]
            while (node != null) {
                stringBuilder.append("[key:${node.key},num=${node.num}] ")
                node = node.next
            }
        }
        return stringBuilder.toString()
    }
}

fun main() {
    testST(RecordNumHashST())
    val st = RecordNumHashST<Int, Int>()
    val array1 = IntArray(20) { it }
    array1.forEach { st.put(it, 0) }
    println("put [0,19]")
    println(st.joinToString())
    println()

    repeat(10) {
        val value = random(20)
        if (st.contains(value)) {
            st.delete(value)
        }
    }
    println("random delete 10")
    println(st.joinToString())
    println()

    val array2 = IntArray(10) { it + 20 }
    array2.forEach { st.put(it, 0) }
    println("put [20,29]")
    println(st.joinToString())
    println()

    st.deleteNumGreaterThan(15)
    println("deleteNumGreaterThan 15")
    println(st.joinToString())
}