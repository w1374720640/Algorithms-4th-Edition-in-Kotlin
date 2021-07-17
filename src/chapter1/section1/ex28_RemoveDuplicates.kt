package chapter1.section1

import extensions.inputPrompt
import extensions.readAllInts

/**
 * 删除重复元素
 * 修改BinarySearch类中的测试用例来删去排序之后白名单中的所有重复元素。
 *
 * 解：这里的删除重复元素是指元素重复三次，删除两个保留一个
 */
fun ex28_RemoveDuplicates(array: IntArray): IntArray {
    if (array.size <= 1) return array
    val result = mutableListOf<Int>()
    var value = array[0]
    result.add(value)
    for (i in 1 until array.size) {
        if (array[i] == value) continue
        value = array[i]
        result.add(value)
    }
    return result.toIntArray()
}

fun main() {
    inputPrompt()
    val array = readAllInts()
    array.sort()
    println("sort array=${array.joinToString()}")
    val result = ex28_RemoveDuplicates(array)
    println("result=${result.joinToString()}")
}