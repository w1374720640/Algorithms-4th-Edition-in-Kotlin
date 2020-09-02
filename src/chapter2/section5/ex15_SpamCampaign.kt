package chapter2.section5

import chapter2.section4.HeapMinPriorityQueue
import extensions.inputPrompt
import extensions.readString

/**
 * 对E-mail排序，先按逆域名排序，再按用户名排序
 */
class EMail(address: String) : Comparable<EMail> {
    val userName: String
    val domain: Domain

    init {
        val items = address.split("@")
        require(items.size == 2 && items[0].isNotEmpty() && items[1].isNotEmpty())
        userName = items[0]
        domain = Domain(items[1])
    }

    override fun compareTo(other: EMail): Int {
        val domainCompareResult = domain.compareTo(other.domain)
        return if (domainCompareResult == 0) {
            userName.compareTo(other.userName)
        } else {
            domainCompareResult
        }
    }

    override fun toString(): String {
        return "${userName}@${domain}"
    }
}

fun main() {
    inputPrompt()
    val pq = HeapMinPriorityQueue<EMail>()
    while (true) {
        try {
            pq.insert(EMail(readString()))
        } catch (e: Exception) {
            println("End of input")
            break
        }
    }
    while (!pq.isEmpty()) {
        println(pq.delMin())
    }
}