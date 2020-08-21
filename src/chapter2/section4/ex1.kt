package chapter2.section4

/**
 * 给定序列中，字母表示插入元素，星号表示删除最大元素，操作一个初始为空的优先队列，给出每次删除最大元素返回的字符
 */
fun main() {
    val array = "PRIO*R**I*T*Y***QUE***U*E".toCharArray()
    val pq = HeapMaxPriorityQueue<Char>()
    array.forEach {
        if (it == '*') {
            print(pq.delMax() + " ")
        } else {
            pq.insert(it)
        }
    }
}