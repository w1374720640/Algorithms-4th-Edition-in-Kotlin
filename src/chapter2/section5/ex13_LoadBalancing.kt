package chapter2.section5

import chapter2.section4.HeapMinPriorityQueue
import extensions.inputPrompt
import extensions.readInt
import extensions.readLong
import extensions.readString

/**
 * 负载均衡
 * 编写一段程序，接受一个整数M作为命令行参数，从标准输入中读取任务的名称和所需的时间，
 * 用2.5.4.3所述的最长处理时间优先原则打印出一份调度计划，
 * 将所有任务分配给M个处理器并使得所有任务完成所需的总时间最少
 *
 * 解：先对任务列表逆序排序，每个任务分配一个CPU，放入小顶堆中
 * CPU数量达到最大值时，从小顶堆中删除最小值，插入新任务，再重新添加到小顶堆中
 */
fun ex13_LoadBalancing(M: Int, jobArray: Array<Job>): Array<CPU> {
    val pq = HeapMinPriorityQueue<CPU>(M)
    jobArray.sortDescending()
    jobArray.forEach {
        if (pq.size() < M) {
            val cpu = CPU()
            cpu.add(it)
            pq.insert(cpu)
        } else {
            val cpu = pq.delMin()
            cpu.add(it)
            pq.insert(cpu)
        }
    }
    return Array(pq.size()) { pq.delMin() }
}

class CPU : Comparable<CPU> {
    private val jobList = mutableListOf<Job>()

    fun add(job: Job) {
        jobList.add(job)
    }

    fun totalTime(): Long {
        var totalTime = 0L
        jobList.forEach { totalTime += it.time }
        return totalTime
    }

    override fun compareTo(other: CPU): Int {
        return totalTime().compareTo(other.totalTime())
    }

    override fun toString(): String {
        return "[totalTime=${totalTime()}, jobList=${jobList.joinToString()}]"
    }
}

fun main() {
    inputPrompt()
    val M = readInt("M: ")
    val list = mutableListOf<Job>()
    while (true) {
        try {
            val name = readString("name: ")
            val time = readLong("time: ")
            val job = Job(name, time)
            list.add(job)
        } catch (e: Exception) {
            println("End of input")
            break
        }
    }
    val cpuArray = ex13_LoadBalancing(M, list.toTypedArray())
    cpuArray.forEach {
        println(it)
    }
}