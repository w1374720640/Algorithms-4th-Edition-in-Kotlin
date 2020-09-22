package chapter3.section1

import extensions.random

/**
 * 对于N=10、10²、10³、10⁴、10⁵、10⁶，在N个小于1000的随机非负整数中FrequencyCounter平均能够找到多少个不同的键
 */
fun main() {
    var size = 10
    val times = 10
    repeat(6) {
        var count = 0
        repeat(times) {
            val st = ArrayOrderedST<Int, Int>()
            val array = Array(size) { random(1000) }
            array.forEach {
                st.put(it, 0)
            }
            count += st.size()
        }
        println("size=$size count=${count / times}")
        size *= 10
    }
}