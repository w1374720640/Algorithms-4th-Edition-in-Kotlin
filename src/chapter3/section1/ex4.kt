package chapter3.section1

import extensions.shuffle
import java.util.*

/**
 * 开发抽象数据类型Time和Event来处理表3.1.5中的例子中的数据
 *
 * 解：自定义可以比较的Time类，Event类直接用String类代替
 */
fun main() {
    val inputArray = arrayOf(
            Time("09:00:00") to "Chicago",
            Time("09:00:03") to "Phoenix",
            Time("09:00:13") to "Houston",
            Time("09:00:59") to "Chicago",
            Time("09:01:10") to "Houston",
            Time("09:03:13") to "Chicago",
            Time("09:10:11") to "Seattle",
            Time("09:10:25") to "Seattle",
            Time("09:14:25") to "Phoenix",
            Time("09:19:32") to "Chicago",
            Time("09:19:46") to "Chicago",
            Time("09:21:05") to "Chicago",
            Time("09:22:43") to "Seattle",
            Time("09:22:54") to "Seattle",
            Time("09:25:52") to "Chicago",
            Time("09:35:21") to "Chicago",
            Time("09:36:14") to "Seattle",
            Time("09:37:44") to "Phoenix"
    )
    inputArray.shuffle()
    val orderedST = BinarySearchST<Time, String>()
    inputArray.forEach {
        orderedST.put(it.first, it.second)
    }
    check(orderedST.min().time == "09:00:00")
    check(orderedST.get(Time("09:00:13")) == "Houston")
    check(orderedST.floor(Time("09:05:00")).time == "09:03:13")
    check(orderedST.select(7).time == "09:10:25")
    val keys = orderedST.keys(Time("09:15:00"), Time("09:25:00"))
    var index = 0
    keys.forEach {
        when (index) {
            0 -> check(it.time == "09:19:32")
            1 -> check(it.time == "09:19:46")
            2 -> check(it.time == "09:21:05")
            3 -> check(it.time == "09:22:43")
            4 -> check(it.time == "09:22:54")
            else -> throw IllegalStateException()
        }
        index++
    }
    check(orderedST.ceiling(Time("09:30:00")).time == "09:35:21")
    check(orderedST.max().time == "09:37:44")
    check(orderedST.size(Time("09:15:00"), Time("09:25:00")) == 5)
    check(orderedST.rank(Time("09:10:25")) == 7)
    println("Check succeed.")
}

class Time(val time: String) : Comparable<Time> {
    var timeList = time.split(":")

    init {
        require(timeList.size == 3)
        for (i in timeList.indices) {
            require(timeList[i].length == 2)
        }
    }

    override fun compareTo(other: Time): Int {
        for (i in timeList.indices) {
            val compare = timeList[i].compareTo(other.timeList[i])
            if (compare != 0) {
                return compare
            }
        }
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (this === other) return true
        if (other !is Time) return false
        return this.compareTo(other) == 0
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(timeList.toTypedArray())
    }
}