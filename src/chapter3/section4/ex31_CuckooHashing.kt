package chapter3.section4

import chapter3.section1.ST
import chapter3.section1.testST
import edu.princeton.cs.algs4.Queue
import extensions.random
import extensions.setSeed
import kotlin.math.log2
import kotlin.math.pow

/**
 * Cuckoo散列函数
 * 实现一个符号表，在其中维护两张散列表和两个散列函数
 * 一个给定的键只能存在于一张散列表中
 * 在插入一个新键时，在其中一张散列表中插入该键
 * 如果这张表中该键的位置已经被占用了，就用新键替代老键并将老键插入到另一张散列表中
 * （如果在这张表中该键的位置也被占用了，那么就将这个占用者重新插入第一张散列表，把位置腾给被插入的键）
 * 如此循环往复，动态调整数组大小以保持两张表都不到半满
 * 这种实现中查找所需的比较次数在最坏情况下是一个常数，插入操作所需的时间在均摊后也是常数
 *
 * 解：查找时，键要么在第一张表中，要么在第二张表中，最多只需要两次hash运算
 * 插入时使用两个独立且均匀散列的散列函数
 * 散列函数中先用key的原始hashCode和一个大整数异或，两个散列函数使用不同的大整数
 * 然后再根据练习3.4.18以及答疑中的方法，先和一个大素数取余，再和m取余
 *
 * BUG：无论选择什么样的散列函数，都可能导致死循环，暂时的解决方法是直接扩大数组容量，会导致数组快速膨胀
 * 还有一种方法，当出现死循环时，重新生成新的散列函数，将所有的键重新插入，但是对于大量数据找到合适的散列函数耗时仍然太长
 * 参考：https://github.com/reneargento/algorithms-sedgewick-wayne/blob/master/src/chapter3/section4/Exercise31_CuckooHashing.java
 */
class CuckooHashST<K : Any, V : Any>(m: Int = 4) : ST<K, V> {
    init {
        require(m > 0)
    }

    //第一张表中的元素数量
    var n1 = 0
        private set

    //第二张表中的元素数量
    var n2 = 0
        private set

    //数组长度
    var m = m
        private set

    private var keys1 = arrayOfNulls<Any>(m)
    private var values1 = arrayOfNulls<Any>(m)
    private var keys2 = arrayOfNulls<Any>(m)
    private var values2 = arrayOfNulls<Any>(m)

    fun hash1(key: K): Int {
        var t = (key.hashCode() xor 0x2e7bb6b1) and 0x7fffffff
        val logM = log2(m.toDouble()).toInt()
        if (logM < 26) {
            t %= primes(logM)
        }
        return t % m
    }

    fun hash2(key: K): Int {
        var t = (key.hashCode() xor 0x58795ee3) and 0x7fffffff
        val logM = log2(m.toDouble()).toInt()
        if (logM < 26) {
            t %= primes(logM)
        }
        return t % m
    }

    /**
     * 计算小于2^k的最大素数
     * 小于5时取固定值31
     */
    fun primes(k: Int): Int {
        require(k in 0..31)
        if (k < 5) return 31
        val array = intArrayOf(1, 3, 1, 5, 3, 3, 9, 3, 1, 3, 19, 15, 1, 5, 1, 3, 9, 3, 15, 3, 39, 5, 39, 57, 3, 35, 1)
        check(array.size == 31 - 5 + 1)
        return 2.0.pow(k).toInt() - array[k - 5]
    }

    @Suppress("UNCHECKED_CAST")
    private fun resize(size: Int) {
        val newST = CuckooHashST<K, V>(size)
        for (i in keys1.indices) {
            if (keys1[i] != null) {
                newST.put(keys1[i] as K, values1[i] as V)
            }
            if (keys2[i] != null) {
                newST.put(keys2[i] as K, values2[i] as V)
            }
        }
        m = newST.m
        n1 = newST.n1
        n2 = newST.n2
        keys1 = newST.keys1
        values1 = newST.values1
        keys2 = newST.keys2
        values2 = newST.values2
    }

    @Suppress("UNCHECKED_CAST")
    override fun put(key: K, value: V) {
        if (n1 >= m / 2 || n2 >= m / 2) resize(m * 2)
        val i1 = hash1(key)
        val k1 = keys1[i1] as K?
        if (k1 != null && k1 == key) {
            values1[i1] = value
            return
        }
        val i2 = hash2(key)
        val k2 = keys2[i2] as K?
        if (k2 != null && k2 == key) {
            values2[i2] = value
            return
        }

        if (n1 <= n2) {
            putTable1(key, value)
        } else {
            putTable2(key, value)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun putTable1(key: K, value: V, recursionDepth: Int = 0) {
        val i = hash1(key)
        when (keys1[i]) {
            null -> {
                keys1[i] = key
                values1[i] = value
                n1++
            }
            key -> values1[i] = value
            else -> {
                if (recursionDepth < 5) {
                    val oldKey = keys1[i] as K
                    val oldValue = values1[i] as V
                    keys1[i] = key
                    values1[i] = value
                    putTable2(oldKey, oldValue, recursionDepth + 1)
                } else {
                    //FIXME 这里会导致数组快速膨胀
                    resize(m * 2)
                    put(key, value)
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun putTable2(key: K, value: V, recursionDepth: Int = 0) {
        val i = hash2(key)
        when (keys2[i]) {
            null -> {
                keys2[i] = key
                values2[i] = value
                n2++
            }
            key -> values2[i] = value
            else -> {
                if (recursionDepth < 5) {
                    val oldKey = keys2[i] as K
                    val oldValue = values2[i] as V
                    keys2[i] = key
                    values2[i] = value
                    putTable1(oldKey, oldValue, recursionDepth + 1)
                } else {
                    //FIXME 这里会导致数组快速膨胀
                    resize(m * 2)
                    put(key, value)
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun get(key: K): V? {
        val i1 = hash1(key)
        val k1 = keys1[i1] as K?
        if (k1 != null && k1 == key) return values1[i1] as V
        val i2 = hash2(key)
        val k2 = keys2[i2] as K?
        if (k2 != null && k2 == key) return values2[i2] as V
        return null
    }

    @Suppress("UNCHECKED_CAST")
    override fun delete(key: K) {
        val i1 = hash1(key)
        val k1 = keys1[i1] as K?
        if (k1 != null && k1 == key) {
            keys1[i1] = null
            values1[i1] = null
            n1--
            if (m >= 8 && n1 + n2 <= m / 8) resize(m / 2)
            return
        }
        val i2 = hash2(key)
        val k2 = keys2[i2] as K?
        if (k2 != null && k2 == key) {
            keys2[i2] = null
            values2[i2] = null
            n2--
            if (m >= 8 && n1 + n2 <= m / 8) resize(m / 2)
            return
        }
        throw NoSuchElementException()
    }

    override fun contains(key: K): Boolean {
        return get(key) != null
    }

    override fun isEmpty(): Boolean {
        return size() == 0
    }

    override fun size(): Int {
        return n1 + n2
    }

    @Suppress("UNCHECKED_CAST")
    override fun keys(): Iterable<K> {
        val queue = Queue<K>()
        keys1.forEach {
            if (it != null) {
                queue.enqueue(it as K)
            }
        }
        keys2.forEach {
            if (it != null) {
                queue.enqueue(it as K)
            }
        }
        return queue
    }
}

private fun testRandomKey() {
    setSeed(1000)
    val size = 10000
    val st = CuckooHashST<Int, Int>()
    repeat(size) {
        st.put(random(Int.MAX_VALUE), 0)
    }
    println("random key test succeed.")
}

fun main() {
    testST(CuckooHashST())
    testRandomKey()
}