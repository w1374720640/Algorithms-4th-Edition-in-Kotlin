package chapter1.section4

import extensions.formatDouble
import extensions.inputPrompt
import extensions.readInt
import kotlin.math.log2

/**
 * 有一栋N层的大楼和许多鸡蛋，从F层或更高的地方扔下鸡蛋会摔碎，否则不会
 * 设计一种策略来确定F的值，其中扔~lgN次鸡蛋后摔碎的鸡蛋数量为~lgN
 *
 * 解：用二分查找确定F的值，需要扔~lgN次，平均成本为~1/2*lgN（每次扔，摔碎和没摔碎的概率都为1/2）
 * 数组中0表示不会摔碎，1表示会摔碎，找出0和1的分界点
 * 返回层数和摔碎的鸡蛋数（不是查找次数）
 */
fun ex24a_ThrowingEggs(array: IntArray, start: Int = 0, end: Int = array.size - 1, count: Int = 0): Pair<Int, Int> {
    require(array.isNotEmpty() && start >= 0 && end < array.size)
    if (start > end) {
        return if (start >= array.size) {
            -1 to count
        } else {
            start to count
        }
    }
    val mid = (start + end) / 2
    return if (array[mid] == 0) {
        ex24a_ThrowingEggs(array, mid + 1, end, count)
    } else {
        ex24a_ThrowingEggs(array, start, mid - 1, count + 1)
    }
}

/**
 * 条件与上面相同，把成本减低到~2lgF
 * 先从第二层开始扔，如果没碎，依次从4、8、16 ... 2^n层楼扔，直到鸡蛋碎了，成本为常数1
 * 然后在[2^(n-1), F)范围内二分查找，因为确定范围时总是乘2，所以二分查找的范围小于等于F/2
 * 所以，总成本（平均值）为1+1/2*lg(F/2)=1+1/2*(lgF-lg2)=1+1/2*lgF-1/2*1=(1+lgF)/2
 * 和题目要求不匹配，且受数据源影响较大
 */
fun ex24b_ThrowingEggs(array: IntArray): Pair<Int, Int> {
    require(array.isNotEmpty())
    if (array.size <= 2) {
        return ex24a_ThrowingEggs(array)
    }
    var n = 0
    var end = 1
    do {
        n++
        end *= 2
        if (end >= array.size - 1) {
            return ex24a_ThrowingEggs(array, start = end / 2, end = array.size - 1)
        }
        if (array[end] == 1) {
            return ex24a_ThrowingEggs(array, start = end / 2, end = end, count = 1)
        }
    } while (true)
}

fun main() {
    inputPrompt()
    //计算ex24a摔碎的鸡蛋数量
    val total = readInt("total: ")
    val floor = readInt("floor: ")
    val array = IntArray(total) { if (it < floor) 0 else 1 }
    val pair1 = ex24a_ThrowingEggs(array)
    println(array.joinToString(limit = 100))
    println("ex24a total=$total floor=$floor result=${pair1.first} count=${pair1.second}")

    //计算total层楼时，ex24a方法的平均值，与期望值1/2*lgN对比
    //total层的楼共有total+1种可能性，例如total=3时，可能为(1,1,1)(0,1,1)(0,0,1)(0,0,0)
    var totalCount = 0
    repeat(total + 1) { loopFloor ->
        val loopArray = IntArray(total) { if (it < loopFloor) 0 else 1 }
        val loopPair = ex24a_ThrowingEggs(loopArray)
        totalCount += loopPair.second
    }
    println("ex24a 1/2*lgN = ${formatDouble(log2(total.toDouble()) / 2, 2)} average count = ${formatDouble(totalCount.toDouble() / (total + 1), 2)}")

    val pair2 = ex24b_ThrowingEggs(array)
    println("ex24b total=$total floor=$floor result=${pair2.first} count=${pair2.second}")
    println("ex24b (1+lgF)/2=${formatDouble((1 + log2(floor.toDouble())) / 2, 2)}")
}