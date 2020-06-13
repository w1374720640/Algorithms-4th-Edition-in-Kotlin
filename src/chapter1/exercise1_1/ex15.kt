package chapter1.exercise1_1

//返回一个大小为M的数组，第i个元素的值为参数数组source中i出现的次数，原理类似于Java中的BitSet
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
    val M = 4
    val source = arrayOf(4, 5, 6, 3, 2, 6, 0, 43, 2, 3, 5, 6, 55, 3, 1, 0, 4)
    println("source=${source.joinToString { it.toString() }}")
    val result = ex15(M, source)
    println("result=${result.joinToString { it.toString() }}")
    println("source.size=${source.size},result total=${result.sum()}")
}