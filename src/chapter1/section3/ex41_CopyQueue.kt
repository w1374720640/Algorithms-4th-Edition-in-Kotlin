package chapter1.section3

import edu.princeton.cs.algs4.Queue
import extensions.inputPrompt
import extensions.readAllStrings

/**
 * 复制队列
 * 编写一个新的构造函数，使以下代码
 * Queue<Item> r = new Queue<Item>(q);
 * 得到的r指向队列q的一个新的独立的副本。
 * 可以对q或r进行任意入列或出列操作但它们不会相互影响。
 * 提示：从q中去除所有元素再将它们插入q和r
 *
 * 解：使用扩展函数模拟新的构造函数
 */
fun <T> Queue<T>.copy(): Queue<T> {
    val copyQueue = Queue<T>()
    val size = size()
    //这里将原队列先出列，再分别入列到两个队列中，
    //也可以用迭代器访问，只需要入列一次
    repeat(size) {
        val value = dequeue()
        enqueue(value)
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
    val copyQueue = originQueue.copy()
    if (!originQueue.isEmpty) {
        originQueue.dequeue()
    }
    println("origin queue = ${originQueue.joinToString()}")
    println("copy queue = ${copyQueue.joinToString()}")
}