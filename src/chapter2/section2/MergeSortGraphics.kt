package chapter2.section2

import chapter2.*
import edu.princeton.cs.algs4.StdDraw
import java.awt.Color

interface MergeSortCallback {
    fun mergeStart(start: Int, end: Int)
    fun copyToOriginal(extraIndex: Int, originalIndex: Int)
}

val mergeSortCallbackList = mutableListOf<MergeSortCallback>()

enum class ArrayType {
    ORIGINAL, EXTRA
}

/**
 * 使用绘图API直观显示数组的归并排序过程
 * 因为归并排序需要一个大小为N的额外数组，所以绘制上下两幅图，上面的图形表示原始数组，下面的图形表示创建的额外数组
 * 默认为亮灰色，对比时显示灰色，交换时显示黑色
 * 因为是使用直方图显示数据，所以数组大小最好不要超过100，不然会显得很拥挤，看不清
 *
 * @param array 待排序的数组
 * @param sortMethod 用来排序数组的函数，merge函数中必须回调MergeSortCallback的相应方法才可以正常工作
 * @param delay 每次绘图的间隔时间，单位毫秒
 */
fun showMergeSortProcess(array: Array<Double>, sortMethod: (Array<Double>) -> Unit, delay: Long) {
    require(array.size > 1)
    val originalArray = array.copyOf()
    var min = Double.MAX_VALUE
    var max = Double.MIN_VALUE
    originalArray.forEach {
        if (it < min) min = it
        if (it > max) max = it
    }
    val extraArray = Array(originalArray.size) { 0.0 }
    //让最小的值也有高度，最大的值也与顶部有距离
    val space = (max - min) / 100
    //一幅图的高度
    val height = max - min + 2 * space
    //设置画布横坐标和纵坐标的范围，让数据填满画布
    StdDraw.setXscale(0.0, originalArray.size.toDouble())
    StdDraw.setYscale(0.0, 2 * height)

    fun calculateYCenter(type: ArrayType, index: Int): Double {
        return if (type == ArrayType.ORIGINAL) {
            height + (originalArray[index] - min + space) / 2
        } else {
            (extraArray[index] - min + space) / 2
        }
    }

    fun calculateHalfHeight(type: ArrayType, index: Int): Double {
        return if (type == ArrayType.ORIGINAL) {
            (originalArray[index] - min + space) / 2
        } else {
            (extraArray[index] - min + space) / 2
        }
    }

    fun drawItem(type: ArrayType, index: Int) {
        //绘制直方图，每个item中间设置一定间隔
        StdDraw.filledRectangle(index + 0.5, calculateYCenter(type, index), 0.4, calculateHalfHeight(type, index))
    }

    fun drawArray(type: ArrayType) {
        setPenColor(Color.LIGHT_GRAY)
        for (i in if (type == ArrayType.ORIGINAL) originalArray.indices else extraArray.indices) {
            drawItem(type, i)
        }
    }

    fun clearItem(type: ArrayType, index: Int) {
        setPenColor(Color.WHITE)
        StdDraw.filledRectangle(index + 0.5, if (type == ArrayType.ORIGINAL) height * 3.0 / 2 else height / 2, 0.5, height / 2)
    }

    fun clearRangeItem(type: ArrayType, start: Int, end: Int) {
        setPenColor(Color.WHITE)
        val average = (end + start) / 2.0
        StdDraw.filledRectangle(average + 0.5, if (type == ArrayType.ORIGINAL) height * 3.0 / 2 else height / 2,
                average - start + 0.5, height / 2)
    }

    val callback = object : MergeSortCallback {
        override fun mergeStart(start: Int, end: Int) {
            //从原始数组中拷贝指定范围数据到额外创建的数组中
            clearRangeItem(ArrayType.EXTRA, start, end)
            setPenColor(Color.GRAY)
            for (i in start..end) {
                extraArray[i] = originalArray[i]
                drawItem(ArrayType.ORIGINAL, i)
                drawItem(ArrayType.EXTRA, i)
            }
            sleep(delay)
        }

        override fun copyToOriginal(extraIndex: Int, originalIndex: Int) {
            //从额外创建的数组中拷贝一个值到原始数组中
            setPenColor(Color.BLACK)
            drawItem(ArrayType.EXTRA, extraIndex)
            sleep(delay)

            setPenColor(Color.LIGHT_GRAY)
            drawItem(ArrayType.EXTRA, extraIndex)
            clearItem(ArrayType.ORIGINAL, originalIndex)
            setPenColor(Color.BLACK)
            originalArray[originalIndex] = extraArray[extraIndex]
            drawItem(ArrayType.ORIGINAL, originalIndex)
            sleep(delay)

            setPenColor(Color.LIGHT_GRAY)
            drawItem(ArrayType.ORIGINAL, originalIndex)
        }
    }
    mergeSortCallbackList.add(callback)
    drawArray(ArrayType.ORIGINAL)
    sleep(delay)
    sortMethod(originalArray)
    mergeSortCallbackList.remove(callback)
}