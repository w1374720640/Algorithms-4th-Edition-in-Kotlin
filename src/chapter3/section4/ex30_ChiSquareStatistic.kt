package chapter3.section4

import extensions.formatDouble
import extensions.random
import kotlin.math.sqrt

/**
 * 卡方值（chi-square statistic）
 * 为SeparateChainingHashST添加一个方法来计算散列表的X²
 * 对于大小为M并含有N个元素的散列表，这个值的定义为：
 * X² = (M / N)((f₀ - N / M)² + (f₁ - N / M)² + ... + (f(m-1) - N / M)²)
 * 其中，f(i)为散列值为i的键的数量
 * 这个统计数据是检测我们的散列函数产生的随机值是否满足假设的一种方法
 * 如果满足，对于N>cM，这个值落在 M - sqrt(M) 和 M + sqrt(M) 之间的概率为 1 - 1/c
 *
 * 解：c为一个小常数，大小等于N/M（向下取整）
 * 随机生成N个键插入到Hash表中，计算卡方值，判断是否在 M - sqrt(M) 和 M + sqrt(M) 之间
 * 重复多次，计算卡方值在范围内的实际概率，与期望概率 1 - 1/c 对比
 */
fun <K : Any, V : Any> SeparateChainingHashST<K, V>.chiSquareStatistic(): Double {
    val array = IntArray(m)
    keys().forEach {
        array[hash(it)]++
    }
    var sum = 0.0
    val mn = m.toDouble() / n
    val nm = n.toDouble() / m
    array.forEach {
        sum += (it - nm) * (it - nm)
    }
    return mn * sum
}

fun main() {
    val size = 100
    val maxKey = Int.MAX_VALUE
    val repeatTimes = 1000

    val N = size
    var M = 4
    //根据N的大小，计算在只插入不删除的情况下M的大小
    while (N > 8 * M) {
        M *= 2
    }
    val c = N / M
    val min = M - sqrt(M.toDouble())
    val max = M + sqrt(M.toDouble())
    println("N=$N  M=$M  c=$c")
    println("min=${formatDouble(min, 2)}")
    println("max=${formatDouble(max, 2)}")
    println("Expected probability: (1-1/c) = ${formatDouble(1 - 1.0 / c, 2)}")

    var count = 0
    repeat(repeatTimes) {
        val st = SeparateChainingHashST<Int, Int>()
        while (st.size() < size) {
            st.put(random(maxKey), 0)
        }
        val value = st.chiSquareStatistic()
        if (value in min..max) {
            count++
        }
    }
    println("Actual probability: ${formatDouble(count.toDouble() / repeatTimes, 2)}")

}