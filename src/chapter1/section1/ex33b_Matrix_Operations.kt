package chapter1.section1

//矩阵和矩阵之积
//若A为m行n列的矩阵，B为n行p列的矩阵，则A和B的乘积（A·B）是一个m行p列的矩阵
//A·B第i行j列的值为A的第i行分别和B的第j列乘积的和
//A·B的行数与A相同，列数与B相同
fun ex33b_Matrix_Operations(a: Array<Array<Int>>, b: Array<Array<Int>>): Array<Array<Int>> {
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

fun main() {
    val a = arrayOf(arrayOf(1, 2, 3), arrayOf(4, 5, 6))
    val b = arrayOf(arrayOf(7, 8), arrayOf(9, 10), arrayOf(11, 12))
    a.forEach {
        println(it.joinToString())
    }
    println()
    b.forEach {
        println(it.joinToString())
    }
    println()
    val result = ex33b_Matrix_Operations(a, b)
    result.forEach {
        println(it.joinToString())
    }
}