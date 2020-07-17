package chapter2

import edu.princeton.cs.algs4.StdDraw
import extensions.random
import extensions.spendTimeMillis
import java.awt.Color

/**
 * 对数组排序时，每次交换数组的值会回调这个接口
 * 每个方法接收一个数组作为tag参数，用于标识排序的是哪个数组
 */
interface SwapListener {
    fun before(tag: Any, i: Int, j: Int)
    fun after(tag: Any, i: Int, j: Int)
}

/**
 * 数据交换回调列表
 */
val swapListenerList = mutableListOf<SwapListener>()

/**
 * 所有排序函数都应该使用这个扩展函数交换数据
 */
fun <T : Comparable<T>> Array<T>.swap(i: Int, j: Int) {
    swapListenerList.forEach { listener ->
        listener.before(this, i, j)
    }
    val temp = this[i]
    this[i] = this[j]
    this[j] = temp
    swapListenerList.forEach { listener ->
        listener.after(this, i, j)
    }
}

/**
 * 用随机数据测试排序方法性能
 */
fun timeRandomInput(sortFun: (Array<Double>) -> Unit, size: Int, times: Int = 1): Long {
    var time = 0L
    repeat(times) {
        val array = Array(size) { random() }
        val spendTime = spendTimeMillis { sortFun(array) }
        time += spendTime
    }
    return time
}

/**
 * 使用绘图API直观显示数组的排序过程
 * 因为是使用直方图显示数据，所以数组大小最好不要超过100，不然会显得很拥挤，看不清
 *
 * @param array 待排序的数组
 * @param sortFun 用来排序数组的函数，函数内必须用这个文件中的Array<T>.swap(Int,Int)方法交换数据才可以正常工作
 * @param delay 每次绘图的间隔时间，单位毫秒
 * @return 返回交换的次数
 */
fun showSortingProcess(array: Array<Double>, sortFun: (Array<Double>) -> Unit, delay: Long): Int {
    var min = Double.MAX_VALUE
    var max = Double.MIN_VALUE
    array.forEach {
        if (it < min) min = it
        if (it > max) max = it
    }
    //让最小的值也有高度，最大的值也与顶部有距离
    val space = (max - min) / 100
    //设置画布横坐标和纵坐标的范围，让数据填满画布
    StdDraw.setXscale(0.0, array.size.toDouble())
    StdDraw.setYscale(min - space, max + space)

    fun drawItem(index: Int) {
        //绘制直方图，每个item中间设置一定间隔
        StdDraw.filledRectangle(index + 0.5, (array[index] + min - space) / 2, 0.4, (array[index] - min + space) / 2)
    }

    fun drawArray() {
        StdDraw.setPenColor(Color.LIGHT_GRAY)
        for (i in array.indices) {
            drawItem(i)
        }
    }

    fun drawSwapItem(i: Int, j: Int, isSelected: Boolean) {
        StdDraw.setPenColor(if (isSelected) Color.BLACK else Color.LIGHT_GRAY)
        drawItem(i)
        drawItem(j)
    }

    fun clearSwapItem(i: Int, j: Int) {
        StdDraw.setPenColor(Color.WHITE)
        StdDraw.filledRectangle(i + 0.5, (max + min) / 2, 0.5, (max - min) / 2 + space)
        StdDraw.filledRectangle(j + 0.5, (max + min) / 2, 0.5, (max - min) / 2 + space)
    }

    var swapTimes = 0
    val listener = object : SwapListener {
        override fun before(tag: Any, i: Int, j: Int) {
            if (tag !== array) return
            drawSwapItem(i, j, true)
            Thread.sleep(delay)
            swapTimes++
        }

        override fun after(tag: Any, i: Int, j: Int) {
            if (tag !== array) return
            clearSwapItem(i, j)
            drawSwapItem(i, j, true)
            Thread.sleep(delay)
            drawSwapItem(i, j, false)
        }

    }
    swapListenerList.add(listener)
    drawArray()
    Thread.sleep(delay)
    sortFun(array)
    swapListenerList.remove(listener)
    return swapTimes
}
