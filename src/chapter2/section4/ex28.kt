package chapter2.section4

import extensions.random
import extensions.spendTimeMillis
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * 随机生成N个三维坐标(x,y,z)，求距离原点最近的M个点
 * 在N=10⁸且M=10⁴时，预计程序的运行时间
 *
 * 解：三维坐标点和原点的距离公式为sqr(x²+y²+z²)
 * 使用基于大顶堆的优先队列保存最小的M个值
 * 像队列中添加数据后，判断长度是否大于M，大于则弹出最大值
 */
fun ex28(N: Int, M: Int): MaxPriorityQueue<Double> {
    val pq = HeapMaxPriorityQueue<Double>(M + 1)
    repeat(N) {
        val point = Point3D(random(), random(), random())
        pq.insert(point.distanceFromOrigin())
        if (pq.size() > M) {
            pq.delMax()
        }
    }
    return pq
}

class Point3D(val x: Double, val y: Double, val z: Double) {
    fun distance(other: Point3D): Double {
        return sqrt((x - other.x).pow(2) + (y - other.y).pow(2) + (z - other.z).pow(2))
    }

    fun distanceFromOrigin(): Double {
        return sqrt(x * x + y * y + z * z)
    }
}

/**
 * 先计算N=10⁶ M=10⁴时消耗的时间，再估算N=10⁸ M=10⁴时消耗的时间
 * 因为优先队列大小不变，所以N=10⁸时消耗的时间是N=10⁶的100倍
 */
fun main() {
    var time = spendTimeMillis {
        repeat(10) {
            ex28(100_0000, 1_0000)
        }
    }
    time /= 10
    println("N=10⁶ M=10⁴ average spend $time ms")
    println("N=10⁸ M=10⁴ expect spend ${time * 100} ms")
}