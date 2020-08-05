package chapter2.section2

import chapter2.getDoubleArray
import extensions.spendTimeMillis

/**
 * 用实验评估正文中所提到的归并排序的三项改进的效果（参考练习2.2.11）
 * 并比较正文中实现的归并和练习2.2.10所实现的归并之间的性能
 * 根据经验给出应该在何时为子数组切换到插入排序
 *
 * 解：这里只是通过设置不同的插入排序大小来找到最合适的值，性能对比参考2.2.11
 */
fun main() {
    for (i in 2..20) {
        var time = 0L
        repeat(10) {
            val array = getDoubleArray(100_0000)
            time += spendTimeMillis { optimizedTopDownMergeSort(array, i) }
        }
        time /= 10
        println("insertionSize=$i time=$time")
    }
}