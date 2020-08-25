package chapter2.section3

import chapter2.*
import chapter2.section1.cornerCases
import chapter2.section1.doublingTest
import chapter2.section1.insertionSort
import edu.princeton.cs.algs4.StdRandom
import kotlin.math.log2

/**
 * 五取样切分
 * 实现一种基于随机抽取子数组中5个元素并取中位数进行切分的快速排序
 * 将取样元素放在数组的一侧以保证只有中位数元素参与了切分
 * 运行双倍测试来确定这项改动的效果，并和标准的快速排序以及三取样切分的快速排序进行比较
 * 附加题：找到一种对于任意输入都只需要少于7次比较的五取样算法
 *
 * 附加题解：设五个位置分别为a b c d e
 * 对bc排序，对de排序，比较两次
 * 比较bd，如果d较小，将de放到bc位置，比较一次
 * 交换ab，再排序bc，比较一次
 * 比较bd，如果d较小，将de放到bc位置，比较一次
 * 这时，ab分别是最小值和次小值（没有大小关系，因为交换ab时没有比较大小），e>d>b c>b
 * 比较cd，如果c>d则d为中位数，c<d则c为中位数，比较一次
 * 一共比较6次，对于任意输入都只需要少于7次比较
 */
fun <T : Comparable<T>> quickSort5Select(array: Array<T>) {
    StdRandom.shuffle(array)
    quickSort5Select(array, 0, array.size - 1)
}

fun <T : Comparable<T>> quickSort5SelectNotShuffle(array: Array<T>) {
    quickSort5Select(array, 0, array.size - 1)
}

fun <T : Comparable<T>> quickSort5Select(array: Array<T>, start: Int, end: Int) {
    if (start >= end) return
    if (end - start < 15) {
        insertionSort(array, start, end)
        return
    }
    val mid = partition5Select(array, start, end)
    quickSort5Select(array, start, mid - 1)
    quickSort5Select(array, mid + 1, end)
}

fun <T : Comparable<T>> partition5Select(array: Array<T>, start: Int, end: Int): Int {
    swapStartWith5Mid(array, start, end)
    //前三位数小于等于第一位的值，后两位数大于等于第一位的值
    var i = start + 2
    var j = end - 1
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
 * 在指定范围内随机取5个值，取中值和范围起始值交换，大于中值的一个值和范围的最后一个值交换
 */
private fun <T : Comparable<T>> swapStartWith5Mid(array: Array<T>, start: Int, end: Int) {
    //排序前先打乱，所以可以直接取前五个数
    if (array.less(start + 2, start + 1)) {
        array.swap(start + 1, start + 2)
    }
    if (array.less(start + 4, start + 3)) {
        array.swap(start + 3, start + 4)
    }
    if (array.less(start + 3, start + 1)) {
        array.swap(start + 1, start + 3)
        array.swap(start + 2, start + 4)
    }
    array.swap(start, start + 1)
    if (array.less(start + 2, start + 1)) {
        array.swap(start + 1, start + 2)
    }
    if (array.less(start + 3, start + 1)) {
        array.swap(start + 1, start + 3)
        array.swap(start + 2, start + 4)
    }
    if (array.less(start + 3, start + 2)) {
        array.swap(start + 3, start + 2)
    }
    //中位数和第一个值交换，大于中位数的两个值和取值范围内的最后两个数交换
    array.swap(start, start + 2)
    array.swap(start + 3, end - 1)
    array.swap(start + 4, end)
}

fun main() {
    cornerCases(::quickSort5SelectNotShuffle)
    val sortMethods: Array<Pair<String, (Array<Double>) -> Unit>> = arrayOf(
            "quickSortNotShuffle" to ::quickSortNotShuffle,
            "quickSort3SelectNotShuffle" to ::quickSort3SelectNotShuffle,
            "quickSort5SelectNotShuffle" to ::quickSort5SelectNotShuffle
    )
    sortMethodsCompare(sortMethods, 10, 100_0000, ArrayInitialState.RANDOM)

    println("quickSortNotShuffle:")
    doublingTest(1000_0000, ::quickSortNotShuffle) { N -> N * log2(N.toDouble()) }
    println("quickSort3SelectNotShuffle:")
    doublingTest(1000_0000, ::quickSort3SelectNotShuffle) { N -> N * log2(N.toDouble()) }
    println("quickSort5SelectNotShuffle:")
    doublingTest(1000_0000, ::quickSort5SelectNotShuffle) { N -> N * log2(N.toDouble()) }
}