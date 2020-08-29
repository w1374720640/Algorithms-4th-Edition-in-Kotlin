package chapter2.section2

import chapter1.section1.binarySearch
import extensions.random

/**
 * 给定三个列表，每个列表包含N个名字，编写一个线性对数级别的算法来判定三份列表中是否含有公共的名字
 * 如果有，返回第一个被找到的这种名字
 *
 * 解：对第二个和第三个列表用归并排序，时间复杂度为NlgN
 * 遍历第一个列表，在第二个列表和第三个列表中用二分查找判断值是否存在，如果都存在，则返回该值
 * 遍历复杂度为N，二分查找复杂度为lgN，遍历+查找复杂度为NlgN
 * 所以总复杂度为线性对数级的NlgN
 */
fun ex21_Triplicates(list1: Array<String>, list2: Array<String>, list3: Array<String>): String? {
    topDownMergeSort(list2)
    topDownMergeSort(list3)

    list1.forEach {
        val index2 = binarySearch(it, list2)
        if (index2 != -1) {
            val index3 = binarySearch(it, list3)
            if (index3 != -1) return it
        }
    }
    return null
}

fun main() {
    val size = 10
    //对比数字形式的字符串时，9比11大，因为第一个元素不同时，不会继续比较第二个元素
    println("(The string '9' is greater than '11')")
    val list1 = Array(size) { random(size * 5).toString() }
    val list2 = Array(size) { random(size * 5).toString() }
    val list3 = Array(size) { random(size * 5).toString() }
    val name = ex21_Triplicates(list1, list2, list3)
    //第一个数组为无序，第二和第三个数组已排序
    if (name == null) {
        println("Not found")
        println(list1.joinToString(limit = 100))
        println(list2.joinToString(limit = 100))
        println(list3.joinToString(limit = 100))
    } else {
        println("Has found, name = $name")
        println(list1.joinToString(limit = 100))
        println(list2.joinToString(limit = 100))
        println(list3.joinToString(limit = 100))
    }
    println()
}