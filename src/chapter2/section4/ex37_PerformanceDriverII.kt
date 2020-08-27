package chapter2.section4

import chapter2.sleep
import edu.princeton.cs.algs4.Point2D
import extensions.delayExit
import extensions.random
import kotlin.concurrent.thread

/**
 * 编写一个性能测试用例，用插入元素操作填满一个优先队列，
 * 然后在一秒钟之内尽可能多地连续反复调用删除最大元素和插入元素的操作。
 * 用一列随机的长短不同的元素多次重复以上过程，
 * 将程序能够完成的删除最大元素操作的平均次数打印出来或是绘制成图表
 */
fun ex37_PerformanceDriverII(size: Int): Long {
    val pq = HeapMaxPriorityQueue<Double>(size)
    repeat(size) {
        pq.insert(random())
    }
    var interruptFlag = false
    var count = 0L
    thread {
        sleep(1000)
        interruptFlag = true
    }
    while (!interruptFlag) {
        pq.delMax()
        pq.insert(random())
        count++
    }
    return count
}

fun main() {
    var size = 10000
    val list = mutableListOf<Point2D>()
    repeat(10) {
        val count = ex37_PerformanceDriverII(size)
        println("size=$size count=$count")
        list.add(Point2D(size.toDouble(), count.toDouble()))
        drawPointList(list)
        size *= 2
    }
    delayExit()
}