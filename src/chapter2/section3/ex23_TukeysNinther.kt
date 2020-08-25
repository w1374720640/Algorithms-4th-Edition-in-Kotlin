package chapter2.section3

import chapter2.ArrayInitialState
import chapter2.compare
import chapter2.section1.cornerCases
import chapter2.section1.insertionSort
import chapter2.sortMethodsCompare
import chapter2.swap

/**
 * 优化练习2.3.22
 * 选择三组，每组三个元素，分别取三组元素的中位数，然后取三个中位数的中位数作为切分元素，且在排序小数组时切换到插入排序
 */
fun <T : Comparable<T>> ex23_TukeysNinther(array: Array<T>) {
    ex23_TukeysNinther(array, 0, array.size - 1)
}

fun <T : Comparable<T>> ex23_TukeysNinther(array: Array<T>, start: Int, end: Int) {
    if (start >= end) return
    if (end - start < 9) {
        insertionSort(array, start, end)
        return
    }
    var p = start
    var q = end + 1
    var i = p
    var j = end
    val value = getMedian(array, start, end)
    while (true) {
        while (true) {
            val compare = array.compare(i, value)
            if (compare == 0) {
                array.swap(i++, p++)
            } else if (compare > 0) {
                array.swap(i, j--)
                break
            } else {
                i++
            }
            if (i > j) break
        }
        if (i > j) break
        while (true) {
            val compare = array.compare(j, value)
            if (compare == 0) {
                array.swap(j--, --q)
            } else if (compare > 0) {
                j--
            } else {
                array.swap(i++, j)
                break
            }
            if (i > j) break
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
    ex23_TukeysNinther(array, start, j)
    ex23_TukeysNinther(array, i, end)
}

private fun <T : Comparable<T>> getMedian(array: Array<T>, start: Int, end: Int): T {
    require(end - start + 1 >= 9)
    val eps = (end - start + 1) / 9
    val mid = (start + end) / 2
    val value1 = midOf(array[start], array[start + eps], array[start + eps * 2])
    val value2 = midOf(array[mid - eps], array[mid], array[mid + eps])
    val value3 = midOf(array[end - 2 * eps], array[end - eps], array[end])
    return midOf(value1, value2, value3)
}

/**
 * 返回三个数大小的中间值
 */
fun <T : Comparable<T>> midOf(a: T, b: T, c: T): T {
    return when {
        (a >= b && a <= c) || (a <= b && a >= c) -> a
        (b >= a && b <= c) || (b >= c && b <= a) -> b
        else -> c
    }
}

fun main() {
    cornerCases(::ex23_TukeysNinther)

    val sortMethods: Array<Pair<String, (Array<Double>) -> Unit>> = arrayOf(
            "quickSortNotShuffle" to ::quickSortNotShuffle,
            "quickSort3Way" to ::quickSort3Way,
            "quickSortFast3Way" to ::quickSortFast3Way,
            "ex23" to ::ex23_TukeysNinther
    )
    println("ArrayInitialState.REPEAT:")
    sortMethodsCompare(sortMethods, 10, 100_0000, ArrayInitialState.REPEAT)
    println("ArrayInitialState.RANDOM:")
    sortMethodsCompare(sortMethods, 10, 100_0000, ArrayInitialState.RANDOM)
}