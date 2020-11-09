package chapter3.section4

import chapter3.section1.SequentialSearchST
import extensions.random

/**
 * 链表长度的范围
 * 编写一段程序，向一张长度为N/100的基于拉链法的散列表中插入N个随机的int键
 * 找出表中最长和最短的链表的长度，其中N=10³、10⁴、10⁵和10⁶
 */
fun main() {
    val N = 10000
    val st = object : SeparateChainingHashST<Int, Int>(N / 100) {
        override fun resize(size: Int) {
        }

        fun getSequentialSearchSTArray(): Array<SequentialSearchST<Int, Int>> {
            return stArray
        }
    }
    repeat(N) {
        st.put(random(Int.MAX_VALUE), 0)
    }
    val stArray = st.getSequentialSearchSTArray()
    var minST = stArray[0]
    var maxST = stArray[0]
    stArray.forEach {
        if (it.size() < minST.size()) {
            minST = it
        } else if (it.size() > maxST.size()) {
            maxST = it
        }
    }
    println("minLength=${minST.size()} maxLength=${maxST.size()}")
}