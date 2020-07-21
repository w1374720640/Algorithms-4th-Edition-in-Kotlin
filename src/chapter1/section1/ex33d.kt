package chapter1.section1

//矩阵和向量之积
//长度为n的向量可以表示为n行1列的矩阵
fun ex33d(a: Array<Array<Int>>, x: Array<Int>): Array<Array<Int>> {
    val matrix = Array(x.size) { arrayOf(0) }
    for (i in x.indices) {
        matrix[i][0] = x[i]
    }
    return ex33b(a, matrix)
}

fun main() {
    val a = arrayOf(arrayOf(1, 2, 3), arrayOf(4, 5, 6))
    val b = arrayOf(7, 8, 9)
    a.forEach { println(it.joinToString()) }
    println()
    b.forEach { println(it) }
    println()
    val result = ex33d(a, b)
    result.forEach { println(it.joinToString()) }
}