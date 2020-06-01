package capter1.exercise1_1

import extensions.inputPrompt
import extensions.readAllInts
import extensions.readInt

//在一个有序数组中分别查找大于、小于、等于key值的数量
//因为是有序数组，所以二分法是最快的（在数据量比较大的情况下）
//查找小于key的值时，即使已经找到等于key的值，继续在左侧查找，直到找到左侧第一个不等于key的值
//也可以在找到等于key的值时，向左右两侧遍历数组，但是当一个值重复多次时可能会很慢
fun ex29(key: Int, array: IntArray) {
    array.sort()
    fun numberLessThanKey(key: Int, array: IntArray): Int {
        var startIndex = 0
        var endIndex = array.size - 1
        do {
            //如果数组中不存在要查找的键，则左半边是小于键的值
            if (startIndex > endIndex) return endIndex + 1
            val midIndex = (startIndex + endIndex) / 2
            when {
                //和正常的二分法区别是，当key=array[midIndex]时，执行key<array[midIndex]相同的逻辑
                key <= array[midIndex] -> endIndex = midIndex - 1
                key > array[midIndex] -> startIndex = midIndex + 1
            }
        } while (true)
    }

    fun numberGreaterThanKey(key: Int, array: IntArray): Int {
        var startIndex = 0
        var endIndex = array.size - 1
        do {
            //如果数组中不存在要查找的键，则右半边是大于键的值
            if (startIndex > endIndex) return array.size - (endIndex + 1)
            val midIndex = (startIndex + endIndex) / 2
            when {
                //和正常的二分法区别是，当key=array[midIndex]时，执行key>array[midIndex]相同的逻辑
                key < array[midIndex] -> endIndex = midIndex - 1
                key >= array[midIndex] -> startIndex = midIndex + 1
            }
        } while (true)
    }
    println("key=${key} array=${array.joinToString()}")
    val lessNumber = numberLessThanKey(key, array)
    val greaterNumber = numberGreaterThanKey(key, array)
    println("array.size=${array.size} lessNumber=${lessNumber} equalNumber=${array.size - lessNumber - greaterNumber} greaterNumber=${greaterNumber}")
}

fun main() {
    inputPrompt()
    ex29(readInt(), readAllInts())
}