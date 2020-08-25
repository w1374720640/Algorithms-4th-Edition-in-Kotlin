package chapter1.section1

//向量和矩阵之积
//长度为n的向量可以表示为n行1列的矩阵
fun ex33e_Matrix_Operations(y: Array<Int>, a: Array<Array<Int>>): Array<Array<Int>> {
    val matrix = Array(y.size) { arrayOf(0) }
    for (i in y.indices) {
        matrix[i][0] = y[i]
    }
    return ex33b_Matrix_Operations(matrix, a)
}

fun main() {
    val a = arrayOf(1,2,3)
    val b = arrayOf(arrayOf(4,5,6,7))
    a.forEach { println(it) }
    println()
    b.forEach { println(it.joinToString()) }
    println()
    val result = ex33e_Matrix_Operations(a, b)
    result.forEach {
        println(it.joinToString())
    }
}