package chapter2.section4


/**
 * 无需交换的堆
 * 因为sink()和swim()中都用到了初级函数swap()，所以所有元素都被加载并存储了一次。
 * 回避这种低效方式，用插入排序给出新的实现（请见练习2.1.25）
 *
 * 解：在sink()和swim()中记录最大值或最小值，当下沉到底或上浮到顶时才将最大值或最小值交换到指定位置
 */
class HeapMaxPriorityQueueNotSwap<T : Comparable<T>> : HeapMaxPriorityQueue<T>() {

    override fun swim(k: Int) {
        var i = k
        val value = priorityQueue[i]!!
        while (i > 1 && priorityQueue[i / 2]!! < value) {
            priorityQueue[i] = priorityQueue[i / 2]
            i /= 2
        }
        if (i < k) {
            priorityQueue[i] = value
        }
    }

    override fun sink(k: Int) {
        var i = k
        val value = priorityQueue[i]!!
        while (2 * i <= size) {
            var j = i * 2
            if (j < size && less(j, j + 1)) {
                j++
            }
            if (value < priorityQueue[j]!!) {
                priorityQueue[i] = priorityQueue[j]
                i = j
            } else {
                break
            }
        }
        if (i > k) {
            priorityQueue[i] = value
        }
    }

}

fun main() {
    val priorityQueue = HeapMaxPriorityQueueNotSwap<Int>()
    repeat(1000) {
        priorityQueue.insert(it)
    }
    repeat(1000) {
        if (priorityQueue.delMax() != 999 - it) {
            println("check failed!")
            return
        }
    }
    println("check succeed!")
}