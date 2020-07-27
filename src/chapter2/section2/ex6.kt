package chapter2.section2

import chapter2.getDoubleArray
import chapter2.setPenColor
import edu.princeton.cs.algs4.Point2D
import edu.princeton.cs.algs4.StdDraw
import extensions.delayExit
import java.awt.Color
import kotlin.math.log2
import kotlin.math.max

/**
 * 编写一个程序来计算自顶向下和自底向上的归并排序访问数组的准确次数
 * 使用这个程序将N=1至512的结果绘制成曲线，并将其和上限6NlgN比较
 */
fun ex6(array: Array<Double>, sortMethod: (Array<Double>) -> Unit): Int {
    var count = 0

    val callback = object : MergeSortCallback {
        var start = 0
        var end = 0
        var mid = 0
        var i = 0
        var j = 0

        override fun mergeStart(start: Int, end: Int) {
            this.start = start
            this.end = end
            mid = (start + end) / 2
            count += 2 * (end - start + 1)
        }

        override fun copyToOriginal(extraIndex: Int, originalIndex: Int) {
            count += 2

            //除了交换数组元素需要访问数组外，对比数组元素大小也需要访问数组
            //参考merge方法中数组的对比次数
            if (i < mid && j < end) {
                count += 2
            }
            if (extraIndex > mid) {
                j = extraIndex
            } else {
                i = extraIndex
            }
        }
    }

    mergeSortCallbackList.add(callback)
    sortMethod(array)
    mergeSortCallbackList.remove(callback)
    return count
}

fun main() {
    val maxSize = 512
    val expectPointList = mutableListOf<Point2D>()
    val topDownMergePointList = mutableListOf<Point2D>()
    val buttonUpMergePointList = mutableListOf<Point2D>()

    fun drawPoint(index: Int) {
        if (index == 0) {
            StdDraw.setXscale(0.0, maxSize + 1.0)
            StdDraw.setYscale(0.0, max(expectPointList[index].y(), topDownMergePointList[index].y()) + 1)
        }
        //期望值6NlgN为红色
        setPenColor(Color.RED)
        expectPointList[index].draw()
        //由顶向下的实际访问次数为黑色
        setPenColor(Color.BLACK)
        topDownMergePointList[index].draw()
        //由底向上的实际访问次数为蓝色
        setPenColor(Color.BLUE)
        buttonUpMergePointList[index].draw()
    }

    for (i in 0 until maxSize) {
        //从大到小绘制，确定坐标范围
        val size = maxSize - i
        expectPointList.add(Point2D(size.toDouble(), 6 * size * log2(size.toDouble())))
        topDownMergePointList.add(Point2D(size.toDouble(), ex6(getDoubleArray(size), ::topDownMergeSort).toDouble()))
        buttonUpMergePointList.add(Point2D(size.toDouble(), ex6(getDoubleArray(size), ::buttonUpMergeSort).toDouble()))
        drawPoint(i)
    }
    delayExit()
}