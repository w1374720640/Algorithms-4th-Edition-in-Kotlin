package chapter2.section1

import chapter2.getDoubleArray
import extensions.formatDouble
import java.util.*

/**
 * 验证
 * 编写一个check()方法，调用sort()对任意数组排序。
 * 如果排序成功而且数组中的所有对象均没有被修改则返回true，否则返回false。
 * 不要假设sort()只能通过exch()来一栋数据，可以信任并使用Array.sort()。
 *
 * 解：排序前先统计每个元素出现的次数，排序后再对于每个元素出现的次数是否发生变化，无变化则所有对象均没有被修改。
 */
fun <T : Comparable<T>> ex16_Certification(array: Array<T>, sortFun: (Array<T>) -> Unit): Boolean {
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
    val array = getDoubleArray(1000)
    val result = ex16_Certification(array) { Arrays.sort(it) }
    println("Check result = $result")
    println(array.joinToString(limit = 100) { formatDouble(it, 4) })
}