package chapter2.section5

import chapter2.section4.HeapMinPriorityQueue
import extensions.inputPrompt
import extensions.readString
import kotlin.math.min

/**
 * 逆域名排序
 * 为域名编写一个数据类型Domain并为它实现一个compareTo()方法，使之能够按照逆向的域名排序
 * 例如，域名cs.princeton.edu的逆是edu.princeto.cs。这在网络日志处理时很有用
 * 提示：使用s.split("\\.")将域名用点分为若干部分
 * 编写一个Domain的用例，从标准输入读取域名并将他们按照逆域名有序的打印出来
 */
class Domain(val name: String) : Comparable<Domain> {
    private fun split(): List<String> {
        //这里调用的是CharSequence.split()扩展方法，而不是String的类方法，不是正则匹配
        return name.split(".")
    }

    override fun compareTo(other: Domain): Int {
        val selfList = split()
        val otherList = other.split()
        val minSize = min(selfList.size, otherList.size)
        //从后向前比较
        for (i in 1..minSize) {
            val compareResult = selfList[selfList.size - i].compareTo(otherList[otherList.size - i])
            if (compareResult != 0) return compareResult
        }
        return when {
            selfList.size == otherList.size -> 0
            selfList.size < otherList.size -> -1
            else -> 1
        }
    }

    override fun toString(): String {
        return name
    }
}

fun main() {
    inputPrompt()
    val pq = HeapMinPriorityQueue<Domain>()
    while (true) {
        try {
            pq.insert(Domain(readString()))
        } catch (e: Exception) {
            println("End of input")
            break
        }
    }
    while (!pq.isEmpty()) {
        println(pq.delMin())
    }
}