package chapter2.section2

import chapter2.ArrayInitialState
import chapter2.getDoubleArray
import chapter2.enumValueOf
import extensions.delayExit
import extensions.inputPrompt
import extensions.readInt
import extensions.readLong
import kotlin.math.min

/**
 * 归并排序（自底向上）
 */
inline fun <reified T : Comparable<T>> buttonUpMergeSort(originalArray: Array<T>) {
    if (originalArray.size <= 1) return
    val extraArray = arrayOfNulls<T>(originalArray.size)
    var step = 1 //1 2 4 8 16...
    while (true) {
        //依次合并[0,1],[2,3],[4,5]... [0,3],[4,7],[8,11]... [0,7],[8,15],[16,23]...
        //边界条件是：保持mid在有效范围内，end超出有效范围后取最后一个有效值
        for (start in 0..originalArray.size - step step 2 * step) {
            merge(originalArray, extraArray, start, start + step - 1, min(start + 2 * step - 1, originalArray.size - 1))
        }
        if (step * 2 >= originalArray.size) {
            break
        } else {
            step *= 2
        }
    }
}

fun main() {
    inputPrompt()
    val size = readInt("size: ")
    val delay = readLong("delay time millis: ")
    val ordinal = readInt("array initial state(0~4): ")
    val state = enumValueOf<ArrayInitialState>(ordinal)
    val array = getDoubleArray(size, state)
    showMergeSortProcess(array, ::buttonUpMergeSort, delay)
    delayExit()
}