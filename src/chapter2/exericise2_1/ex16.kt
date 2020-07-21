package chapter2.exericise2_1

import chapter2.ArrayInitialState
import chapter2.getDoubleArray
import extensions.formatDouble
import java.util.*

/**
 * 在方法内用排序方法对数组排序，如果排序成功且原数组中没有被添加或删除过数据，则返回true，否则返回false
 * 不要假设排序方法一定用swap()方法排序，适用于任何排序方法
 */
fun <T : Comparable<T>> ex16(array: Array<T>, sortFun: (Array<T>) -> Unit): Boolean {
    val map = mutableMapOf<T, Int>()
    array.forEach {
        map[it] = map.getOrDefault(it, 0) + 1
    }
    sortFun(array)
    for (i in 0 until array.size - 1) {
        if (array[i] > array[i + 1]) return false
    }
    array.forEach {
        val value = map[it]
        if (value == null || value == 0) {
            return false
        } else {
            if (value == 1) {
                map.remove(it)
            } else {
                map[it] = value - 1
            }
        }
    }
    return map.isEmpty()
}

fun main() {
    val array = getDoubleArray(1000, ArrayInitialState.RANDOM)
    val result = ex16(array) { Arrays.sort(it) }
    println("Check result = $result")
    println(array.joinToString(limit = 100) { formatDouble(it, 4) })
}