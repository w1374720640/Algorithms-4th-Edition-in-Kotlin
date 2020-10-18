package chapter3.section2

import edu.princeton.cs.algs4.StdRandom
import extensions.formatDouble
import kotlin.math.log2

/**
 * 用经验数据评估一颗由N个随机结点构造的二叉查找树的平均高度，其中N=10⁴、10⁵和10⁶，重复实验100遍
 * 将你的实验结果和正文中给出的估计值2.99lgN进行对比
 *
 * 解：用练习3.2.6中的方法计算二叉查找树的高度，和2.99lgN对比，误差约等于一个常数
 */
fun main() {
    var size = 10000
    val repeatTimes = 100
    repeat(3) {
        var height = 0.0
        repeat(repeatTimes) {
            val st = BinaryTreeHeightST<Int, Int>()
            val array = IntArray(size) { it }
            StdRandom.shuffle(array)
            array.forEach { st.put(it, 0) }
            height += st.height()
        }
        height /= repeatTimes
        println("size=$size height=${formatDouble(height, 2)} 2.99lgN=${formatDouble(2.99 * log2(size.toDouble()), 2)}")
        size *= 10
    }
}