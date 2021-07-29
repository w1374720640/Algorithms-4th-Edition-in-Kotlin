package chapter1.section1

/**
 * 编写一个静态方法histogram()，接受一个整型数组a[]和一个整数M为参数并返回一个大小为M的数组，其中第i个元素的值为整数i在参数数组中出现的次数。
 * 如果a[]中的值均在0到M-1之间，返回数组中所有元素之和应该和a.length相等
 */
fun ex15(M: Int, source: Array<Int>): Array<Int> {
    val result = Array(M) { 0 }
    for (i in source) {
        if (i in 0 until M) {
            result[i]++
        }
    }
    return result
}

fun main() {
    val M = 10
    val source = arrayOf(4, 5, 6, 3, 2, 6, 0, 4, 2, 3, 5, 6, 5, 3, 1, 0, 4)
    println("source=${source.joinToString { it.toString() }}")
    val result = ex15(M, source)
    println("result=${result.joinToString { it.toString() }}")
    println("source.size=${source.size},result total=${result.sum()}")
}