package chapter2.section1

import chapter2.*
import extensions.inputPrompt
import extensions.readInt

/**
 * 在插入排序中，先找到最小元素放到最左边，可以规避内层循环的边界判断
 */
fun <T : Comparable<T>> ex24(array: Array<T>) {
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
    val ordinal = readInt("array initial state(0~4): ")
    val state = ArrayInitialState::class.getEnumByOrdinal(ordinal)
    println("Array initial state: ${state.name}")
    val sortMethods: Array<Pair<String, (Array<Double>) -> Unit>> = arrayOf(
            "Insertion Sort" to ::insertionSort,
            "ex24" to ::ex24
    )
    sortMethodsCompare(sortMethods, times, size, state)
}