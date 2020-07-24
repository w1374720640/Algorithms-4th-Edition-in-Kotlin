package chapter2.section1

import chapter2.sortMethodsCompare
import extensions.randomBoolean

/**
 * 对于主键只能取两种值的数组，评估插入排序和选择排序的性能，假设两种主键出现的概率相同
 */
fun main() {
    val sortMethods: Array<Pair<String, (Array<Int>) -> Unit>> = arrayOf(
            "Selection Sort" to ::selectionSort,
            "Insertion Sort" to ::insertionSort
    )
    var size = 128
    repeat(9) {
        println("size=$size")
        sortMethodsCompare(sortMethods, 10) {
            Array(size) {if (randomBoolean()) 0 else 1}
        }
        size *= 2
    }
}