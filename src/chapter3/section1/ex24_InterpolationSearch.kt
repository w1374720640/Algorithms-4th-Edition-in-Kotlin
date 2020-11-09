package chapter3.section1

import extensions.random
import extensions.spendTimeMillis

/**
 * 插值法查找
 * 假设符号表的键支持算术操作（例如，他们可能是Double或者Integer类型的值）
 * 编写一个二分查找来模拟查字典的行为，例如当单词的首字母在字母表的开头时，我们也会在字典的前半部分进行查找
 * 具体来说，假设klo为符号表的第一个键，khi为符号表的最后一个键，当要查找kx时，
 * 先和 ((kx-klo)/(khi-klo))*(hi-lo)+lo 进行比较，而非取中间值，
 * 用SearchCompare调用FrequencyCounter来比较你的实现和BinarySearchST的性能
 *
 * 解：继承BinarySearchST类，重写rank()方法，在用二分法查找时，不使用中间元素对比，按比例查找对应位置的元素
 * 原书中的公式为kx的在符号表中的相对位置，还需要转换成具体索引才能使用
 */
class InterpolationSearch<V : Any> : BinarySearchST<Int, V>() {
    override fun rank(key: Int): Int {
        var start = 0
        var end = size - 1
        while (start <= end) {
            val index = if (start == end) {
                start
            } else {
                //和start、end两处的key值比较，而不是直接和start、end比较，计算当前位置百分比
                val ratio = (key - keys[start] as Int).toDouble() / (keys[end] as Int - keys[start] as Int)
                //key超出范围时取边界值比较
                when {
                    ratio < 0 -> start
                    ratio > 1 -> end
                    else -> (ratio * (end - start)).toInt() + start
                }
            }
            val result = keys[index]!!.compareTo(key)
            when {
                result > 0 -> end = index - 1
                result < 0 -> start = index + 1
                else -> return index
            }
        }
        return start
    }
}

fun main() {
    testOrderedST(InterpolationSearch())

    //当数据源key值连续时，InterpolationSearch查找效率更高，否则效率不如二分查找
    val size = 10_0000
    val maxRange = 10_0000
//    val size = 100_0000
//    val maxRange = 1_0000
    val array = Array(size) { random(maxRange) }
    var result1: Pair<Int, Int>? = null
    val time1 = spendTimeMillis {
        result1 = frequencyCounter(array.copyOf(), BinarySearchST())
    }
    println("BinarySearchST      maxFrequency:[${result1!!.first}, ${result1!!.second}] time=$time1 ms")
    var result2: Pair<Int, Int>? = null
    val time2 = spendTimeMillis {
        result2 = frequencyCounter(array.copyOf(), InterpolationSearch())
    }
    println("InterpolationSearch maxFrequency:[${result2!!.first}, ${result2!!.second}] time=$time2 ms")
}