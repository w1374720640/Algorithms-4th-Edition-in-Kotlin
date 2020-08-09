package chapter2.section3

import chapter2.compare
import chapter2.section1.cornerCases
import chapter2.section1.doubleGrowthTest
import chapter2.section1.insertionSort
import chapter2.swap
import extensions.random
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
    var i = start
    var j = end
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
    val indexArray = IntArray(5)
    //将取值范围等分为5份，分别在5份内取随机值（范围小于15时用插入排序不会调用这个方法）
    val size = (end - start + 1) / 5
    repeat(5) {
        val index = random(size) + size * it + start
        indexArray[it] = index
    }
    if (array[indexArray[2]] < array[indexArray[1]]) {
        indexArray.swap(1, 2)
    }
    if (array[indexArray[4]] < array[indexArray[3]]) {
        indexArray.swap(3, 4)
    }
    if (array[indexArray[3]] < array[indexArray[1]]) {
        indexArray.swap(1, 3)
        indexArray.swap(2, 4)
    }
    indexArray.swap(0, 1)
    if (array[indexArray[2]] < array[indexArray[1]]) {
        indexArray.swap(1, 2)
    }
    if (array[indexArray[3]] < array[indexArray[1]]) {
        indexArray.swap(1, 3)
        indexArray.swap(2, 4)
    }
    val midIndex = if (array[indexArray[2]] < array[indexArray[3]]) {
        2
    } else {
        3
    }
    //让最后一个值大于中间值，第一个值等于中间值，可以省略partition方法内循环中的判断
    array.swap(indexArray[4], end)
    array.swap(indexArray[midIndex], start)
}

fun IntArray.swap(i: Int, j: Int) {
    val temp = this[i]
    this[i] = this[j]
    this[j] = temp
}

fun main() {
    cornerCases(::quickSort5Select)

    println("quickSortWithOriginalArray:")
    doubleGrowthTest(1000_0000, ::quickSortWithOriginalArray) { N -> N * log2(N.toDouble()) }
    println("quickSort3Select:")
    doubleGrowthTest(1000_0000, ::quickSort3Select) { N -> N * log2(N.toDouble()) }
    println("quickSort5Select:")
    doubleGrowthTest(1000_0000, ::quickSort5Select) { N -> N * log2(N.toDouble()) }
}