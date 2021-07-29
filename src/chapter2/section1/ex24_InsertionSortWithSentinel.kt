package chapter2.section1

import chapter2.*
import extensions.inputPrompt
import extensions.readInt

/**
 * 插入排序的哨兵
 * 在插入排序的实现中先找出最小的元素并将其置于数组的最左边，这样就能去掉内循环的判断条件j>0。
 * 使用SortCompare来评估这种做法的效果。
 * 注意：这是一种常见的规避边界测试的方法，能够省略判断条件的元素通常被称为哨兵。
 */
fun <T : Comparable<T>> ex24_InsertionSortWithSentinel(array: Array<T>) {
    if (array.isEmpty()) return
    var minIndex = 0
    for (i in array.indices) {
        if (array.less(i, minIndex)) {
            minIndex = i
        }
    }
    if (minIndex != 0) {
        array.swap(0, minIndex)
    }
    for (i in 1 until array.size) {
        //这里不用判断左边界，因为数组中最左边的值最小，会自动跳出内循环
        for (j in i downTo Int.MIN_VALUE) {
            if (!array.less(j, j - 1)) break
            array.swap(j, j - 1)
        }
    }
}

fun main() {
    inputPrompt()
    val times = readInt("repeat times: ")
    val size = readInt("size: ")
    //设置初始数组是完全随机、完全升序、完全降序、接近升序、接近降序这五种状态
    val ordinal = readInt("array initial state(0~5): ")
    val state = enumValueOf<ArrayInitialState>(ordinal)
    println("Array initial state: ${state.name}")
    val sortMethods: Array<Pair<String, (Array<Double>) -> Unit>> = arrayOf(
            "Insertion Sort" to ::insertionSort,
            "ex24" to ::ex24_InsertionSortWithSentinel
    )
    sortMethodsCompare(sortMethods, times, size, state)
}