package chapter1.section4

import extensions.inputPrompt
import extensions.readAllInts
import extensions.readInt

/**
 * 无重复值之中的二分查找
 * 用二分查找实现StaticSETofInts（请见表1.2.15），保证contains()的运行时间为~lgR，其中R为参数数组中不同整数的数量。
 *
 * 解：创建对象时，先对数组排序，再创建一个可变长度的列表，遍历排序后的数组
 * 当数组值和列表的最后一位不同时将数据添加到列表中，最后将列表转化为新数组
 * 在contains方法中使用普通的二分查找即可
 * 新数组中不包含重复元素，数组的长度为R，所以contains方法的运行时间为~logR
 */
class StaticsSETofInts(array: IntArray) {
    private val orderedArray: IntArray

    init {
        val sortArray = IntArray(array.size)
        array.forEachIndexed { index, value ->
            sortArray[index] = value
        }
        sortArray.sort()
        val list = mutableListOf<Int>()
        sortArray.forEach {
            if (list.isEmpty()) {
                list.add(it)
            } else if (it != list.last()) {
                list.add(it)
            }
        }
        orderedArray = list.toIntArray()
    }

    fun contains(key: Int): Boolean {
        var start = 0
        var end = orderedArray.size - 1
        while (start <= end) {
            val mid = (start + end) / 2
            when {
                orderedArray[mid] == key -> return true
                orderedArray[mid] < key -> start = mid + 1
                orderedArray[mid] > key -> end = mid - 1
            }
        }
        return false
    }
}

fun main() {
    inputPrompt()
    val key = readInt()
    val array = readAllInts()
    val set = StaticsSETofInts(array)
    println(array.joinToString())
    println("Array ${if (set.contains(key)) "contains" else "dose not contain"} $key")
}