package chapter2.section5

import chapter1.section1.binarySearch
import extensions.spendTimeMillis

/**
 * 从标准输入读入一列单词并打印出其中所有由两个单词组成的组合词
 * 例如，如果输入的单词为after、thought和afterthought，那么afterthought就是一个组合词
 *
 * 解：先对数组排序，然后从数组中取两个数相加（区分正反顺序），使用二分查找判断结果是否存在于数组中
 * 排序的复杂度为NlgN，从N中取两个数排列的可能性为N*(N-1)，二分查找的复杂度为lgN
 * 所以总时间复杂度为NlgN+N*(N-1)*lgN ~N²lgN
 */
fun ex2a(array: Array<String>): List<String> {
    if (array.size < 3) return emptyList()
    array.sort()
    val list = mutableListOf<String>()
    for (i in 0..array.size - 2) {
        for (j in i until array.size) {
            val index1 = binarySearch(array[i] + array[j], array)
            val index2 = binarySearch(array[j] + array[i], array)
            if (index1 != -1) {
                list.add(array[index1])
            }
            if (index2 != -1) {
                list.add(array[index2])
            }
        }
    }
    return list
}

/**
 * 对String的封装，先比较长度，再依次比较每个字符
 */
private class LengthFirstString(val item: String) : Comparable<LengthFirstString> {
    override fun compareTo(other: LengthFirstString): Int {
        return when {
            item === other.item -> 0
            item.length < other.item.length -> -1
            item.length > other.item.length -> 1
            else -> item.compareTo(other.item)
        }
    }

    fun length(): Int = item.length

    operator fun plus(other: LengthFirstString): LengthFirstString {
        return LengthFirstString(item + other.item)
    }

    override fun toString(): String {
        return item
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is LengthFirstString) return false
        if (this.item === other.item) return true
        return item == other.item
    }

    override fun hashCode(): Int {
        return item.hashCode()
    }
}

/**
 * 对上面方法的优化
 * 自定义数据类型，比较大小时先比较字符串长度，长度相同时才依次比较每个字符
 * 对自定义的数据类型排序，找出字符串的最大长度，两个字符串相加长度超过最大长度时直接返回
 * 具体对效率的提升依赖数据源，最坏情况：
 * 有一个长度特长的字符串，超过其他任意两个字符相加的长度，内循环中根据长度快速返回的代码失效
 */
fun ex2b(array: Array<String>): List<String> {
    if (array.size < 3) return emptyList()
    val newArray = Array(array.size) { LengthFirstString(array[it]) }
    newArray.sort()
    val maxLength = newArray.last().length()
    val list = mutableListOf<String>()
    for (i in 0..newArray.size - 2) {
        for (j in i until newArray.size) {
            //当两个字符串长度相加超长时，内循环中剩余的任意一个值和外循环的值相加都超长
            if (newArray[i].length() + newArray[j].length() > maxLength) break

            //还可以用HashSet替代二分查找，不过需要更多的额外空间
            val index1 = binarySearch(newArray[i] + newArray[j], newArray)
            val index2 = binarySearch(newArray[j] + newArray[i], newArray)
            if (index1 != -1) {
                list.add(newArray[index1].item)
            }
            if (index2 != -1) {
                list.add(newArray[index2].item)
            }
        }
    }
    return list
}

fun main() {
    val array1 = Array(10000) { it.toString() }
//    array1[array1.size - 1] = "10000000"
    var list1: List<String> = emptyList()
    val time1 = spendTimeMillis {
        list1 = ex2a(array1)
    }
    println("ex2a: time=${time1} ms size=${list1.size} top10=${list1.joinToString(limit = 20)}")

    val array2 = Array(10000) { it.toString() }
    //如果放开下面这个注释，会导致内循环中根据长度快速返回的代码失效，是最坏情况
//    array2[array2.size - 1] = "10000000"
    var list2: List<String> = emptyList()
    val time2 = spendTimeMillis {
        list2 = ex2b(array2)
    }
    println("ex2b: time=${time2} ms size=${list2.size} top10=${list2.joinToString(limit = 20)}")
}