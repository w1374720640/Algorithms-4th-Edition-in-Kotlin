package chapter5.section1

import chapter2.swap
import kotlin.math.min

/**
 * 基于字母表的高位优先字符串排序算法
 */
fun alphabetMSDSort(array: Array<String>, alphabet: Alphabet) {
    val aux = Array(array.size) { "" }
    alphabetMSDSort(array, alphabet, aux, 0, array.size - 1, 0)
}

private const val M = 10

private fun alphabetMSDSort(array: Array<String>, alphabet: Alphabet, aux: Array<String>, low: Int, high: Int, d: Int) {
    if (high - low < M) {
        msdInsertionSort(array, alphabet, low, high, d)
        return
    }
    val count = IntArray(alphabet.R() + 2)
    for (i in low..high) {
        count[msdCharAt(array[i], alphabet, d) + 2]++
    }
    for (i in 1 until count.size) {
        count[i] += count[i - 1]
    }
    for (i in low..high) {
        val index = msdCharAt(array[i], alphabet, d) + 1
        aux[count[index] + low] = array[i]
        count[index]++
    }
    for (i in low..high) {
        array[i] = aux[i]
    }

    for (i in 0 until alphabet.R()) {
        alphabetMSDSort(array, alphabet, aux, low + count[i], low + count[i + 1] - 1, d + 1)
    }
}

private fun msdCharAt(string: String, alphabet: Alphabet, d: Int): Int {
    return if (d < string.length) alphabet.toIndex(string[d]) else -1
}

private fun msdInsertionSort(array: Array<String>, alphabet: Alphabet, low: Int, high: Int, d: Int) {
    val msdComparator = MSDComparator(alphabet)
    for (i in low + 1..high) {
        for (j in i downTo low + 1) {
            if (msdLess(array, msdComparator, j, j - 1, d)) {
                array.swap(j, j - 1)
            } else break
        }
    }
}

private fun msdLess(array: Array<String>, msdComparator: MSDComparator, i: Int, j: Int, d: Int): Boolean {
    if (i == j) return false
    val first = array[i]
    val second = array[j]
    return msdComparator.compare(first.substring(d, first.length), second.substring(d, second.length)) < 0
}

private class MSDComparator(private val alphabet: Alphabet) : Comparator<String> {
    override fun compare(o1: String, o2: String): Int {
        for (i in 0 until min(o1.length, o2.length)) {
            val result = msdCharAt(o1, alphabet, i).compareTo(msdCharAt(o2, alphabet, i))
            if (result != 0) return result
        }
        return o1.length - o2.length
    }

}

fun main() {
    val array = getMSDData()
    alphabetMSDSort(array, Alphabet.EXTENDED_ASCII)
    println(array.joinToString(separator = "\n"))
}