package chapter1.exercise1_3

import edu.princeton.cs.algs4.Queue
import extensions.inputPrompt
import extensions.readAllStrings

//复制队列
fun <T> Queue<T>.copy(queue: Queue<T>): Queue<T> {
    val copyQueue = Queue<T>()
    val size = queue.size()
    //这里将原队列先出列，再分别入列到两个队列中，
    //也可以用迭代器访问，只需要入列一次
    repeat(size) {
        val value = queue.dequeue()
        queue.enqueue(value)
        copyQueue.enqueue(value)
    }
    return copyQueue
}

fun main() {
    inputPrompt()
    val array = readAllStrings()
    val originQueue = Queue<String>()
    array.forEach {
        originQueue.enqueue(it)
    }
    val copyQueue = originQueue.copy(originQueue)
    if (!originQueue.isEmpty) {
        originQueue.dequeue()
    }
    println("origin queue = ${originQueue.joinToString()}")
    println("copy queue = ${copyQueue.joinToString()}")
}