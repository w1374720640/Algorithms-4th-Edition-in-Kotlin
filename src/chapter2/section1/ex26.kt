package chapter2.section1

import extensions.inputPrompt
import extensions.random
import extensions.readInt
import extensions.spendTimeMillis

/**
 * 比较使用原始类型Int数组的插入排序和自动装箱、拆箱的Integer版本的性能差距
 */
fun ex26(array: IntArray) {
    for (i in 1 until array.size) {
        for (j in i downTo 1) {
            if (array[j] >= array[j - 1]) break
            val temp = array[j]
            array[j] = array[j - 1]
            array[j - 1] = temp
        }
    }
}

fun main() {
    inputPrompt()
    val size = readInt("size: ")
    var intTime = 0L
    repeat(10) {
        val array = IntArray(size) { random(Int.MAX_VALUE) }
        intTime += spendTimeMillis {
            ex26(array)
        }
    }
    intTime /= 10
    println("Int array spend $intTime ms")

    var integerTime = 0L
    repeat(10) {
        val array = Array(size) { random(Int.MAX_VALUE) }
        integerTime += spendTimeMillis {
            insertionSort(array)
        }
    }
    integerTime /= 10
    println("Integer array spend $integerTime ms")
}