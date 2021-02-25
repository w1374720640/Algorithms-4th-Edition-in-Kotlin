package chapter5.section1

import chapter2.swap

/**
 * 原地键索引计数法
 * 实现一个仅使用常数级别的额外空间的键索引计数法。
 * 证明你的实现是稳定的或者提供一个反例。
 *
 * 解：创建两个大小为R+1的整型数组，存放键的起始索引，
 * 设每个键的起始索引为i（会不断增加），键的起点为start（不变）
 * 遍历原始数组，数组的索引为j，
 * 若j小于start，则将索引i和j位置的值交换，i自增一，j不变，继续循环
 * 若j大于等于start，且j等于i，则i和j都自增一，继续循环
 * 若j大于等于start，且j不等于i，则i不变，j自增一，继续循环
 * 当数组遍历结束时，所有的键都在相应的范围内
 *
 * 需要的额外空间和R的大小有关，相对数组大小N来说是常数
 * 该实现是不稳定的，例如在getStudentGroup()方法返回的数据中，Harris和Martin的key相等，但顺序被交换了
 */
fun ex17_InPlaceKeyIndexedCounting(array: Array<Key>, R: Int) {
    val count = IntArray(R + 1)
    array.forEach {
        count[it.key() + 1]++
    }
    for (i in 1 until count.size) {
        count[i] += count[i - 1]
    }
    val start = count.copyOf()

    var i = 0
    while (i < array.size) {
        val key = array[i].key()
        if (i < start[key]) {
            array.swap(i, count[key])
            count[key]++
        } else {
            if (i == count[key]) {
                count[key]++
            }
            i++
        }
    }
}

fun main() {
    val group = getStudentGroup()
    val array = group.first
    val R = group.second
    ex17_InPlaceKeyIndexedCounting(array, R)
    println(array.joinToString(separator = "\n"))
}