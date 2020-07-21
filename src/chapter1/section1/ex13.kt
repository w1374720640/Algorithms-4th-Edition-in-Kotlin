package chapter1.section1

import extensions.formatInt

//交换二维数组的行列
fun ex13(array: Array<Array<Int>>): Array<Array<Int>> {
    val row = array.size
    require(row > 0)
    val column = array[0].size
    array.forEach {
        require(column == it.size)
    }
    //上面都是检查输入条件
    val newArray = Array(column) {
        Array(row) { 0 }
    }
    newArray.forEachIndexed { outerIndex, outerArray ->
        for (i in outerArray.indices) {
            outerArray[i] = array[i][outerIndex]
        }
    }
    return newArray
}

fun main() {
    val array = arrayOf(arrayOf(1, 2, 3, 4), arrayOf(5, 6, 7, 8), arrayOf(9, 10, 11, 12), arrayOf(13, 14, 15, 16))
    array.forEach {
        println(it.joinToString { formatInt(it, 4) })
    }
    println("--------------------------")
    val newArray = ex13(array)
    newArray.forEach {
        println(it.joinToString { formatInt(it, 4) })
    }
}