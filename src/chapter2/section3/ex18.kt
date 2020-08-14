package chapter2.section3

import chapter2.*
import chapter2.section1.cornerCases
import edu.princeton.cs.algs4.StdRandom

/**
 * 三取样切分
 * 从数组内取三个值，中值放在第一位，最大值放在最后一位，最小值放在中间，可以省略while循环中的边界判断
 */
fun <T : Comparable<T>> quickSort3Select(array: Array<T>) {
    StdRandom.shuffle(array)
    quickSort3Select(array, 0, array.size - 1)
}

fun <T : Comparable<T>> quickSort3SelectNotShuffle(array: Array<T>) {
    quickSort3Select(array, 0, array.size - 1)
}

fun <T : Comparable<T>> quickSort3Select(array: Array<T>, start: Int, end: Int) {
    if (start >= end) return
    val mid = partition3Select(array, start, end)
    quickSort3Select(array, start, mid - 1)
    quickSort3Select(array, mid + 1, end)
}

fun <T : Comparable<T>> partition3Select(array: Array<T>, start: Int, end: Int): Int {
    val mid = (start + end) / 2
    if (array.less(start, mid)) array.swap(start, mid)
    if (array.less(end, mid)) array.swap(end, mid)
    if (array.less(end, start)) array.swap(end, start)

    var i = start
    var j = end + 1
    while (true) {
        while (array.compare(++i, start) < 0) {
        }
        while (array.compare(--j, start) > 0) {
        }
        if (i >= j) break
        array.swap(i, j)
    }
    if (j != start) array.swap(start, j)
    return j
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
    //检查排序方法是否正确
    cornerCases(::quickSort3SelectNotShuffle)
//    displaySortingProcessTemplate("Quick Sort 3 Select", ::quickSort3Select)

    val sortMethods: Array<Pair<String, (Array<Double>) -> Unit>> = arrayOf(
            "quickSortNotShuffle" to ::quickSortNotShuffle,
            "quickSort3SelectNotShuffle" to ::quickSort3SelectNotShuffle
    )
    sortMethodsCompare(sortMethods, 10, 100_0000, ArrayInitialState.RANDOM)
}