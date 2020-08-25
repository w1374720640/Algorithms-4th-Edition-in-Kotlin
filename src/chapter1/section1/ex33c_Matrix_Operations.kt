package chapter1.section1

//转置矩阵
//把m行n列的矩阵转换为n行m列的矩阵，行和列互换（相当于二维数组交换行列）
fun ex33c_Matrix_Operations(a: Array<Array<Int>>): Array<Array<Int>> {
    require(a.isNotEmpty() && a[0].isNotEmpty())
    val b = Array(a[0].size) { Array(a.size) { 0 } }
    for (i in a.indices) {
        for (j in a[0].indices) {
            b[j][i] = a[i][j]
        }
    }
    return b
}

fun main() {
    val a = arrayOf(arrayOf(1, 2, 3), arrayOf(4, 5, 6))
    a.forEach { println(it.joinToString()) }
    println()
    val b = ex33c_Matrix_Operations(a)
    b.forEach { println(it.joinToString()) }
}