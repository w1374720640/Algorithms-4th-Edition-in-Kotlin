package chapter2.section4

import kotlin.math.pow

/**
 * 用基于比较的方式实现MinPriorityQueue的API，使得插入元素需要~loglogN次比较，删除最小元素需要~2logN次比较
 * 提示：在swim()方法中用二分查找来寻找祖先结点
 *
 * 解：在swim()方法中用二分查找找到最后一个结点正确的位置，并将路径上的结点依次下移一个层级，其他逻辑不变
 * 如何确定路径上每个点的坐标：
 * 设需要上浮的点为k，根据练习1.1.14找到不大于log₂k的最大整数t
 * 则路径上的点分别为k/(2^1) k/(2^2) ... k/(2^t)
 * 对[1,t]范围使用二分查找，判断实际需要对比的点
 */
class FastInsertHeapMinPriorityQueue<T: Comparable<T>> : HeapMinPriorityQueue<T>() {
    override fun swim(k: Int) {
        val t = binarySearchLargeThan(k, 1, chapter1.section1.ex14(k))
        if (t == -1) return
        val value = this[k]
        swap(k, getIndex(k, 1))
        for (i in 2 .. t) {
            swap(getIndex(k, i - 1),getIndex(k, i))
        }
        this[getIndex(k, t)] = value
    }

    /**
     * 插入时二分查找范围为[1, lgN]，需要转换为实际索引从数组中取值
     */
    private fun binarySearchLargeThan(k: Int, start: Int, end: Int): Int {
        val key = this[k]!!
        var low = start
        var high = end
        while (low <= high) {
            val mid = (low + high) / 2
            //需要注意，mid值越大，getIndex()值越小
            if (this[getIndex(k, mid)]!! <= key) {
                high = mid - 1
            } else {
                low = mid + 1
            }
        }
        return if (high < start) -1 else high
    }

    /**
     * t越大，返回索引越小，实际值越小
     */
    private fun getIndex(k: Int, t: Int): Int {
        return k / (2.0.pow(t).toInt())
    }
}

fun main() {
    val pq = FastInsertHeapMinPriorityQueue<Int>()
    for (i in 1000 downTo 1) {
        pq.insert(i)
    }
    for (i in 1..1000) {
        check(i == pq.delMin())
    }
    println("check succeed!")
}