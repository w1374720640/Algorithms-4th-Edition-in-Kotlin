package chapter3.section5

import edu.princeton.cs.algs4.Queue

/**
 * 创建一个类似于队列的数据类型，但每个元素只能插入队列一次
 * 用一个符号表来记录所有已经被插入的元素并忽略所有将它们重新插入的请求
 *
 * 解：使用集合来记录已经被插入的元素而不是符号表
 */
class UniQueue<Item : Any> : Iterable<Item> {
    private val queue = Queue<Item>()
    private val set = LinearProbingHashSET<Item>()

    fun size(): Int {
        return queue.size()
    }

    fun isEmpty(): Boolean {
        return size() == 0
    }

    fun peek(): Item {
        return queue.peek()
    }

    fun enqueue(item: Item) {
        if (set.contains(item)) return
        queue.enqueue(item)
        set.add(item)
    }

    fun dequeue(): Item {
        val item = queue.dequeue()
        set.delete(item)
        return item
    }

    override fun iterator(): Iterator<Item> {
        return queue.iterator()
    }
}

fun main() {
    val queue = UniQueue<Int>()
    queue.enqueue(1)
    queue.enqueue(2)
    queue.enqueue(3)
    queue.enqueue(1)
    queue.enqueue(4)
    check(queue.size() == 4)
    check(queue.dequeue() == 1)
    check(queue.peek() == 2)
    check(queue.dequeue() == 2)
    check(queue.dequeue() == 3)
    check(queue.dequeue() == 4)
    check(queue.isEmpty())
    println("UniQueue check succeed.")
}