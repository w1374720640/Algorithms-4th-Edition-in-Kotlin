package chapter2.exericise1_1

import chapter2.*
import extensions.delayExit
import extensions.inputPrompt
import extensions.readInt
import extensions.readLong

/**
 * 选择排序
 */
fun <T : Comparable<T>> selectSort(array: Array<T>) {
    for (i in array.indices) {
        var minIndex = i
        for (j in i + 1 until array.size) {
            if (array.less(j, minIndex)) minIndex = j
        }
        if (minIndex != i) array.swap(i, minIndex)
    }
}

/**
 * 通用的排序方法主函数模板，用于显示数组中数据的交换过程
 */
fun sortingMethodMainFunTemplate(name: String, sortFun: (Array<Double>) -> Unit, showComparisonProcess: Boolean = true) {
    inputPrompt()
    val size = readInt("size: ")
    //两次绘制的间隔
    val delay = readLong("delay time millis: ")
    //设置初始数组是完全随机、完全升序、完全降序、接近升序、接近降序这五种状态
    val state = readInt("array initial state(0~4): ")
    val enumState = ArrayInitialState.getEnumByState(state)
    println("Array initial state: ${enumState.name}")
    val array = getDoubleArray(size, enumState)
    val swapTimes = showSortingProcess(array, sortFun, delay, showComparisonProcess)
    println("$name swap $swapTimes times")
    delayExit()
}

fun main() {
    sortingMethodMainFunTemplate("Select sort", ::selectSort)
}