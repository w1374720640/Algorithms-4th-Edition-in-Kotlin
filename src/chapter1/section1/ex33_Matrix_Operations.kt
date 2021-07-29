package chapter1.section1

/**
 * 矩阵库
 */
class Matrix {

    /**
     * 向量点乘
     * 向量的点乘，也叫向量的内积、数量积，对两个向量执行点乘运算，就是对这两个向量对应位一一相乘之后求和的操作，点乘的结果是一个标量。
     * 对于向量a和向量b  a=[a1,a2,...an]   b=[b1,b2,...bn]  a和b的点积公式为 a·b=a1*b1+a2*b2+...+an*bn
     */
    fun dot(x: Array<Int>, y: Array<Int>): Int {
        require(x.size == y.size) { "向量点乘要求两个向量的行列数相同，x.size=${x.size} y.size=${y.size}" }
        require(x.isNotEmpty())
        var result = 0
        for (i in x.indices) {
            result += x[i] * y[i]
        }
        return result
    }

    /**
     * 矩阵和矩阵之积
     * 若A为m行n列的矩阵，B为n行p列的矩阵，则A和B的乘积（A·B）是一个m行p列的矩阵
     * A·B第i行j列的值为A的第i行分别和B的第j列乘积的和
     * A·B的行数与A相同，列数与B相同
     */
    fun mult(a: Array<Array<Int>>, b: Array<Array<Int>>): Array<Array<Int>> {
        require(a.isNotEmpty() && a[0].isNotEmpty())
        require(a[0].size == b.size) { "A的列数必须等于B的行数才可以相乘，a[0].size=${a[0].size} b.size=${b.size}" }
        val result = Array(a.size) { Array(b[0].size) { 0 } }
        for (i in result.indices) {
            for (j in result[0].indices) {
                for (k in a[0].indices) {
                    result[i][j] += a[i][k] * b[k][j]
                }
            }
        }
        return result
    }

    /**
     * 转置矩阵
     * 把m行n列的矩阵转换为n行m列的矩阵，行和列互换（相当于二维数组交换行列）
     */
    fun transpose(a: Array<Array<Int>>): Array<Array<Int>> {
        require(a.isNotEmpty() && a[0].isNotEmpty())
        val b = Array(a[0].size) { Array(a.size) { 0 } }
        for (i in a.indices) {
            for (j in a[0].indices) {
                b[j][i] = a[i][j]
            }
        }
        return b
    }

    /**
     * 矩阵和向量之积
     * 长度为n的向量可以表示为n行1列的矩阵
     */
    fun mult(a: Array<Array<Int>>, x: Array<Int>): Array<Array<Int>> {
        val matrix = Array(x.size) { arrayOf(0) }
        for (i in x.indices) {
            matrix[i][0] = x[i]
        }
        return mult(a, matrix)
    }

    /**
     * 向量和矩阵之积
     * 长度为n的向量可以表示为n行1列的矩阵
     */
    fun mult(y: Array<Int>, a: Array<Array<Int>>): Array<Array<Int>> {
        val matrix = Array(y.size) { arrayOf(0) }
        for (i in y.indices) {
            matrix[i][0] = y[i]
        }
        return mult(matrix, a)
    }
}

fun main() {
    val matrix = Matrix()

    println("-----------------向量点乘------------------")
    val x1 = arrayOf(1, 2, 3)
    val y1 = arrayOf(4, 5, 6)
    println("x=${x1.joinToString()}")
    println("y=${y1.joinToString()}")
    val result1 = matrix.dot(x1, y1)
    println("result=${result1}")
    println()

    println("----------------矩阵和矩阵之积-------------------")
    val a2 = arrayOf(arrayOf(1, 2, 3), arrayOf(4, 5, 6))
    val b2 = arrayOf(arrayOf(7, 8), arrayOf(9, 10), arrayOf(11, 12))
    a2.forEach {
        println(it.joinToString())
    }
    println()
    b2.forEach {
        println(it.joinToString())
    }
    println()
    val result2 = matrix.mult(a2, b2)
    result2.forEach {
        println(it.joinToString())
    }
    println()

    println("----------------转置矩阵-------------------")
    val a3 = arrayOf(arrayOf(1, 2, 3), arrayOf(4, 5, 6))
    a3.forEach { println(it.joinToString()) }
    println()
    val result3 = matrix.transpose(a3)
    result3.forEach { println(it.joinToString()) }
    println()

    println("---------------矩阵和向量之积--------------------")
    val a4 = arrayOf(arrayOf(1, 2, 3), arrayOf(4, 5, 6))
    val b4 = arrayOf(7, 8, 9)
    a4.forEach { println(it.joinToString()) }
    println()
    b4.forEach { println(it) }
    println()
    val result4 = matrix.mult(a4, b4)
    result4.forEach { println(it.joinToString()) }
    println()

    println("---------------向量和矩阵之积--------------------")
    val a5 = arrayOf(1, 2, 3)
    val b5 = arrayOf(arrayOf(4, 5, 6, 7))
    a5.forEach { println(it) }
    println()
    b5.forEach { println(it.joinToString()) }
    println()
    val result5 = matrix.mult(a5, b5)
    result5.forEach {
        println(it.joinToString())
    }
}