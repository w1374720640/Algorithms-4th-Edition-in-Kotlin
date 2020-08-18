package chapter2.section4

/**
 * 按照练习2.4.1的规则，用给定序列操作一个初始为空的面向最大元素的堆，给出每次操作后堆的内容
 */
fun main() {
    val array = "PRIO*R**I*T*Y***QUE***U*E".toCharArray()
    val pq = HeapMaxPriorityQueue<Char>(10)
    array.forEach {
        if (it == '*') {
            pq.delMax()
        } else {
            pq.insert(it)
        }
        println(pq.joinToString())
    }
}