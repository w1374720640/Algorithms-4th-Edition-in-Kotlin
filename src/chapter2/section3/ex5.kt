package chapter2.section3

import chapter2.section1.checkAscOrder
import chapter2.swap
import extensions.randomBoolean
import extensions.spendTimeMillis

/**
 * 给出一段代码，将已知只有两种主键值的数组排序
 *
 * 解：以数组的第一个值A为基准，找到第一个不等于A的值，判断是比A大还是比A小
 * 如果比A大，从左向右遍历数组，找到一个比A大的值，从右向左遍历，找到一个等于A的值，交换两个值，直到遍历结束
 * 如果比A小，从右向左遍历数组，找到一个比A小的值，再从左向右遍历，找到一个等于A的值，交换两个值，直到遍历结束
 * 只需要完整遍历一次数组，所以时间复杂度为N
 */
fun <T : Comparable<T>> ex5(array: Array<T>) {
    if (array.size <= 1) return
    val firstValue = array[0]
    var compareWithFirst = 0
    for (i in 1 until array.size) {
        val result = array[i].compareTo(firstValue)
        if (result != 0) {
            compareWithFirst = result
            break
        }
    }
    when {
        compareWithFirst < 0 -> ex5LowerThanFirst(array, firstValue)
        compareWithFirst > 0 -> ex5GreaterThanFirst(array, firstValue)
    }
}

fun <T : Comparable<T>> ex5LowerThanFirst(array: Array<T>, first: T) {
    var leftIndex = -1
    var rightIndex = array.size
    while (true) {
        while (array[++leftIndex] < first) {
            if (leftIndex == array.size - 1) break
        }
        while (array[--rightIndex] == first) {
            if (rightIndex == 0) break
        }
        if (leftIndex >= rightIndex) break
        array.swap(leftIndex, rightIndex)
    }
}

fun <T : Comparable<T>> ex5GreaterThanFirst(array: Array<T>, first: T) {
    var leftIndex = -1
    var rightIndex = array.size
    while (true) {
        while (array[++leftIndex] == first) {
            if (leftIndex == array.size - 1) break
        }
        while (array[--rightIndex] > first) {
            if (rightIndex == 0) break
        }
        if (leftIndex >= rightIndex) break
        array.swap(leftIndex, rightIndex)
    }
}

fun main() {
    val size = 1000_0000
    val array = Array(size) { if (randomBoolean()) 0 else 1 }
    val time = spendTimeMillis {
        ex5(array)
    }
    val isAscOrder = array.checkAscOrder()
    println("isAscOrder=$isAscOrder time=$time ms")
}