package chapter5.section1

import chapter2.swap
import extensions.shuffle

/**
 * 三向字符串快速排序
 */
object Quick3String {
    private val M = 15

    fun sort(array: Array<String>) {
        array.shuffle()
        sort(array, 0, array.size - 1, 0)
    }

    private fun sort(array: Array<String>, low: Int, high: Int, d: Int) {
        if (low >= high) return
        if (high - low < M) {
            insertionSort(array, low, high, d)
            return
        }
        val base = charAt(array[low], d)
        var i = low + 1
        var j = low
        var k = high
        while (i <= k) {
            val value = charAt(array[i], d)
            when {
                value > base -> array.swap(i, k--)
                value < base -> array.swap(i++, j++)
                else -> i++
            }
        }
        sort(array, low, j - 1, d)
        if (base >= 0) sort(array, j, k, d + 1)
        sort(array, k + 1, high, d)
    }

    private fun charAt(string: String, d: Int): Int {
        return if (d < string.length) string[d].toInt() else -1
    }

    private fun insertionSort(array: Array<String>, low: Int, high: Int, d: Int) {
        for (i in low + 1..high) {
            for (j in i downTo low + 1) {
                if (less(array, j, j - 1, d)) {
                    array.swap(j, j - 1)
                } else break
            }
        }
    }

    /**
     * 比较字符串指定位置之后字符的大小，Java中substring()方法时间复杂度为常数
     */
    private fun less(array: Array<String>, i: Int, j: Int, d: Int): Boolean {
        if (i == j) return false
        return array[i].substring(d) < array[j].substring(d)
    }
}

fun getQuick3StringData() = arrayOf(
        "edu.princeton.cs",
        "com.apple",
        "edu.princeton.cs",
        "com.cnn",
        "com.google",
        "edu.uva.cs",
        "edu.princeton.cs",
        "edu.princeton.cs.www",
        "edu.uva.cs",
        "edu.uva.cs",
        "edu.uva.cs",
        "com.adobe",
        "edu.princeton.ee"
)

fun main() {
    val array = getQuick3StringData()
    Quick3String.sort(array)
    println(array.joinToString(separator = "\n"))
}