package chapter1.section1

import extensions.inputPrompt
import extensions.readAllInts

//删除排序后的重复元素，因为数组是有序的，所以删除重复元素比较简单
fun ex28_RemoveDuplicates(array: IntArray): IntArray {
    if (array.size <= 1) return array
    val result = mutableListOf<Int>()
    var startIndex = 0
    var endIndex = 1
    do {
        /* 这里的删除重复元素是指元素重复三次，删除两个保留一个
        如果需要把重复元素全部删除，将下面代码中的result.add(array[startIndex])替换为
        if (endIndex - startIndex == 1) {
            result.add(array[startIndex])
        }
        */
        if (endIndex >= array.size) {
            result.add(array[startIndex])
            break
        }
        if (array[startIndex] == array[endIndex]) {
            endIndex++
        } else {
            result.add(array[startIndex])
            startIndex = endIndex
            endIndex++
        }
    } while (true)

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