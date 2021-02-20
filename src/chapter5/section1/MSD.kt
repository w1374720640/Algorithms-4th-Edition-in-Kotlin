package chapter5.section1

import chapter2.swap

/**
 * 高位优先的字符串排序
 */
object MSD {
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
            // 因为字符索引从0开始，所以R需要加2而不是1（LSD针对特定字符串排序，索引不会为0）
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
                // 注意这里aux的索引要减去low的值
                array[i] = aux[i - low]
            }

            // 递归对R个子区间排序
            for (i in 0 until R) {
                // count[0]表示已经到达结尾的字符串数量，不需要继续排序，所以从low+count[i]开始排序
                sort(array, aux, low + count[i], low + count[i + 1] - 1, d + 1)
            }
        }
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

fun getMSDData() = arrayOf(
        "she",
        "sells",
        "seashells",
        "by",
        "the",
        "seashore",
        "the",
        "shells",
        "she",
        "sells",
        "are",
        "surely",
        "seashells"
)

fun main() {
    val array = getMSDData()
    MSD.sort(array)
    println(array.joinToString(separator = "\n"))
}