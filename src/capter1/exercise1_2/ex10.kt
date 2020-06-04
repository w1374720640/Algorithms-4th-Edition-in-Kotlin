package capter1.exercise1_2

import edu.princeton.cs.algs4.StdDraw
import extensions.inputPrompt
import extensions.randomBoolean
import extensions.readDouble
import extensions.readInt
import java.awt.Color
import kotlin.math.abs
import kotlin.system.exitProcess

/**
 * 支持加一和减一的操作，最大操作次数为N，计数器绝对值最大值为max
 * 绘制出每次计数器变化后的值
 */
class VisualCounter(private val N: Int, private val max: Int) {
    var count = 0
    var value = 0

    init {
        //画背景
        StdDraw.setPenColor(Color.lightGray)
        StdDraw.line(0.0, 0.5, 1.0, 0.5)
        StdDraw.textLeft(0.0, 0.5, "(0,0)")
        StdDraw.textRight(1.0, 0.5, "(${N},0)")
        StdDraw.setPenColor(Color.BLACK)
        //设置点的半径
        StdDraw.setPenRadius(0.01)
    }

    //加一
    fun increment() {
        //预先判断操作完成后计数器的绝对值，如果大于max，直接结束程序
        if (abs(value + 1) > max) {
            end()
        }
        value++
        count++
        draw()
        //操作结束后如果达到最大操作次数，直接结束程序
        if (count >= N) {
            end()
        }
    }

    //减一
    fun decrease() {
        if (abs(value - 1) > max) {
            end()
        }
        value--
        count++
        draw()
        if (count >= N) {
            end()
        }
    }

    private fun draw() {
        //count范围为(1,N]，value范围为[-max,max]，绘图时需要在[0,1]范围内
        StdDraw.point(count.toDouble() / N,
                1.0 / (2 * max) * value + 0.5)
    }

    //为了让程序结束时仍然能看到绘制的图形，使用一个读取的阻塞操作让进程延时退出
    private fun end() {
        print("Press <Enter> to exit ")
        readLine()
        exitProcess(0)
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