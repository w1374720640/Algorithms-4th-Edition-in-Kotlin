package chapter3.section5

import chapter3.section4.LinearProbingHashST
import extensions.formatDouble
import kotlin.math.abs

/**
 * 为SparseVector添加一个sum()方法，接受一个SparseVector对象作为参数并将两者相加的结果返回为一个SparseVector对象
 * 请注意：你需要使用delete()方法来处理向量中的一项变为0的情况（请特别注意精度）
 *
 * 解：向量相加等于对应位置的数相加
 */
class SparseVector(val st: LinearProbingHashST<Int, Double> = LinearProbingHashST()) {

    fun size(): Int {
        return st.size()
    }

    fun put(index: Int, value: Double) {
        st.put(index, value)
    }

    fun get(index: Int): Double {
        return st.get(index) ?: 0.0
    }

    fun dot(array: Array<Double>): Double {
        var sum = 0.0
        for (index in st.keys()) {
            sum += get(index) * array[index]
        }
        return sum
    }

    fun sum(vector: SparseVector): SparseVector {
        val newST = LinearProbingHashST<Int, Double>()
        for (index in st.keys()) {
            newST.put(index, st.get(index)!!)
        }
        for (index in vector.st.keys()) {
            val value = vector.st.get(index)!!
            val sum = (st.get(index) ?: 0.0) + value
            // 浮点数相加可能产生误差，当和的绝对值小于指定精度时，认为和为0
            if (abs(sum) < 0.00000001) {
                newST.delete(index)
            } else {
                newST.put(index, sum)
            }
        }
        return SparseVector(newST)
    }
}

fun main() {
    val a = arrayOf(
            arrayOf(0.0, 0.9, 0.0, 0.0, 0.0),
            arrayOf(0.0, 0.0, 0.36, 0.36, 0.18),
            arrayOf(0.0, 0.0, 0.0, 0.9, 0.0),
            arrayOf(0.9, 0.0, 0.0, 0.0, 0.0),
            arrayOf(0.47, 0.0, 0.47, 0.0, 0.0)
    )
    val b = arrayOf(
            arrayOf(0.1, 0.2, 0.0, 0.4, 0.0),
            arrayOf(0.0, 0.36, -0.36, 0.36, 0.0),
            arrayOf(0.0, 0.0, 0.0, -0.9, 0.0),
            arrayOf(0.1, 0.0, 0.0, 0.0, 0.0),
            arrayOf(0.0, 0.0, -0.47, 0.2, 0.0)
    )
    val x = arrayOf(0.05, 0.04, 0.36, 0.37, 0.19)
    val sparseVectorArrayA = Array(a.size) {
        SparseVector().apply {
            for (i in a[it].indices) {
                if (a[it][i] != 0.0) {
                    put(i, a[it][i])
                }
            }
        }
    }
    val sparseVectorArrayB = Array(b.size) {
        SparseVector().apply {
            for (i in b[it].indices) {
                if (b[it][i] != 0.0) {
                    put(i, b[it][i])
                }
            }
        }
    }
    println("a•x = ")
    for (i in sparseVectorArrayA.indices) {
        println(formatDouble(sparseVectorArrayA[i].dot(x), 4))
    }
    println()
    for (i in sparseVectorArrayA.indices) {
        println("a[${i}]+b[${i}]=")
        val result = sparseVectorArrayA[i].sum(sparseVectorArrayB[i])
        println(result.st.keys().joinToString { "${it}:${formatDouble(result.st.get(it)!!, 3) }"})
    }
}