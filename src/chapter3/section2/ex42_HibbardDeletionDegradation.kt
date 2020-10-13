package chapter3.section2

import extensions.formatDouble
import extensions.random
import kotlin.math.sqrt

/**
 * Hibbard删除方法的性能问题
 * 编写一个程序，从命令行接受一个参数N并构造一棵由N个随机键生成的二叉查找树，然后进入一个循环
 * 在循环中它先删除一个随机键（delete(select(StdRandom.uniform(N)))），然后再插入一个随机键，如此循环N²次
 * 循环结束后，计算并打印树的内部平均路径长度（内部路径长度除以N再加1）
 * 对于N=10²、10³和10⁴，运行你的程序来验证一个有些违反直觉的假设：
 * 这个过程会增加树的平均路径长度，增加的长度和N的平方根成正比
 * 使用能够随机选择前趋或后继结点的delete()方法重复这个实验
 *
 * 解：使用练习3.2.7中的avgCompares()方法计算树的内部平均路径长度
 */
fun ex42_HibbardDeletionDegradation(N: Int) {
    require(N > 0)
    val st = BinaryTreeST<Int, Int>()
    repeat(N) {
        st.put(random(Int.MAX_VALUE), 0)
    }
    println("N=$N")
    val beforeAvgCompares = st.avgComparesDouble()
    println("before avgCompares=${formatDouble(beforeAvgCompares, 2)}")
    repeat(N * N) {
        st.delete(st.select(random(st.size())))
        st.put(random(Int.MAX_VALUE), 0)
    }
    val afterAvgCompares = st.avgComparesDouble()
    println("after  avgCompares=${formatDouble(afterAvgCompares, 2)}")
    val sqrtN = sqrt(N.toDouble())
    println("increase:${formatDouble(afterAvgCompares - beforeAvgCompares, 2)} sqrt(N)=${formatDouble(sqrtN, 2)} " +
            "ratio=${formatDouble((afterAvgCompares - beforeAvgCompares) / sqrtN, 2)}")
}

fun main() {
    var N = 1000
    repeat(4) {
        ex42_HibbardDeletionDegradation(N)
        N *= 2
    }
}