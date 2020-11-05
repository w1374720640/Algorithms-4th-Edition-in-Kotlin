package chapter3.section4

import chapter3.section1.ST
import chapter3.section1.testST
import edu.princeton.cs.algs4.Queue
import extensions.random
import extensions.spendTimeMillis
import kotlin.math.log2
import kotlin.math.pow

/**
 * Cuckoo散列函数的另一种实现
 * 只使用一个散列表，使用多个散列函数
 * 插入一个键值对时，依次使用多个散列函数计算散列值，若索引对应的位置为空，则插入空位置
 * 若所有的散列函数对应的位置都不为空，随机选择一个位置插入，踢出原来的键值对，将原来的键值对重新插入
 * 依次循环，当循环超过一定次数时对散列表扩容，防止死循环
 * 参考：http://blog.foool.net/2012/05/cuckoo-hash-%E5%B8%83%E8%B0%B7%E9%B8%9F%E5%93%88%E5%B8%8C/
 */
@Suppress("UNCHECKED_CAST")
class CuckooHashST2<K : Any, V : Any>(m: Int = 16) : ST<K, V> {
    // 散列函数的数量
    val hashFunNum = 5

    // 剔出操作最大循环次数
    val kickOutLimit = 10

    // 所有的散列函数
    private var hashFunList = ArrayList<(K) -> Int>()

    var m = m
        private set
    var n = 0
        private set
    private var keys = arrayOfNulls<Any>(m)
    private var values = arrayOfNulls<Any>(m)

    init {
        repeat(hashFunNum) {
            val randomBigInt = random(0x10000000, 0x7fffffff)
            hashFunList.add {
                var t = (it.hashCode() xor randomBigInt) and 0x7fffffff
                val logM = log2(m.toDouble()).toInt()
                if (logM < 26) {
                    t %= primes(logM)
                }
                t % m
            }
        }
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
        val newST = CuckooHashST2<K, V>(size)
        for (i in keys.indices) {
            if (keys[i] != null) {
                newST.put(keys[i] as K, keys[i] as V)
            }
        }
        m = newST.m
        n = newST.n
        keys = newST.keys
        values = newST.values
        hashFunList = newST.hashFunList
    }

    override fun put(key: K, value: V) {
        if (n >= m / 2) resize(m * 2)
        var index = getIndex(key)
        // 先判断键是否已经存在
        if (index != -1) {
            keys[index] = key
            values[index] = value
            return
        }

        var insertKey = key
        var insertValue = value
        //踢出次数
        var kickOutNum = 0
        while (kickOutNum < kickOutLimit) {
            hashFunList.forEach { hashFun ->
                index = hashFun(insertKey)
                val k = keys[index] as K?
                // 前面已经判断键不存在于表中，直接判断是否有空位置
                if (k == null) {
                    keys[index] = insertKey
                    values[index] = insertValue
                    // 只有插入新键时才自增1，插入踢出的数据不自增
                    if (insertKey == key) {
                        n++
                    }
                    return
                }
            }

            // 所有的位置都被占据，踢出一条数据，每次都剔第一个散列函数返回的索引
            index = hashFunList[0](insertKey)
            val oldKey = keys[index] as K
            val oldValue = values[index] as V
            keys[index] = insertKey
            values[index] = insertValue
            insertKey = oldKey
            insertValue = oldValue
            kickOutNum++
        }
        resize(m * 2)
        // 扩容后需要将被踢出的键值对重新插入，同时调整n的大小
        put(insertKey, insertValue)
        n--
    }

    override fun get(key: K): V? {
        val index = getIndex(key)
        return if (index == -1) null else values[index] as V
    }

    fun getIndex(key: K): Int {
        hashFunList.forEach { hashFun ->
            val index = hashFun(key)
            val k = keys[index] as K?
            if (k != null && k == key) {
                return index
            }
        }
        return -1
    }

    override fun delete(key: K) {
        val index = getIndex(key)
        if (index == -1) {
            throw NoSuchElementException()
        }
        keys[index] = null
        values[index] = null
        n--
        // 这里为了防止缩容时hash冲突重新扩容，设置的缩容条件比较苛刻
        if (m >= 32 && n <= m / 16) {
            resize(m / 2)
        }
    }

    override fun contains(key: K): Boolean {
        return getIndex(key) != -1
    }

    override fun isEmpty(): Boolean {
        return size() == 0
    }

    override fun size(): Int {
        return n
    }

    override fun keys(): Iterable<K> {
        val queue = Queue<K>()
        keys.forEach {
            if (it != null) {
                queue.enqueue(it as K)
            }
        }
        return queue
    }

    fun joinToString(limit: Int = 100): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("hashFunNum=$hashFunNum kickOutLimit=$kickOutLimit m=$m n=$n")
                .append("\n")
        for (i in keys.indices) {
            if (i >= limit) {
                stringBuilder.append(" ...")
                break
            }
            if (keys[i] == null) {
                stringBuilder.append(" _") //使用下划线替代null
            } else {
                stringBuilder.append(" ${keys[i].toString()}")
            }
        }
        return stringBuilder.toString()
    }
}

private fun testRandomKey(size: Int) {
    val array = IntArray(size) { random(Int.MAX_VALUE) }
    val st = CuckooHashST2<Int, Int>()
    array.forEach {
        st.put(it, it)
    }
    array.forEach {
        check(st.get(it) == it)
    }
    println("random key test succeed.")
    println("hashFunNum=${st.hashFunNum} kickOutLimit=${st.kickOutLimit} n=${st.n} array.size()=${st.m}")
}

fun main() {
    testST(CuckooHashST2())
    val time2 = spendTimeMillis {
        testRandomKey(100_0000)
    }
    println("spend $time2 ms")
}