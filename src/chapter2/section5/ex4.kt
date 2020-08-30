package chapter2.section5

import chapter2.section1.checkAscOrder
import chapter2.section3.quickSort
import extensions.random
import extensions.shuffle
import extensions.spendTimeMillis

/**
 * 实现一个方法String[] dedup(String[] a)，返回一个有序的a[]，并删去其中重复的元素
 *
 * 解：复制一个新数组，对新数组用快速排序排序，
 * 遍历数组，统计有多少个需要删除的重复元素（有三个元素相同，删除两个保留一个）
 * 创建一个新数组，大小等于原数组大小减去需要删除的重复元素数量
 * 再次遍历数组，复制原数组中的值到新数组中，当a[i]==a[i-1]时直接跳过，遍历结束时正好填满新数组
 */
fun < T : Comparable<T>> Array<T>.dedup(): Array<T> {
    val copyArray = this.copyOf()
    quickSort(copyArray)
    var delCount = 0
    for (i in 1 until copyArray.size) {
        if (copyArray[i] == copyArray[i - 1]) {
            delCount++
        }
    }
    if (delCount == 0) return copyArray
    val copyArray2 = copyArray.copyOfRange(0, copyArray.size - delCount)
    //从后向前遍历可以重复利用delCount变量，也可以减少部分循环次数
    for (i in copyArray.size - 1 downTo 1) {
        if (copyArray[i] == copyArray[i - 1]) {
            delCount--
            //第一个重复元素前面的元素不需要重新赋值
            if (delCount == 0) break
        } else {
            copyArray2[i - delCount] = copyArray[i]
        }
    }
    return copyArray2
}

fun main() {
    val size = 100_0000
    val array = Array(size) { it.toString() }
    array.shuffle()
    repeat(size / 10) {
        array[random(size)] = random(size).toString()
    }
    var result: Array<String> = emptyArray()
    val time = spendTimeMillis {
        result = array.dedup()
    }
    val isAscOrder = result.checkAscOrder()
    var hasDup = false
    for (i in 1 until result.size) {
        if (result[i] == result[i - 1]) {
            hasDup = true
            break
        }
    }
    println("size=$size resultSize=${result.size} isAscOrder=${isAscOrder} hasDup=$hasDup time=$time ms")
}