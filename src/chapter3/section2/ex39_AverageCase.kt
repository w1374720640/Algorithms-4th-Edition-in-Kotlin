package chapter3.section2

import extensions.formatDouble
import extensions.random
import extensions.shuffle
import kotlin.math.abs
import kotlin.math.log2
import kotlin.math.sqrt

/**
 * 平均情况
 * 用经验数据评估在一颗由N个随机结点构造的二叉查找树中，一次命中的查找和未命中的查找平均所需的比较次数的平均差和标准差
 * 其中N=10⁴、10⁵和10⁶，重复实验100遍
 * 将你的实验结果和练习3.2.35给出的计算平均比较次数的公式进行对比
 *
 * 解：利用练习3.2.7可以计算一颗给定的树中的一次随机命中查找平均所需要的比较次数
 * 通过重写BinaryTreeST的get()方法统计实际的比较次数
 * 构造二叉查找树时，只插入偶数，查找时，偶数为命中查找，奇数为未命中查找
 * 平均差是将各个元素与平均值求差后取绝对值，再除以总数
 * 标准差是将各个元素与平均值求差后平方，再除以总数，再开根号。相对于平均差，通过平方给较大的偏差更大的惩罚
 * 3.2.35内容：随着N的增大，在一颗随机构造的二叉查找树中，一次命中查找所需的平均比较次数会趋近于1.39lgN-1.85
 */
fun main() {
    var size = 10000
    val repeatTimes = 100
    repeat(3) {
        val input = Array(size) { it * 2 }
        input.shuffle()
        val st = NumOfComparisonsBinaryTreeST<Int, Int>()
        input.forEach { st.put(it, 0) }

        //命中key的实际比较次数
        val hitArray = IntArray(repeatTimes)
        //未命中key的实际比较次数
        val missArray = IntArray(repeatTimes)
        repeat(repeatTimes) { index ->
            hitArray[index] = st.getNumOfComparisons(random(size) * 2)
            missArray[index] = st.getNumOfComparisons(random(size) * 2 + 1)
        }

        //命中查找的平均比较次数
        val hitAverage = hitArray.average()
        //未命中查找的平均比较次数
        val missAverage = missArray.average()
        //计算平均差的lambda表达式
        val averageDiff = { array: IntArray, average: Double ->
            var total = 0.0
            array.forEach {
                total += abs(it - average)
            }
            total / repeatTimes
        }
        //计算标准差的lambda表达式
        val standardDeviation = { array: IntArray, average: Double ->
            var total = 0.0
            array.forEach {
                total += (it - average) * (it - average)
            }
            sqrt(total / repeatTimes)
        }
        val hitAverageDiff = averageDiff(hitArray, hitAverage)
        val hitStandardDeviation = standardDeviation(hitArray, hitAverage)
        val missAverageDiff = averageDiff(missArray, missAverage)
        val missStandardDeviation = standardDeviation(missArray, missAverage)

        println("size=$size expectAverage=${formatDouble(st.avgComparesDouble(), 2)} ex35=${formatDouble(1.39 * log2(size.toDouble()) - 1.85, 2)}")
        println("hit : ActualAverage=${formatDouble(hitAverage, 2)} AverageDiff=${formatDouble(hitAverageDiff, 2)} StandardDeviation=${formatDouble(hitStandardDeviation, 2)}")
        println("miss: ActualAverage=${formatDouble(missAverage, 2)} AverageDiff=${formatDouble(missAverageDiff, 2)} StandardDeviation=${formatDouble(missStandardDeviation, 2)}")
        println()

        size *= 10
    }
}

/**
 * 可以统计每次查找比较次数的二叉查找树
 */
class NumOfComparisonsBinaryTreeST<K : Comparable<K>, V : Any> : BinaryTreeST<K, V>() {
    var numOfComparisons = 0

    fun getNumOfComparisons(key: K): Int {
        get(key)
        return numOfComparisons
    }

    override fun get(key: K): V? {
        numOfComparisons = 0
        return super.get(key)
    }

    override fun get(node: Node<K, V>, key: K): Node<K, V>? {
        numOfComparisons++
        return super.get(node, key)
    }
}