package chapter5.section1

import chapter2.section2.topDownMergeSort
import edu.princeton.cs.algs4.Queue

/**
 * 队列排序
 * 按照以下方法使用队列实现高位优先的字符串排序：为每个盒子设置一个队列。
 * 在第一次遍历所有元素时，将每个元素根据首字母插入到适当的队列中。
 * 然后，将每个子列表排序并合并所有队列得到一个完整的排序结果。
 * 注意，在这种方法中count[]数组不需要在递归方法内创建。
 *
 * 解：根据首字母将每个元素插入适当的队列中后，对每个队列转换成相应大小的数组，分别进行归并或快速排序
 */
fun ex11_QueueSort(array: Array<String>) {
    val R = 256
    val aux = Array(R) { Queue<String>() }
    array.forEach {
        val queue = aux[it[0].toInt()]
        queue.enqueue(it)
    }

    var i = 0
    aux.forEach { queue ->
        if (!queue.isEmpty) {
            val childArray = Array(queue.size()) { queue.dequeue() }
            topDownMergeSort(childArray)
            childArray.forEach {
                array[i++] = it
            }
        }
    }
}

fun main() {
    val array = getMSDData()
    ex11_QueueSort(array)
    println(array.joinToString(separator = "\n"))
}