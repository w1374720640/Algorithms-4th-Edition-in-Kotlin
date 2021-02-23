package chapter5.section1

import chapter2.swap

/**
 * 原地键索引计数法
 * 实现一个仅使用常数级别的额外空间的键索引计数法。
 * 证明你的实现是稳定的或者提供一个反例。
 *
 * 解：创建三个大小为R的整型数组，分别存放每个键的起始索引、键的起点、键的终点
 * 设每个键的起始索引为i（会不断增加），键的起点为low（不变），键的终点为high（不变）
 * 遍历原始数组，数组的索引为j，
 * 若j不在low和high范围内，将索引i和j位置的值交换，i自增一，j不变，继续循环
 * 若j在low和high范围内，且j等于i，则i和j都自增一，继续循环
 * 若j在low和high范围内，且j不等于i，则i不变，j自增一，继续循环
 * 当数组遍历结束时，所有的键都在相应的范围内
 *
 * 需要的额外空间和R的大小有关，相对数组大小N来说是常数
 * 该实现是不稳定的，例如在getStudentGroup()方法返回的数据中，Harris和Martin的key相等，但顺序被交换了
 */
fun ex17_InPlaceKeyIndexedCounting(array: Array<Key>, R: Int) {
    val count = IntArray(R)
    array.forEach {
        count[it.key()]++
    }
    val low = IntArray(R)
    for (i in 1 until R) {
        low[i] = low[i - 1] + count[i - 1]
    }
    val high = IntArray(R)
    for (i in 1 until R) {
        high[i] = count[i] + low[i] - 1
    }
    // 将每个键的总数重新赋值为起始索引
    for (i in 1 until R) {
        count[i] = low[i]
    }

    var i = 0
    while (i < array.size) {
        val key = array[i].key()
        if (i in low[key]..high[key]) {
            if (i == count[key]) {
                count[key]++
            }
            i++
        } else {
            array.swap(i, count[key])
            count[key]++
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