package chapter5.section1

import chapter2.section1.insertionSort
import extensions.shuffle

/**
 * 亚线性排序
 * 编写一个处理Int值的排序算法，遍历数组两遍，第一遍根据所有键的高16位进行低位优先的排序，第二遍进行插入排序。
 *
 * 解：对高16位进行低位优先排序后，插入排序会被分割为多个独立的区间，每个区间最多有2^16=65536个不同的值
 * 插入排序在小范围内的性能不错
 */
fun ex15_SublinearSort(array: Array<Int>) {
    ex15_Hight16BitSort(array)
    insertionSort(array)
}

/**
 * 对Int的高16位进行低位优先排序
 *
 * 如何判断第d位的值：将1左移d-1位，与原值按位与运算，若值为0，则第d位为0，值非0则第d位为1
 * 对于第16~31位，0比1小，0排在1前面，对于第32位符号位来说，1排在0前面，因为1代表负数
 */
fun ex15_Hight16BitSort(array: Array<Int>) {
    val aux = Array(array.size) { 0 }
    // 使用两个变量代替count数组
    var x = 0
    var y = 0
    for (d in 16..32) {
        val base = 1 shl (d - 1)
        x = 0
        y = 0
        // 统计0和非0的总数
        array.forEach {
            val value = it and base
            if (d == 32) {
                // 对于第32位符号位来说，1比0小，1排在前面
                if (value == 0) y++ else x++
            } else {
                // 对于其他位来说，0比1小，0排在前面
                if (value == 0) x++ else y++
            }
        }
        // 将总数转换为索引
        y = x
        x = 0
        for (i in array.indices) {
            val value = array[i] and base
            if (d == 32) {
                // 第32位来说，y代表0的索引，x代表1的索引
                if (value == 0) {
                    aux[y++] = array[i]
                } else {
                    aux[x++] = array[i]
                }
            } else {
                if (value == 0) {
                    aux[x++] = array[i]
                } else {
                    aux[y++] = array[i]
                }
            }
        }
        for (i in array.indices) {
            array[i] = aux[i]
        }
    }
}

fun main() {
    // kotlin中如果第32位为1则会被推导为Long类型，而不是负数，Java中可以推导为int类型的负数
    val array = arrayOf<Int>(
            0xFFFFFFFF.toInt(), // -1
            0xFFFFFFFE.toInt(), // -2
            0x80000000.toInt(), // Int.MIN_VALUE -2147483648
            0xF000FFFF.toInt(),
            0xFF0FFFFF.toInt(),
            0xFF1FFFFF.toInt(),
            0xFF2FFFFF.toInt(),
            0xFF2FFFF0.toInt(),
            0x7FFFFFFF, // Int.MAX_VALUE 2147483647
            0x7CFFFFFF,
            0x7CFFFFF0,
            0x10000000,
            0x00000000, // 0
            0x00000001, // 1
            0x00000002  // 2
    )
    // 随机打乱数组
    array.shuffle()
    ex15_SublinearSort(array)
    println(array.joinToString(separator = "\n"))
}