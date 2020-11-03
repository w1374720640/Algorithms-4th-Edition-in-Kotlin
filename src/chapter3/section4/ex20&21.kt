package chapter3.section4

import extensions.formatDouble
import extensions.random

/**
 * 3.4.20：为LinearProbingHashST添加一个方法来计算一次命中查找的平均成本，假设表中每个键被查找的可能性相同
 * 3.4.21：为LinearProbingHashST添加一个方法来计算一次未命中查找的平均成本，假设使用了一个随机的散列函数
 * 请注意：要解决这个问题并不一定要计算所有的散列函数
 *
 * 解：命题M：在一张大小为M并含有N=aM个键的基于线性探测的散列表中，基于假设J，命中和未命中的查找所需的探测次数分别为：
 * ~1/2*(1+1/(1-a))和~1/2(1+1/(1-a)^2)
 * 特别是当a约为1/2时，查找命中所需的探测次数约为3/2，未命中所需要的约为5/2
 *
 * 计算命中查找的平均成本：
 * 依次遍历键数组，如果键不为空，计算hash值，键的查找成本为索引与hash值的距离加一
 * 若索引小于hash值，则距离为 m - hash + i
 *
 * 计算未命中查找的平均成本：
 * 先找到第一个键为空的位置，然后依次遍历数组，如果键为空，查找成本加一
 * 如果键不为空，计算当前键簇的长度l，则当前键簇的查找成本为2+3+4...+(l+1)
 * （因为get()方法的实现会在索引与hash不同时直接遍历后面所有连续的键，直到遇见空键，至少判断两次，不会判断后续的键hash值和索引是否相同）
 * 若遍历到数组末尾，键簇的长度l继续从数组起始位置累加
 */
class SearchCostLinearProbingHashST<K : Any, V : Any> : LinearProbingHashST<K, V>() {
    /**
     * 命中查找成本
     */
    @Suppress("UNCHECKED_CAST")
    fun hitSearchCost(): Double {
        if (n == 0) return 0.0
        var cost = 0
        for (i in keys.indices) {
            val key = keys[i] as K?
            if (key != null) {
                val hashCode = hash(key)
                when {
                    hashCode == i -> cost++
                    hashCode < i -> cost += i - hashCode + 1
                    hashCode > i -> cost += m - hashCode + i + 1
                }
            }
        }
        return cost.toDouble() / n
    }

    /**
     * 未命中查找成本
     */
    fun missSearchCost(): Double {
        var cost = 0
        var i = 0
        var startIndex = -1
        while (i != startIndex) {
            val key = keys[i]
            if (startIndex == -1) {
                if (key == null) {
                    startIndex = i
                    cost++
                    i++
                } else {
                    i++
                }
            } else {
                if (key == null) {
                    cost++
                    i = (i + 1) % m
                } else {
                    var count = 0
                    while (i != startIndex && keys[i] != null) {
                        count++
                        i = (i + 1) % m
                    }
                    //等差数列求和，第一项为2
                    cost += sumOfArithmeticSequence(count, a1 = 2)
                }
            }
        }
        //这里除以m而不是n，因为未命中键的hash值可能是m个索引中的任意一个
        return cost.toDouble() / m
    }

    /**
     * 等差数列求和
     * [n]为总数，[a1]为初始值，[d]为公差
     */
    private fun sumOfArithmeticSequence(n: Int, a1: Int = 1, d: Int = 1): Int {
        require(n > 0)
        return n * a1 + n * (n - 1) * d / 2
    }
}

fun main() {
    val repeatTimes = 1000
    //自动扩容机制会导致m=256,n=128，命中查找平均所需探测次数为1.5，未命中平均所需探测次数为2.5
    val size = 128
    //使用random(Int.MAX_VALUE)来保证hash值随机
    val maxKey = Int.MAX_VALUE

    var hitCost = 0.0
    var missCost = 0.0
    repeat(repeatTimes) {
        val st = SearchCostLinearProbingHashST<Int, Int>()
        while (st.size() < size) {
            st.put(random(maxKey), 0)
        }
        hitCost += st.hitSearchCost()
        missCost += st.missSearchCost()
        if (it == 0) {
            println(st.joinToString())
            println("hitSearchCost =${formatDouble(st.hitSearchCost(), 2)}")
            println("missSearchCost=${formatDouble(st.missSearchCost(), 2)}")
            println("...")
        }
    }
    println("average hitSearchCost=${formatDouble(hitCost / repeatTimes, 2)}")
    println("average missSearchCost=${formatDouble(missCost / repeatTimes, 2)}")
}