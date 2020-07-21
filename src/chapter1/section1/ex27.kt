package chapter1.section1

import extensions.inputPrompt
import extensions.readDouble
import extensions.readInt

//估算binomial(100, 50, 0.25)产生的递归调用次数
fun ex27a(N: Int, k: Int, p: Double): Int {
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

//练习1.1.27优化后的函数
//这是练习1.1中我觉得最难理解的一题了
fun ex27b(N: Int, k: Int, p: Double): Int {
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
    val count = ex27b(readInt(), readInt(), readDouble())
    println("count=${count}")
}