package chapter2.section1

import chapter2.ArrayInitialState
import chapter2.getDoubleArray
import extensions.formatDouble
import extensions.formatInt
import extensions.formatLong
import extensions.spendTimeMillis
import kotlin.math.pow

/**
 * 编写一个能对排序算法进行双倍测试的用例
 * 数组规模N的起始值为1000，排序后打印N、估计排序用时、实际排序用时以及在N倍增后两次用时的比例
 * 用这段程序验证随机输入模型下插入排序和选择排序的运行时间都是平方级别的，对希尔排序的性能作出猜想并验证你的猜想
 * 和第一章第三节中的TimeRatio方法类似
 *
 * @param maxSize 从1000开始倍增，最大的数组大小
 * @param sortMethod 用于排序的方法
 * @param O 理论上的算法复杂度，如 O(lgN) O(N) O(NlgN) O(N²) 等
 */
fun doubleGrowthTest(maxSize: Int, sortMethod: (Array<Double>) -> Unit, O: (N: Int) -> Double) {
    var N = 1000
    var preTime = 0L
    while (N <= maxSize) {
        val array = getDoubleArray(N)
        val time = spendTimeMillis {
            sortMethod(array)
        }
        val expectedTime = (preTime * O(N) / O(N / 2)).toLong()
        println("N=${formatInt(N, 7)} expectedTime=${if (expectedTime == 0L) "    /" else formatLong(expectedTime, 5)} ms " +
                "realTime=${formatLong(time, 5)} ms ratio=${if (preTime == 0L) " / " else formatDouble(time.toDouble() / preTime, 2)}")
        preTime = time
        N *= 2
    }
}

fun main() {
    println("selectSort:")
    doubleGrowthTest(10_0000, ::selectSort) { N -> N.toDouble().pow(2) }
    println("insertSort:")
    doubleGrowthTest(10_0000, ::insertSort) { N -> N.toDouble().pow(2) }

    //希尔排序不同增长序列有不同的性能，参考 https://blog.csdn.net/Foliciatarier/article/details/53891144
    println("shellSort:")
    doubleGrowthTest(500_0000, ::shellSort) { N -> N.toDouble().pow(4.0 / 3) }
}
