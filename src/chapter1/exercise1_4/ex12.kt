package chapter1.exercise1_4

/**
 * 打印两个有序数组中的所有公共元素
 */
fun ex12(array1: IntArray, array2: IntArray) {
    var index1 = 0
    var index2 = 0
    while (index1 < array1.size && index2 < array2.size) {
        val value = array1[index1++]
        while (index1 < array1.size && array1[index1] == value) {
            index1++
        }
        while (index2 < array2.size && array2[index2] <= value) {
            index2++
        }
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