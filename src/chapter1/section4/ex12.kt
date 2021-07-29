package chapter1.section4

/**
 * 编写一个程序，有序打印给定的两个有序数组（含有N个int值）中的所有公共元素，程序在最坏情况下所需的运行时间应该和N成正比。
 */
fun ex12(array1: IntArray, array2: IntArray) {
    var index1 = 0
    var index2 = 0
    while (index1 < array1.size && index2 < array2.size) {
        val value = array1[index1++]
        //让数组1的索引前进到第一个不等于value的位置
        while (index1 < array1.size && array1[index1] == value) {
            index1++
        }
        //让数组2的索引前进到第一个大于value的位置
        while (index2 < array2.size && array2[index2] <= value) {
            index2++
        }
        //数组1和数组2当前索引减一的位置值是否相同，相同则为公共元素
        if (index2 > 0 && array1[index1 - 1] == array2[index2 - 1]) {
            print("${array1[index1 - 1]} ")
        }
    }
}

fun main() {
    val array1 = intArrayOf(1, 3, 5, 5, 7, 7, 9)
    val array2 = intArrayOf(3, 3, 6, 7)
    ex12(array1, array2)
}