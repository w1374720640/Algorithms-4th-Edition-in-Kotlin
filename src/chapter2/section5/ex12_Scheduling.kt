package chapter2.section5

import chapter2.section4.HeapMinPriorityQueue
import extensions.inputPrompt
import extensions.readLong
import extensions.readString

class Job(val name: String, val time: Long) : Comparable<Job> {
    init {
        require(name.isNotBlank())
        require(time > 0)
    }

    override fun compareTo(other: Job): Int {
        return time.compareTo(other.time)
    }

    override fun toString(): String {
        return "$name:${time}ms"
    }
}

/**
 * 编写一段程序，从标准输入中读取任务的名称和所需的运行时间，
 * 用2.5.4.3节所述的最短处理时间优先的原则打印出一份调度计划，使得任务完成的平均时间最小
 */
fun main() {
    inputPrompt()
    //也可以先读取任务数量，然后放入数组中排序
    val heapMinPriorityQueue = HeapMinPriorityQueue<Job>()
    while (true) {
        try {
            val name = readString("name: ")
            val time = readLong("time: ")
            val job = Job(name, time)
            heapMinPriorityQueue.insert(job)
        } catch (e: Exception) {
            println("End of input")
            break
        }
    }
    println("Scheduling:")
    while (!heapMinPriorityQueue.isEmpty()) {
        println(heapMinPriorityQueue.delMin())
    }
}