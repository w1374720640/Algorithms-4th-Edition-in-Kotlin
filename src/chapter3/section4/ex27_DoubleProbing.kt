package chapter3.section4

import chapter3.section1.testST
import extensions.formatDouble

/**
 * 二次探测
 * 修改SeparateChainingHashST，进行二次散列并选择两条链表中的较短者
 * 将键 E A S Y Q U T I O N 依次插入一张初始为空且大小为M=3的基于拉链法的散列表中
 * 以 11 k % M 作为第一个散列函数，17 k % M 作为第二个散列函数来将第k个字母散列到某个数组索引上
 * 给出插入过程的轨迹以及随机的命中查找和未命中查找在该符号表中所需的平均探测次数
 *
 * 解：插入过程中需要先分别遍历两条链表，只有当两条链表中都不存在时才作为新值插入较短的链表
 * delete()和get()方法同理需要先后判断两条链表是否存在对应键
 *
 * 命中查找平均探测次数：
 * 遍历所有链表，用hash1()函数计算key的hash值，判断和实际索引是否相同，不相同则额外加上hash1索引对应链表的长度
 * 然后再加上该键在当前链表中的索引加一
 * 未命中查找的平均探测次数：
 * 未命中时，需要先后遍历hash1()函数和hash2()函数对应的链表，平均而言等于总数量n的两倍再除以m
 *
 * 这题中M=3会导致hash1()和hash2()函数的返回值相同
 */
class DoubleProbingSeparateChainingHashST<K : Any, V : Any>(m: Int = 4) : SeparateChainingHashST<K, V>(m) {
    fun hash1(key: K): Int {
        return ((11 * key.hashCode()) and 0x7fffffff) % m
    }

    fun hash2(key: K): Int {
        return ((17 * key.hashCode()) and 0x7fffffff) % m
    }

    override fun put(key: K, value: V) {
        if (n >= 8 * m) resize(2 * m)

        val hash1 = hash1(key)
        val hash2 = hash2(key)
        val st1 = stArray[hash1]
        val st2 = stArray[hash2]
        if (st1.contains(key)) {
            st1.put(key, value)
            return
        }
        if (st2.contains(key)) {
            st2.put(key, value)
            return
        }
        if (st1.size() < st2.size()) {
            st1.put(key, value)
        } else {
            st2.put(key, value)
        }
        n++
    }

    override fun get(key: K): V? {
        val st1 = stArray[hash1(key)]
        val value1 = st1.get(key)
        if (value1 != null) return value1
        val st2 = stArray[hash2(key)]
        return st2.get(key)
    }

    override fun delete(key: K) {
        val hash1 = hash1(key)
        val hash2 = hash2(key)
        val st1 = stArray[hash1]
        val st2 = stArray[hash2]
        if (st1.contains(key)) {
            st1.delete(key)
            n--
            if (m > 4 && n <= 2 * m) resize(m / 2)
            return
        }
        if (st2.contains(key)) {
            st2.delete(key)
            n--
            if (m > 4 && n <= 2 * m) resize(m / 2)
            return
        }
        throw NoSuchElementException()
    }

    fun hitSearchCost(): Double {
        if (n == 0) return 0.0
        var cost = 0
        for (i in stArray.indices) {
            val st = stArray[i]
            var j = 0
            st.keys().forEach {
                j++
                if (hash1(it) != i) {
                    //如果索引不是用hash1()方法计算出来的，需要加上hash1()方法对应链表的长度
                    cost += stArray[hash1(it)].size()
                }
                cost += j
            }
        }
        return cost.toDouble() / n
    }

    fun missSearchCost(): Double {
        return n * 2.0 / m
    }
}

fun main() {
    testST(DoubleProbingSeparateChainingHashST())
    println()

    val st = DoubleProbingSeparateChainingHashST<Char, Int>(3)
    "EASYQUTION".forEach {
        st.put(it, 0)
        println("insert '${it}'")
        println(st.joinToString())
        println("hitSearchCost=${formatDouble(st.hitSearchCost(), 2)} missSearchCost=${formatDouble(st.missSearchCost(), 2)}")
        println()
    }
}