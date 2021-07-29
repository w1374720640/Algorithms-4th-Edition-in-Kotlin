package chapter1.section1

import extensions.inputPrompt
import extensions.readAllInts
import extensions.readInt

/**
 * 使用1.1.6.4节中的rank()递归方法重新实现BinarySearch并跟踪该方法的调用。
 * 每当方法被调用时，打印出它的参数lo和hi并按照递归的深度缩进。
 * 提示：为递归方法添加一个参数来保存递归的深度。
 */
fun ex22(key: Int, array: IntArray) {
    fun rank(key: Int, array: IntArray, low: Int, high: Int, depth: Int): Int {
        println("    ".repeat(depth) + "low=${low} high=${high}")
        if (low > high) return -1
        val mid = (high + low) / 2
        if (key < array[mid]) return rank(key, array, low, mid - 1, depth + 1)
        if (key > array[mid]) return rank(key, array, mid + 1, high, depth + 1)
        return mid
    }
    array.sort()
    println("array=${array.joinToString()}")
    if (array.isEmpty()) {
        println("array is empty")
        return
    }
    val index = rank(key, array, 0, array.size - 1, 0)
    if (index == -1) {
        println("${key} not found")
    } else {
        println("${key} has found, index=${index}")
    }
}

fun main() {
    inputPrompt()
    ex22(readInt("key: "), readAllInts("array: "))
}