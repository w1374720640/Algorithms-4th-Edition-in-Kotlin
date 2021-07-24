package chapter1.section4

import extensions.inputPrompt
import extensions.readInt
import kotlin.math.abs
import kotlin.math.log2

/**
 * 热还是冷
 * 你的目标是猜出1到N之间的一个秘密的整数。
 * 每次猜完一个整数后，你会知道你的猜测和这个秘密整数是否相等（如果是则游戏结束）。
 * 如果不相等，你会知道你的猜测相比上一次猜测距离该秘密整数是比较热（接近）还是比较冷（远离）。
 * 设计一个算法在~2lgN之内找到这个秘密整数，然后再设计一个算法在~1lgN之内找到这个秘密整数。
 *
 * 解：先判断起始位置，再判断结束位置，根据冷热程度的变化可以确定要找的点是在前半段还是后半段
 * 改变查找范围，重复上面的判断
 * 每次范围减半，需要循环~lgN次，每次需要判断两次，所以总的判断次数为~2lgN
 */
fun ex34a_HotOrCold(hotOrCold: HotOrCold, N: Int): Int {
    var start = 1
    var end = N
    while (start < end) {
        if (hotOrCold.judge(start) == HotOrCold.Feel.HIT) return start
        val mid = (start + end) / 2
        when (hotOrCold.judge(end)) {
            HotOrCold.Feel.HIT -> return end
            //只能判断是冷还是热，无法判断两次距离相等的情况，所以不能像标准二分法那样直接用mid+1或mid-1
            HotOrCold.Feel.HOT -> {
                if (start == mid) {
                    return -1
                } else {
                    start = mid
                }
            }
            HotOrCold.Feel.COLD -> end = mid
        }
    }
    return -1
}

/**
 * 其他条件同上，设计一个算法，在~1lgN内找到这个整数
 *
 * 解：上题每个循环内部都要对比起始点和结束点，但每次只有一端位置改变了
 * 如果能利用上一个判断点，就能节省一次判断
 * 让实际的查找范围和实际的判断点分离，实际的查找范围[start,end]，实际的判断点分别为leftPoint和rightPoint
 * 实际的查找范围不断缩小，判断点不断向两侧扩大
 * 如果上一次对比了rightPoint，则本次对比leftPoint，修正leftPoint，让实际查找范围的中值和实际判断点的中值相等
 * 这样对比leftPoint和rightPoint的结果等于对比start和end的结果
 * 每次循环实际查找范围减半，只对比一次，所以总对比次数为~lgN
 */
fun ex34b_HotOrCold(hotOrCold: HotOrCold, N: Int): Int {
    var start = 1
    var end = N
    var lastPoint = start
    var lastComparisonPointIsLeft = true
    if (hotOrCold.judge(start) == HotOrCold.Feel.HIT) return start
    while (start < end) {
        val mid = (start + end) / 2
        val newPoint = start + end - lastPoint
        when (hotOrCold.judge(newPoint)) {
            HotOrCold.Feel.HIT -> return newPoint
            HotOrCold.Feel.HOT -> {
                if (lastComparisonPointIsLeft) {
                    start = mid
                } else {
                    end = mid
                }
            }
            HotOrCold.Feel.COLD -> {
                if (lastComparisonPointIsLeft) {
                    end = mid
                } else {
                    start = mid
                }
            }
        }
        lastPoint = newPoint
        lastComparisonPointIsLeft = !lastComparisonPointIsLeft
        if (end - start <= 1) break
    }
    if (hotOrCold.judge(start) == HotOrCold.Feel.HIT) return start
    if (hotOrCold.judge(end) == HotOrCold.Feel.HIT) return end
    return -1
}

//用于判断和上一次的猜测结果相比，是更近了（HOT），还是更远了（COLD），还是命中目标（HIT）
class HotOrCold(private val key: Int) {
    private var lastDistance = Int.MAX_VALUE
    var comparisonTimes = 0
        private set

    enum class Feel {
        HOT, HIT, COLD
    }

    fun judge(value: Int): Feel {
        comparisonTimes++
        val distance = abs(value - key)
        val result = when {
            distance == 0 -> Feel.HIT
            distance > lastDistance -> Feel.COLD
            else -> Feel.HOT
        }
        lastDistance = distance
        return result
    }
}

fun main() {
    inputPrompt()
    val N = readInt("N: ")
    val key = readInt("key: ")
    val hotOrColdA = HotOrCold(key)
    val resultA = ex34a_HotOrCold(hotOrColdA, N)
    println("ex34a result=$resultA comparisonTimes=${hotOrColdA.comparisonTimes} 2lgN=${2 * log2(N.toDouble()).toInt()}")
    val hotOrColdB = HotOrCold(key)
    val resultB = ex34b_HotOrCold(hotOrColdB, N)
    println("ex34b result=$resultB comparisonTimes=${hotOrColdB.comparisonTimes} lgN=${log2(N.toDouble()).toInt()}")
}