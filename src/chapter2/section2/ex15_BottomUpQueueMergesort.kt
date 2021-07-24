package chapter2.section2

import edu.princeton.cs.algs4.Queue
import extensions.inputPrompt
import extensions.random
import extensions.readInt

/**
 * 自底向上的有序队列归并排序
 * 用下面的方法编写一个自底向上的归并排序：
 * 给定N个元素，创建N个队列，每个队列包含其中一个元素
 * 创建一个由这N个队列组成的队列，然后不断用练习2.2.14中的方法将队列的头两个元素归并
 * 并将结果重新加入到队列结尾，直到队列的队列只剩下一个元素为止
 */
fun <T : Comparable<T>> ex15_BottomUpQueueMergesort(queue: Queue<Queue<T>>) {
    while (queue.size() > 1) {
        queue.enqueue(ex14_MergingSortedQueues(queue.dequeue(), queue.dequeue()))
    }
}

fun main() {
    inputPrompt()
    val size = readInt("size: ")
    val queue = Queue<Queue<Double>>()
    repeat(size) {
        queue.enqueue(Queue<Double>().apply { enqueue(random()) })
    }
    ex15_BottomUpQueueMergesort(queue)
    if (!queue.isEmpty) {
        val result = queue.peek().checkAscOrder()
        println("result = $result")
    }
}