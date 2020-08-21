package chapter2.section4

/**
 * 将给定字符按顺序插入一个面向最大元素的堆中，给出结果
 */
fun main() {
    val array = "EASYQUESTION".toCharArray()
    val pq = HeapMaxPriorityQueue<Char>(array.size)
    array.forEach { pq.insert(it) }
    println(pq.toString())
}