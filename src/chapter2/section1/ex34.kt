package chapter2.section1

import chapter2.ArrayInitialState
import chapter2.getDoubleArray
import extensions.randomBoolean

/**
 * 极端情况
 * 调用sort方法对实际应用中可能出现的困难或极端情况的数组进行排序
 * 比如，数组可能是有序的，或是逆序的，数组的所有主键相同，数组的主键只有两种值，大小为0或为1的数组
 */
fun cornerCases(sortMethod: (Array<Double>) -> Unit) {
    sortMethod(arrayOf())
    sortMethod(arrayOf(1.0))
    repeat(10) {
        val array = getDoubleArray(2)
        sortMethod(array)
        assert(array.checkAscOrder())
    }

    val ascArray = getDoubleArray(100, ArrayInitialState.ASC)
    sortMethod(ascArray)
    assert(ascArray.checkAscOrder())

    val descArray = getDoubleArray(100, ArrayInitialState.DESC)
    sortMethod(descArray)
    assert(descArray.checkAscOrder())

    val sameValueArray = Array(100) { 1.0 }
    sortMethod(sameValueArray)
    assert(sameValueArray.checkAscOrder())

    val twoValuesArray = Array(100) { if (randomBoolean()) 0.0 else 1.0 }
    sortMethod(twoValuesArray)
    twoValuesArray.checkAscOrder()
}

/**
 * 检查数组是否按正序排列
 */
fun <T : Comparable<T>> Array<T>.checkAscOrder(): Boolean {
    for (i in 0..this.size - 2) {
        if (this[i] > this[i + 1]) return false
    }
    return true
}

/**
 * 检查数组是否按逆序排列
 */
fun <T : Comparable<T>> Array<T>.checkDescOrder(): Boolean {
    for (i in 0..this.size - 2) {
        if (this[i] < this[i + 1]) return false
    }
    return true
}

fun main() {
    val sortMethodList = listOf<(Array<Double>) -> Unit>(::selectSort, ::bubbleSort, ::insertSort, ::shellSort)
    sortMethodList.forEach {
        cornerCases(it)
    }
    println("All sorting methods pass corner cases test")
}