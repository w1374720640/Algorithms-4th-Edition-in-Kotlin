package chapter2.section4

import extensions.random

/**
 * 在HeapMaxPriorityQueue中，如果一个用例使用insert()插入了一个比队列中的所有元素都大的新元素，随后立即调用delMax()
 * 假设没有重复元素，此时的堆和进行这些操作之前的堆完全相同吗？
 * 进行两次insert()（第一次插入一个比队列所有元素都大的元素，第二次插入一个更大的元素）
 * 操作接两次delMax()操作呢？
 */
fun main() {
    val array = Array(100) { random(Int.MAX_VALUE - 2) }
    //最大容量必须至少比数组长度大二
    val pq = HeapMaxPriorityQueue<Int>(110)
    array.forEach {
        pq.insert(it)
    }
    val origin = pq.joinToString()

    pq.insert(pq.max() + 1)
    pq.delMax()
    val result1 = pq.joinToString()
    println(origin == result1)

    pq.insert(pq.max() + 1)
    pq.insert(pq.max() + 2)
    pq.delMax()
    pq.delMax()
    val result2 = pq.joinToString()
    println(origin == result2)
}