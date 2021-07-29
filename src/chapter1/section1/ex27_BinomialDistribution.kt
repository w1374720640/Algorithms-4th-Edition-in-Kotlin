package chapter1.section1

import extensions.inputPrompt
import extensions.readDouble
import extensions.readInt

/**
 * 二项分布
 * 估计用以下代码计算binomial(100, 50, 0.25)将会产生的递归调用次数：
 * 将已经计算过的值保存在数组中并给出一个更好的实现。
 */
fun ex27a_BinomialDistribution(N: Int, k: Int, p: Double): Int {
    var count = 0
    fun binomial(N: Int, k: Int, p: Double): Double {
        count++
        if (N == 0 && k == 0) return 1.0
        if (N < 0 || k < 0) return 0.0
        return (1.0 - p) * binomial(N - 1, k, p) + p * binomial(N - 1, k - 1, p)
    }
    binomial(N, k, p)
    return count
}

//优化后的函数
fun ex27b_BinomialDistribution(N: Int, k: Int, p: Double): Int {
    val array = Array(N + 1) { Array(k + 1) { -1.0 } }
    var count = 0
    fun bin(N: Int, k: Int, p: Double): Double {
        count++
        when {
            N == 0 && k == 0 -> return 1.0
            N < 0 || k < 0 -> return 0.0
            else -> if (array[N][k] == -1.0) {
                array[N][k] = (1.0 - p) * bin(N - 1, k, p) + p * bin(N - 1, k - 1, p)
            }
        }
        return array[N][k]
    }
    bin(N, k, p)
    return count
}

fun main() {
    inputPrompt()
    val count = ex27b_BinomialDistribution(readInt("N: "), readInt("k: "), readDouble("p: "))
    println("count=${count}")
}