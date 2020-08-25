package chapter1.section4

import kotlin.math.pow

/**
 * 假设有一个算法，接受平面上的N个点，并能够返回在同一条直线上的三个点的组数
 * 证明你能够用这个算法解决3-sum问题
 * 强烈提示：使用代数证明当且仅当a+b+c=0时，(a,a³)、(b,b³)和(c,c³)在同一条直线上
 *
 * 解：两个平方数之差a²-b²可因式分解为(a-b)(a+b) 两个立方数之差a³-b³可因式分解为(a-b)(a²+ab+b²)
 * 若(a,a³)、(b,b³)和(c,c³)三个点在同一条直线上
 * 则ab两点组成直线的斜率k1、ac两点组成直线的斜率k2和bc两点组成直线的斜率k3相等
 * k1=(a³-b³)/(a-b)=a²+ab+b²
 * k2=(a³-c³)/(a-c)=a²+ac+c²
 * k3=(b³-c³)(b-c)=b²+bc+c²
 * 由k1=k2可以推导出 a²+ab+b²=a²+ac+c² -> ab-ac=c²-b² -> a(b-c)=(c-b)(c+b) -> (b-c)(a+b+c)=0 -> a+b+c=0 或 b-c=0
 * 当b=c时，两个点位置重复，不能连成一条直线
 * 所以当且仅当a+b+c=0时，(a,a³)、(b,b³)和(c,c³)在同一条直线上
 * 设数组元素分别为x1,x2,x3...xn，对数组处理生成点(x1,x1³),(x2,x2³),(x3,x3³)...(xn,xn³)
 * 根据给定的算法，求出同一条直线上的三个点组数，结果等于3-sum问题的解
 * （要求输入数据中不包含重复元素）
 */
fun ex26_3Collinearity(array: IntArray): Long {

    /**
     * 判断三个点是否在同一条直线上
     * 设三个点分别为(x1,y1),(x2,y2),(x3,y3)
     * 三个点在一条直线上，说明每两个点组成的直线斜率相同或都不存在（垂直与y轴）
     * 由斜率相等得 (y1-y2)/(x1-x2)=(y2-y3)/(x2-x3)
     * 变形得 (y1-y2)*(x2-x3)-(y2-y3)*(x1-x2)=0
     * 所有满足上述变形公式的三个点都在一条直线上，即使斜率不存在
     */
    fun pointsOnSameLine(point1: Pair<Long, Long>, point2: Pair<Long, Long>, point3: Pair<Long, Long>): Boolean {
        return (point1.second - point2.second) * (point2.first - point3.first) == (point2.second - point3.second) * (point1.first - point2.first)
    }

    /**
     * 使用三点一直线的方法判断3-sum问题时，要求数据源中数据不重复
     * 相同的两个点在实际上无法连成一条线，在公式中和任何一个点都满足条件
     */
    fun checkPointIsRepeated(point1: Pair<Long, Long>, point2: Pair<Long, Long>, point3: Pair<Long, Long>): Boolean {
        return point1 == point2 || point1 == point3 || point2 == point3
    }

    /**
     * 计算平面上N个点中，能连成一条直线的三个点的组数
     */
    fun numOfThreePointsOnSameLine(array: Array<Pair<Long, Long>>): Long {
        var count = 0L
        for (i in 0..array.size - 3) {
            for (j in i + 1..array.size - 2) {
                for (k in j + 1 until array.size) {
                    //排除重复的点
                    if (checkPointIsRepeated(array[i], array[j], array[k])) continue
                    if (pointsOnSameLine(array[i], array[j], array[k])) {
                        count++
                    }
                }
            }
        }
        return count
    }
    //为防止数据溢出，使用Long来保存点的坐标
    //也可以使用BigInteger来保存任意长度的整数，但是效率比原始类型低几十倍
    val longArray = Array(array.size) {
        val x = array[it].toLong()
        x to x.toDouble().pow(3).toLong()
    }
    return numOfThreePointsOnSameLine(longArray)
}

fun main() {
    val array = intArrayOf(-9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    println("threeSum: ${threeSum(array)}")
    println("ex26: ${ex26_3Collinearity(array)}")
}