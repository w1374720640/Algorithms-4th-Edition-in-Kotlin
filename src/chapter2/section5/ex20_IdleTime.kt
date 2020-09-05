package chapter2.section5

import chapter2.section2.topDownMergeSort
import extensions.shuffle

/**
 * 假设有一台计算机能够并行处理N个任务
 * 编写一段程序并给定一系列任务的起始时间和结束时间，找出这台机器最长的空闲时间和最长的繁忙时间
 *
 * 解：空闲和繁忙与N的大小无关，只要有任务在处理，就是繁忙状态，只要没有任务在处理就是空闲状态
 * 所以需要判断任务的连续性，最长的连续时间就是最长的繁忙时间，最长的不连续时间就是最长的空闲时间
 * 创建一个Job类表示一个任务，包含开始时间和结束时间，
 * 将所有任务按开始时间排序，创建一个新的任务，遍历所有任务，将重叠的任务合并到新任务中
 * 当遇到不连续的任务时，重新创建一个任务用于合并后面的任务，
 * 遍历结束后，再遍历新任务列表，最长的繁忙时间就是最大的新任务，最长的空闲时间就是两个任务的最大间隔
 */
fun ex20_IdleTime(array: Array<ParallelJob>): Pair<Long, Long> {
    if (array.isEmpty()) return 0L to 0L
    topDownMergeSort(array)
    val list = mutableListOf<ParallelJob>()
    var mergeJob = ParallelJob(array[0].startTime, array[0].endTime)
    list.add(mergeJob)
    for (i in 1 until array.size) {
        val nextJob = array[i]
        if (nextJob.startTime > mergeJob.endTime) {
            //不连续的任务，时间差为空闲的时间
            mergeJob = ParallelJob(nextJob.startTime, nextJob.endTime)
            list.add(mergeJob)
        } else {
            mergeJob.endTime = maxOf(mergeJob.endTime, nextJob.endTime)
        }
    }
    var maxRunTime = list[0].duration()
    var maxIdleTime = 0L
    for (i in 1 until list.size) {
        maxRunTime = maxOf(maxRunTime, list[i].duration())
        maxIdleTime = maxOf(maxIdleTime, list[i].startTime - list[i - 1].endTime)
    }
    return maxRunTime to maxIdleTime
}

class ParallelJob(var startTime: Long, var endTime: Long) : Comparable<ParallelJob> {
    init {
        require(startTime > 0 && endTime >= startTime)
    }

    override fun compareTo(other: ParallelJob): Int {
        return startTime.compareTo(other.startTime)
    }

    fun duration(): Long {
        return endTime - startTime
    }
}

fun main() {
    val jobArray = arrayOf(
            ParallelJob(100, 150),
            ParallelJob(120, 190),
            ParallelJob(140, 210),
            ParallelJob(250, 500),
            ParallelJob(280, 330),
            ParallelJob(380, 440),
            ParallelJob(700, 750),
            ParallelJob(800, 930),
            ParallelJob(840, 980),
            ParallelJob(870, 890)
    )
    jobArray.shuffle()
    val pair = ex20_IdleTime(jobArray)
    println("maxRunTime=${pair.first} ms   maxIdleTime=${pair.second} ms")
}

