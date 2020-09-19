package chapter3.section1

import edu.princeton.cs.algs4.Queue

/**
 * 修改frequencyCounter，打印出现频率最高的所有单词，而非其中之一。提示：请用Queue
 *
 * 解：最后一步遍历所有key时，记录当前出现频率最高的单词，
 * 新的频率小于最高频率时忽略，等于时插入队列中，大于最高频率时先移除队列中所有数据，再添加进去
 */
fun ex19(array: Array<String>, st: ST<String, Int>): Queue<String> {
    array.forEach {
        val count = st.get(it)
        if (count == null) {
            st.put(it, 1)
        } else {
            st.put(it, count + 1)
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
    //tale.txt文件中出现频率最高的单词只有一个，手动生成一个测试数组
    val array = arrayOf(
            "aaaa",
            "ddd",
            "cc",
            "aaaa",
            "cc",
            "eee",
            "aaaa",
            "bbb",
            "ddd",
            "aaaa",
            "ee",
            "ddd",
            "cc",
            "jjj",
            "ddd"
    )
    val queue = ex19(array, ArrayOrderedST())
    while (!queue.isEmpty) {
        println(queue.dequeue())
    }
}