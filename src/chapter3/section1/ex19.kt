package chapter3.section1

import edu.princeton.cs.algs4.In
import edu.princeton.cs.algs4.Queue

/**
 * 修改frequencyCounter，打印出现频率最高的所有单词，而非其中之一。提示：请用Queue
 *
 * 解：最后一步遍历所有key时，记录当前出现频率最高的单词，
 * 新的频率小于最高频率时忽略，等于时插入队列中，大于最高频率时先移除队列中所有数据，再添加进去
 */
fun ex19(input: In, minLength: Int, st: ST<String, Int>): Queue<String> {
    while (!input.isEmpty) {
        val word = input.readString()
        if (word.length >= minLength) {
            val count = st.get(word)
            if (count == null) {
                st.put(word, 1)
            } else {
                st.put(word, count + 1)
            }
        }
    }
    val queue = Queue<String>()
    var maxCount = 0
    st.keys().forEach { key ->
        val count = st.get(key)!!
        if (count == maxCount) {
            queue.enqueue(key)
        } else if (count > maxCount) {
            maxCount = count
            while (!queue.isEmpty) {
                queue.dequeue()
            }
            queue.enqueue(key)
        }
    }
    return queue
}

fun main() {
    val queue = ex19(In("./data/tinyTale.txt"), 0, BinarySearchST())
    while (!queue.isEmpty) {
        println(queue.dequeue())
    }
}