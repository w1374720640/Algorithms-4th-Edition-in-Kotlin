package chapter2.section4

import chapter2.getCompareTimes
import chapter2.getDoubleArray
import extensions.formatInt
import extensions.formatLong

/**
 * 根据正文中的描述实现基于完全堆有序的三叉树和四叉树的堆排序
 * 对于N=10³、10⁶和10⁹大小的随机不重复数组，记录你的程序所使用的比较次数和标准实现所使用的比较次数
 * （多叉堆的实现参考练习2.4.23）
 */
fun main() {
    var size = 1000
    repeat(4) {
        val stdCompareTimes = getCompareTimes(getDoubleArray(size), ::heapSort)
        val threeCompareTimes = getCompareTimes(getDoubleArray(size)) { multiwayHeapSort(it, 3)}
        val fourCompareTimes = getCompareTimes(getDoubleArray(size)) { multiwayHeapSort(it, 4)}
        println("size: ${formatInt(size, 8)} std: ${formatLong(stdCompareTimes, 8)} " +
                "three: ${formatLong(threeCompareTimes, 8)} four: ${formatLong(fourCompareTimes, 8)}")
        size *= 10
    }
}