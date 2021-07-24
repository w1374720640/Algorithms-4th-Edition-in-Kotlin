package chapter2.section1

import extensions.inputPrompt
import extensions.random
import extensions.readInt
import extensions.spendTimeMillis

/**
 * 原始数据类型
 * 编写一个能够处理int值的插入排序的新版本，比较它和正文中所给出的实现（能够隐式地用自动装箱和拆箱转换Integer值并排序）的性能。
 */
fun ex26_PrimitiveTypes(array: IntArray) {
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
            ex26_PrimitiveTypes(array)
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