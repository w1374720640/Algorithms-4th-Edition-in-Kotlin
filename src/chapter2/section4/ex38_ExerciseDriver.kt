package chapter2.section4

import extensions.randomBoolean

/**
 * 编写一个练习用例，用HeapMaxPriorityQueue处理实际应用中可能出现的高难度或是极端情况
 * 例如，元素已经有序、元素全部逆序、元素全部相同或者所有元素只有两个值
 */
fun <T : Comparable<T>> ex38_ExerciseDriver(array: Array<T>) {
    if (array.isEmpty()) return
    val pq = HeapMaxPriorityQueue<T>(array.size)
    array.forEach {
        pq.insert(it)
    }
    var max = pq.delMax()
    while (!pq.isEmpty()) {
        val value = pq.delMax()
        check(value <= max)
        max = value
    }
    println("check succeed!")
}

fun main() {
    val size = 1000
    val ascOrderArray = Array(size) { it }
    val descOrderArray = Array(size) { size - it }
    val sameArray = Array(size) { 1 }
    val twoValueArray = Array(size) { randomBoolean() }
    ex38_ExerciseDriver(ascOrderArray)
    ex38_ExerciseDriver(descOrderArray)
    ex38_ExerciseDriver(sameArray)
    ex38_ExerciseDriver(twoValueArray)
}