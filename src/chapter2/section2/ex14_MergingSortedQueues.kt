package chapter2.section2

import edu.princeton.cs.algs4.Queue
import extensions.random

/**
 * 归并有序的队列
 * 编写一个方法，将两个有序的队列作为参数，返回一个归并后的有序队列
 */
fun <T: Comparable<T>> ex14_MergingSortedQueues(queue1: Queue<T>, queue2: Queue<T>): Queue<T> {
    val result = Queue<T>()
    while (!queue1.isEmpty || !queue2.isEmpty) {
        when {
            queue1.isEmpty -> result.enqueue(queue2.dequeue())
            queue2.isEmpty -> result.enqueue(queue1.dequeue())
            queue1.peek() <= queue2.peek() -> result.enqueue(queue1.dequeue())
            else -> result.enqueue(queue2.dequeue())
        }
    }
    return result
}

fun <T: Comparable<T>> Iterable<T>.checkAscOrder(): Boolean {
    val iterator = this.iterator()
    if (!iterator.hasNext()) return true
    var pre = iterator.next()
    while (iterator.hasNext()) {
        val value = iterator.next()
        if (pre > value) return false
        pre = value
    }
    return true
}

fun main() {
    val queue1 = Queue<Double>()
    val queue2 = Queue<Double>()
    var value1 = 0.0
    var value2 = 0.0
    repeat(1000) {
        //为保证队列升序排列，随机递增一个值，然后添加到队列中
        value1 += random()
        value2 += random()
        queue1.enqueue(value1)
        queue2.enqueue(value2)
    }
    val result = ex14_MergingSortedQueues(queue1, queue2)
    println("result size = ${result.size()}  asc order = ${result.checkAscOrder()}")
}