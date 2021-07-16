package chapter1.section1

import extensions.formatInt

/**
 * 编写一段代码，打印出一个M行N列的二维数组的转置
 */
fun ex13(array: Array<Array<Int>>): Array<Array<Int>> {
    // 检查输入数组是否符合转置条件
    val row = array.size
    require(row > 0)
    val column = array[0].size
    array.forEach {
        require(column == it.size)
    }

    // 由于M和N可能不等，所以转置只能创建新数组，而不能原地转置
    val newArray = Array(column) {
        Array(row) { 0 }
    }
    for (i in newArray.indices) {
        val a = newArray[i]
        for (j in a.indices) {
            a[j] = array[j][i]
        }
    }
    return newArray
}

fun main() {
    val array = arrayOf(arrayOf(1, 2, 3, 4), arrayOf(5, 6, 7, 8), arrayOf(9, 10, 11, 12))
    array.forEach {
        println(it.joinToString { formatInt(it, 4) })
    }
    println("--------------------------")
    val newArray = ex13(array)
    newArray.forEach {
        println(it.joinToString { formatInt(it, 4) })
    }
}