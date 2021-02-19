package chapter5.section1

import chapter2.swap
import extensions.formatStringLength

/**
 * 实现能够处理变长字符串的低位优先的字符串排序算法
 *
 * 解：对于变长字符串的低位优先排序来说，若后d个字符相同，则长度短的排在前面（bcd在abcd前面）
 * 基础实现只需要将MSD中charAt方法改为从右侧开始索引即可
 * 为了使用插入排序，需要自定义一个字符串比较器，从右向左比较
 */
object ex9 {
    private const val R = 256
    private const val M = 10

    fun sort(array: Array<String>) {
        val aux = Array(array.size) { "" }
        sort(array, aux, 0, array.size - 1, 0)
    }

    private fun sort(array: Array<String>, aux: Array<String>, low: Int, high: Int, d: Int) {
        if (high - low < M) {
            // 切换插入排序
            insertionSort(array, low, high, d)
        } else {
            val count = IntArray(R + 2)
            for (i in low..high) {
                count[charAt(array[i], d) + 2]++
            }
            for (i in 1..R + 1) {
                count[i] += count[i - 1]
            }
            for (i in low..high) {
                val index = charAt(array[i], d) + 1
                aux[count[index]] = array[i]
                count[index]++
            }
            for (i in low..high) {
                array[i] = aux[i - low]
            }

            for (i in 0 until R) {
                sort(array, aux, low + count[i], low + count[i + 1] - 1, d + 1)
            }
        }
    }

    private fun charAt(string: String, d: Int): Int {
        // 这里和MSD逻辑不同
        return if (d < string.length) string[string.length - 1 - d].toInt() else -1
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

    private fun less(array: Array<String>, i: Int, j: Int, d: Int): Boolean {
        if (i == j) return false
        val first = array[i]
        val second = array[j]
        // 使用自定义的比较器而不是String默认的比较器，注意裁切范围
        return comparator.compare(first.substring(0, first.length - d), second.substring(0, second.length - d)) < 0
    }

    /**
     * 自定义的String比较器，从右向左比较，bcd排在abcd前面
     */
    private val comparator = Comparator<String> { o1, o2 ->
        var i = 0
        var result = 0
        while (result == 0) {
            if (i >= o1.length && i >= o2.length) {
                break
            }
            if (i >= o1.length) {
                result = -1
                break
            }
            if (i >= o2.length) {
                result = 1
                break
            }
            result = o1[o1.length - 1 - i].compareTo(o2[o2.length - 1 - i])
            i++
        }
        result
    }
}

fun main() {
    val msdData = getMSDData()
    ex9.sort(msdData)
    println(msdData.joinToString(separator = "\n") { formatStringLength(it, 10) })
}