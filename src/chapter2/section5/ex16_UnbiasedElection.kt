package chapter2.section5

import chapter2.section4.HeapMinPriorityQueue
import extensions.inputPrompt
import extensions.readString

/**
 * 公正的选举
 * 为了避免对名字排在字母表靠后的候选人的偏见，加州在2003年的州长选举中将所有候选人按照以下字母顺序排列：
 * R W Q O J M V A H B S G Z X N T C I E K U P D Y F L
 * 创建一个遵守这种顺序的数据类型并编写一个用例California，在它的静态方法main()中将字符串按照这种方式排序。
 * 假设所有字符串全部都是大写的
 */
class CandidateOrder(name: String) : Comparable<CandidateOrder> {
    init {
        require(name.isNotBlank())
    }

    val name = name.toUpperCase()

    companion object {
        //使用HashMap效率更高，也可以使用数组存放，每次查询都要遍历一次
        val scheduledOrder: HashMap<Char, Int> = "RWQOJMVAHBSGZXNTCIEKUPDYFL".toCharArray().let { array ->
            val map = HashMap<Char, Int>()
            for (i in array.indices) {
                map[array[i]] = i
            }
            map
        }
    }

    override fun compareTo(other: CandidateOrder): Int {
        val selfCharArray = name.toCharArray()
        val otherCharArray = other.name.toCharArray()
        var i = 0
        var j = 0
        while (i < selfCharArray.size && j < otherCharArray.size) {
            val selfCharIndex = scheduledOrder[selfCharArray[i]]
            val otherCharIndex = scheduledOrder[otherCharArray[j]]
            //遇见非英文字母的字符则跳过
            if (selfCharIndex == null) {
                i++
                continue
            }
            if (otherCharIndex == null) {
                j++
                continue
            }
            i++
            j++
            if (selfCharIndex != otherCharIndex) {
                return selfCharIndex.compareTo(otherCharIndex)
            }
        }
        return when {
            i == selfCharArray.size && j == otherCharArray.size -> 0
            i < selfCharArray.size -> 1
            else -> -1
        }
    }
}

fun main() {
    inputPrompt()
    val pq = HeapMinPriorityQueue<CandidateOrder>()
    while (true) {
        try {
            pq.insert(CandidateOrder(readString()))
        } catch (e: Exception) {
            println("End of input")
            break
        }
    }
    while (!pq.isEmpty()) {
        println(pq.delMin().name)
    }
}