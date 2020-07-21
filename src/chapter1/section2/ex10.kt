package chapter1.section2

import edu.princeton.cs.algs4.StdDraw
import extensions.*
import java.awt.Color
import kotlin.math.abs

/**
 * 支持加一和减一的操作，最大操作次数为N，计数器绝对值最大值为max
 * 绘制出每次计数器变化后的值
 */
class VisualCounter(private val N: Int, private val max: Int) {
    var count = 0
    var value = 0

    init {
        //对坐标进行伸缩
        StdDraw.setXscale(0.0, N.toDouble())
        StdDraw.setYscale(-max.toDouble(), max.toDouble())
        //画背景
        StdDraw.setPenColor(Color.lightGray)
        StdDraw.line(0.0, 0.0, N.toDouble(), 0.0)
        StdDraw.textLeft(0.0, 0.0, "(0,0)")
        StdDraw.textRight(N.toDouble(), 0.0, "(${N},0)")
        StdDraw.setPenColor(Color.BLACK)
        //设置点的半径
        StdDraw.setPenRadius(0.01)
    }

    //加一
    fun increment() {
        //预先判断操作完成后计数器的绝对值，如果大于max，直接结束程序
        if (abs(value + 1) > max) {
            delayExit()
        }
        value++
        count++
        draw()
        //操作结束后如果达到最大操作次数，直接结束程序
        if (count >= N) {
            delayExit()
        }
    }

    //减一
    fun decrease() {
        if (abs(value - 1) > max) {
            delayExit()
        }
        value--
        count++
        draw()
        if (count >= N) {
            delayExit()
        }
    }

    private fun draw() {
        //count范围为(1,N]，value范围为[-max,max]，绘图时需要在[0,1]范围内
        StdDraw.point(count.toDouble(), value.toDouble())
    }
}

fun main() {
    inputPrompt()
    val N = readInt()
    val max = readInt()
    //随机加一的概率为probability，减一的概率为 1 - probability
    val probability = readDouble()
    require(N > 0 && max > 0 && probability in 0.0..1.0)
    val counter = VisualCounter(N, max)
    do {
        if (randomBoolean(probability)) {
            counter.increment()
        } else {
            counter.decrease()
        }
    } while (true)
}