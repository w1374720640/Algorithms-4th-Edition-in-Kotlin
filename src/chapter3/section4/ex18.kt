package chapter3.section4

import kotlin.math.log2
import kotlin.math.pow

/**
 * 为SeparateChainingHashST添加一个构造函数，使用能够指定查找操作可以接受的在链表中进行的平均探测次数
 * 动态调整数组的大小以保证链表的平均长度小于该值，并使用答疑中所述的方法来保证hash()方法的系数总是素数
 *
 * 解：假设J（均匀散列假设）：我们使用的散列函数能够均匀并独立的将所有的键散布于0到M-1之间
 * 命题K：在一张含有M条链表和N个键的散列表中，（在假设J成立的前提下）任意一条链表中键的数量均在N/M的常数因子范围内的概率无限趋向于1
 * 性质L：在一张含有M条链表和N个键的散列表中，未命名查找和插入操作所需的比较次数为~N/M
 *
 * 每次插入前的检查大小操作，保证N/M小于构造函数中指定的平均探测次数
 * 答疑中的方法为：先用一个大于M的素数来散列键值对，在用散列后的值和M散列
 */
class SpecifyAverageSearchNumSeparateChainingHashST<K : Any, V : Any>(val averageSearchNum: Int) : SeparateChainingHashST<K, V>() {
    override fun hash(key: K): Int {
        var t = key.hashCode() and 0x7fffffff
        val logM = log2(m.toDouble()).toInt()
        if (logM < 26) {
            t %= primes(logM)
        }
        return t % m
    }

    override fun put(key: K, value: V) {
        if (n / m >= averageSearchNum) resize(2 * m)
        val st = stArray[hash(key)]
        if (!st.contains(key)) n++
        st.put(key, value)
    }

    override fun delete(key: K) {
        val st = stArray[hash(key)]
        if (st.contains(key)) {
            st.delete(key)
            n--
            if (m > 4 && n / m < averageSearchNum / 4) resize(m / 2)
        } else {
            throw NoSuchElementException()
        }
    }
}

/**
 * 计算小于2^k的最大素数
 * 小于5时取固定值31
 */
private fun primes(k: Int): Int {
    require(k in 0..31)
    if (k < 5) return 31
    val array = intArrayOf(1, 3, 1, 5, 3, 3, 9, 3, 1, 3, 19, 15, 1, 5, 1, 3, 9, 3, 15, 3, 39, 5, 39, 57, 3, 35, 1)
    check(array.size == 31 - 5 + 1)
    return 2.0.pow(k).toInt() - array[k - 5]
}

fun main() {
    val st = SpecifyAverageSearchNumSeparateChainingHashST<Int, Int>(4)
    for (i in 0..15) {
        st.put(i, 0)
    }
    println(st.joinToString())
    println()

    //插入第17位时自动扩容
    st.put(16, 0)
    println(st.joinToString())
}