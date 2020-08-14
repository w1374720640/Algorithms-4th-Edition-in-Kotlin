package chapter2.section3

import edu.princeton.cs.algs4.StdRandom
import extensions.formatDouble
import extensions.random

/**
 * 螺丝和螺帽
 * 假设有N个螺丝和螺帽混在一堆，你需要快速将它们配对
 * 一个螺丝只会匹配一个螺帽，一个螺帽也只会匹配一个螺丝
 * 你可以试着把一个螺丝和螺帽拧在一起看看谁大了，但不能直接比较两个螺丝或两个螺帽
 * 给出一个解决这个问题的有效方法
 *
 * 解：设螺丝放在数组A中，螺帽放在数组B中，A和B的大小相等，且无重复值
 * 用数组A中的第一个螺丝作为基准，以快速排序的切分方法将数组B分为小于、等于、大于基准的三部分
 * （需要注意的是，标准快速排序的切分方法只能切分成小于等于和大于等于两部分，需要手动记录等于值，循环结束后手动调整位置）
 * 再以数组B中等于第一个螺丝的螺帽作为基准，将数组A切分为三部分，这时作为基准的螺丝和螺帽在A、B中的位置相同
 * 用递归的形式，在左半边和右半边分别取第一个螺丝将螺帽切分，在用螺帽将螺丝切分，直到数组A和数组B完全有序
 * 需要对两个数组进行快速排序，复杂度为2NlgN ~NlgN
 */
fun ex15(bolts: Array<Bolt>, nuts: Array<Nut>) {
    require(bolts.size == nuts.size)
    ex15(bolts, nuts, 0, bolts.size - 1)
}

fun ex15(bolts: Array<Bolt>, nuts: Array<Nut>, start: Int, end: Int) {
    if (start >= end) return
    val partitionNutIndex = ex15PartitionNuts(nuts, bolts[start], start, end)
    val partitionBoltIndex = ex15PartitionBolts(bolts, nuts[partitionNutIndex], start, end)
    check(partitionNutIndex == partitionBoltIndex)
    ex15(bolts, nuts, start, partitionNutIndex - 1)
    ex15(bolts, nuts, partitionNutIndex + 1, end)
}

fun ex15PartitionBolts(bolts: Array<Bolt>, nut: Nut, start: Int, end: Int): Int {
    var lowerThan = start - 1
    var greaterThan = end + 1
    var equalIndex = -1
    while (true) {
        while (bolts[++lowerThan] < nut) {
            if (lowerThan >= end) break
        }
        while (bolts[--greaterThan] > nut) {
            if (greaterThan <= start) break
        }
        if (lowerThan >= greaterThan) break
        val temp = bolts[lowerThan]
        bolts[lowerThan] = bolts[greaterThan]
        bolts[greaterThan] = temp

        if (bolts[lowerThan].compareTo(nut) == 0) {
            equalIndex = lowerThan
        }
        if (bolts[greaterThan].compareTo(nut) == 0) {
            equalIndex = greaterThan
        }
    }
    //最后将相等的值放到中间
    if (equalIndex > greaterThan) {
        greaterThan++
    }
    if (equalIndex >= 0 && equalIndex != greaterThan) {
        val temp = bolts[equalIndex]
        bolts[equalIndex] = bolts[greaterThan]
        bolts[greaterThan] = temp
    }
    return greaterThan
}

fun ex15PartitionNuts(nuts: Array<Nut>, bolt: Bolt, start: Int, end: Int): Int {
    var lowerThan = start - 1
    var greaterThan = end + 1
    var equalIndex = -1
    while (true) {
        while (nuts[++lowerThan] < bolt) {
            if (lowerThan >= end) break
        }
        while (nuts[--greaterThan] > bolt) {
            if (greaterThan <= start) break
        }
        if (lowerThan >= greaterThan) break
        val temp = nuts[lowerThan]
        nuts[lowerThan] = nuts[greaterThan]
        nuts[greaterThan] = temp
        if (nuts[lowerThan].compareTo(bolt) == 0) {
            equalIndex = lowerThan
        }
        if (nuts[greaterThan].compareTo(bolt) == 0) {
            equalIndex = greaterThan
        }
    }
    if (equalIndex > greaterThan) {
        greaterThan++
    }
    if (equalIndex >= 0 && equalIndex != greaterThan) {
        val temp = nuts[equalIndex]
        nuts[equalIndex] = nuts[greaterThan]
        nuts[greaterThan] = temp
    }
    return greaterThan
}

/**
 * 螺丝
 * 只能和螺帽比较大小，而不能和其他螺丝比较大小
 */
class Bolt(val size: Double) : Comparable<Nut> {
    override fun compareTo(other: Nut): Int {
        return when {
            size < other.size -> -1
            size > other.size -> 1
            else -> 0
        }
    }
}

/**
 * 螺帽
 * 只能和螺丝比较大小，而不能和其他螺帽比较大小
 */
class Nut(val size: Double) : Comparable<Bolt> {
    override fun compareTo(other: Bolt): Int {
        return when {
            size < other.size -> -1
            size > other.size -> 1
            else -> 0
        }
    }
}

fun main() {
    val size = 50
    val values = Array(size) { random() }
    val bolts = Array(size) { Bolt(values[it]) }
    val nuts = Array(size) { Nut(values[it]) }
    StdRandom.shuffle(bolts)
    StdRandom.shuffle(nuts)
    println("before:")
    println("bolts: ${bolts.joinToString(limit = 100) { formatDouble(it.size, 3) }}")
    println("nuts : ${nuts.joinToString(limit = 100) { formatDouble(it.size, 3) }}")
    ex15(bolts, nuts)
    println("after:")
    println("bolts: ${bolts.joinToString(limit = 100) { formatDouble(it.size, 3) }}")
    println("nuts : ${nuts.joinToString(limit = 100) { formatDouble(it.size, 3) }}")
}
