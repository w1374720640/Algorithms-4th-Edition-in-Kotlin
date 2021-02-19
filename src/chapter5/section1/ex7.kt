package chapter5.section1

import edu.princeton.cs.algs4.Queue

/**
 * 用一个Queue对象的数组实现键索引计数法
 *
 * 解：创建一个长度为R的Queue对象数组，
 * 遍历原始数组，以元素的key值为索引将元素依次放入对应位置的Queue中，
 * 遍历结束后再依次从辅助数组的每个Queue中取出元素。
 */
fun ex7(array: Array<Key>, R: Int) {
    val aux = Array(R) { Queue<Key>() }
    array.forEach {
        val key = it.key()
        require(key in 0 until R)
        aux[key].enqueue(it)
    }
    var i = 0
    aux.forEach { queue ->
        queue.forEach { key ->
            array[i++] = key
        }
    }
}

fun main() {
    val studentGroup = getStudentGroup()
    val array = studentGroup.first
    val R = studentGroup.second
    ex7(array, R)
    println(array.joinToString(separator = "\n"))
}