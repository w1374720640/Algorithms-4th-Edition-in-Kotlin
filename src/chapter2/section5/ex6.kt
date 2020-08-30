package chapter2.section5

import extensions.shuffle

/**
 * 用递归实现select()
 * select()方法用于找到一组数中第k小的元素
 */
fun <T : Comparable<T>> select(array: Array<T>, k: Int): T {
    array.shuffle()
    return select(array, k, 0, array.size - 1)
}

fun <T : Comparable<T>> select(array: Array<T>, k: Int, start: Int, end: Int): T {
    //k必须在有效范围内
    require(k in start..end)
    if (start == end) return array[k]
    val partition = chapter2.section3.partition(array, start, end)
    return when {
        partition == k -> array[partition]
        partition < k -> select(array, k, partition + 1, end)
        else -> select(array, k, start, partition - 1)
    }
}

fun main() {
    val array = Array(10) { it + 100 }
    repeat(10) {
        array.shuffle()
        println("k=$it value=${select(array, it)}")
    }
}