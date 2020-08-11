package chapter2.section3

import chapter2.ArrayInitialState
import chapter2.compare
import chapter2.section1.cornerCases
import chapter2.section1.insertionSort
import chapter2.sortMethodsCompare
import chapter2.swap
import extensions.random
import extensions.readInt

/**
 * 优化练习2.3.22
 * 选择三组，每组三个元素，分别取三组元素的中位数，然后取三个中位数的中位数作为切分元素，且在排序小数组时切换到插入排序
 */
fun <T : Comparable<T>> ex23(array: Array<T>) {
    ex23(array, 0, array.size - 1)
}

fun <T : Comparable<T>> ex23(array: Array<T>, start: Int, end: Int) {
    if (start >= end) return
    if (end - start < 15) {
        insertionSort(array, start, end)
        return
    }
    var p = start
    var q = end + 1
    var i = p
    var j = end
    val value = getMedian(array, start, end)
    while (true) {
        val compareLeft = array.compare(i, value)
        when {
            compareLeft > 0 -> array.swap(i, j--)
            compareLeft < 0 -> i++
            p == j -> {
                p++
                i++
            }
            else -> array.swap(i++, p++)
        }
        if (i > j) break
        val compareRight = array.compare(j, value)
        when {
            compareRight > 0 -> j--
            compareRight < 0 -> array.swap(i++, j)
            j == q - 1 -> {
                j--
                q--
            }
            else -> array.swap(j--, --q)
        }
        if (i > j) break
    }
    if (i > p) {
        while (p > start) {
            array.swap(--p, j--)
        }
    } else {
        j = start - 1
    }
    if (j < q - 1) {
        while (q <= end) {
            array.swap(q++, i++)
        }
    } else {
        i = end + 1
    }
    ex23(array, start, j)
    ex23(array, i, end)
}

private fun <T : Comparable<T>> getMedian(array: Array<T>, start: Int, end: Int): T {
    require(end - start + 1 >= 9)
    val size = (end - start + 1) / 3
    val medianArray = array.copyOfRange(0, 3)
    repeat(3) {
        val low = start + size * it
        val high = start + size * (it + 1) - 1
        val index = random(low, high - 2)
        medianArray[it] = midOf(array[index], array[index + 1], array[index + 2])
    }
    return midOf(medianArray[0], medianArray[1], medianArray[2])
}

fun main() {
    cornerCases(::ex23)

    val size = readInt("size: ")
    val ordinal = readInt("array initial state(0~5): ")
    val state = chapter2.enumValueOf<ArrayInitialState>(ordinal)
    val sortMethods: Array<Pair<String, (Array<Double>) -> Unit>> = arrayOf(
            "quickSort3Select" to ::quickSort3Select,
            "ex23" to ::ex23
    )
    sortMethodsCompare(sortMethods, 10, size, state)
}