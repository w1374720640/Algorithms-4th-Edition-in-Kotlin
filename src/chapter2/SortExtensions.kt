package chapter2

import chapter2.section1.bubbleSort
import chapter2.section1.insertionSort
import chapter2.section1.selectionSort
import chapter2.section1.shellSort
import chapter2.section2.buttonUpMergeSort
import chapter2.section2.topDownMergeSort
import edu.princeton.cs.algs4.StdDraw
import extensions.*
import java.awt.Color

/**
 * 对数组排序时，每次交换数组的值会回调这个接口
 * 每个方法接收一个数组作为tag参数，用于标识排序的是哪个数组
 */
interface SwapCallback {
    fun before(tag: Any, i: Int, j: Int)
    fun after(tag: Any, i: Int, j: Int)
}

/**
 * 数据交换回调列表
 */
val swapCallbackList = mutableListOf<SwapCallback>()

/**
 * 所有排序函数都应该使用这个扩展函数交换数据
 */
fun <T : Comparable<T>> Array<T>.swap(i: Int, j: Int) {
    swapCallbackList.forEach { callback ->
        callback.before(this, i, j)
    }
    val temp = this[i]
    this[i] = this[j]
    this[j] = temp
    swapCallbackList.forEach { callback ->
        callback.after(this, i, j)
    }
}

/**
 * 数据对比回调列表
 */
val comparisonCallbackList = mutableListOf<(Any, Int, Int) -> Unit>()

/**
 * 所有排序函数都应该使用这个方法对比大小
 */
fun <T : Comparable<T>> Array<T>.less(i: Int, j: Int): Boolean {
    comparisonCallbackList.forEach { callback ->
        callback(this, i, j)
    }
    return this[i] < this[j]
}

/**
 * 暂停程序一段时间
 */
fun sleep(timeMillis: Long) {
    Thread.sleep(timeMillis)
}

/**
 * 设置画笔颜色
 */
fun setPenColor(color: Color) {
    StdDraw.setPenColor(color)
}
/**
 * 使用绘图API直观显示数组的排序过程
 * 可用通过showComparisonProcess参数控制只显示交换过程还是同时显示对比和交换过程
 * 默认为亮灰色，对比时显示灰色，交换时显示黑色
 * 因为是使用直方图显示数据，所以数组大小最好不要超过100，不然会显得很拥挤，看不清
 *
 * @param array 待排序的数组
 * @param sortFun 用来排序数组的函数，函数内必须用这个文件中的Array<T>.swap(Int,Int)方法交换数据才可以正常工作
 * @param delay 每次绘图的间隔时间，单位毫秒
 * @param showComparisonProcess 是否显示每次对比的过程，如果为false则只显示交换过程，为true同时显示对比和交换的过程
 * @return 返回交换的次数
 */
fun showSortingProcess(array: Array<Double>, sortFun: (Array<Double>) -> Unit, delay: Long, showComparisonProcess: Boolean): Int {
    require(array.size > 1)
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
        setPenColor(Color.LIGHT_GRAY)
        for (i in array.indices) {
            drawItem(i)
        }
    }

    fun drawSwapItem(i: Int, j: Int, isSelected: Boolean) {
        setPenColor(if (isSelected) Color.BLACK else Color.LIGHT_GRAY)
        drawItem(i)
        drawItem(j)
    }

    fun drawComparisonItem(i: Int, j: Int, isSelected: Boolean) {
        setPenColor(if (isSelected) Color.GRAY else Color.LIGHT_GRAY)
        drawItem(i)
        drawItem(j)
    }

    fun clearSwapItem(i: Int, j: Int) {
        setPenColor(Color.WHITE)
        StdDraw.filledRectangle(i + 0.5, (max + min) / 2, 0.5, (max - min) / 2 + space)
        StdDraw.filledRectangle(j + 0.5, (max + min) / 2, 0.5, (max - min) / 2 + space)
    }

    var swapTimes = 0
    val swapCallback = object : SwapCallback {
        override fun before(tag: Any, i: Int, j: Int) {
            if (tag !== array) return
            drawSwapItem(i, j, true)
            sleep(delay)
            swapTimes++
        }

        override fun after(tag: Any, i: Int, j: Int) {
            if (tag !== array) return
            clearSwapItem(i, j)
            drawSwapItem(i, j, true)
            sleep(delay)
            drawSwapItem(i, j, false)
        }

    }
    val comparisonCallback = { tag: Any, i: Int, j: Int ->
        if (tag === array) {
            drawComparisonItem(i, j, true)
            sleep(delay)
            drawComparisonItem(i, j, false)
        }
    }
    swapCallbackList.add(swapCallback)
    if (showComparisonProcess) comparisonCallbackList.add(comparisonCallback)
    drawArray()
    sleep(delay)
    sortFun(array)
    swapCallbackList.remove(swapCallback)
    if (showComparisonProcess) comparisonCallbackList.remove(comparisonCallback)
    return swapTimes
}

/**
 * 数组的初始化状态，
 */
enum class ArrayInitialState {
    RANDOM, //完全随机
    ASC, //完全升序
    DESC, //完全降序
    NEARLY_ASC, //接近升序
    NEARLY_DESC //接近降序
}

/**
 * 对枚举类进行扩展，可以通过序数直接获取枚举对象
 */
inline fun <reified T : Enum<T>> enumValueOf(ordinal: Int): T {
    val enumList = enumValues<T>()
    require(enumList.isNotEmpty()) { "Enum objects should not be empty" }
    require(ordinal >= 0 && ordinal < enumList.size) { "The specified ordinal does not exist in the Enum Class" }
    return enumList[ordinal]
}

/**
 * 根据给定顺序，返回指定大小的Double类型数组
 */
fun getDoubleArray(size: Int, initialState: ArrayInitialState = ArrayInitialState.RANDOM): Array<Double> {
    return when (initialState) {
        ArrayInitialState.RANDOM -> {
            Array(size) { random() }
        }
        ArrayInitialState.ASC -> {
            Array(size) { it.toDouble() }
        }
        ArrayInitialState.DESC -> {
            Array(size) { it * -1.0 }
        }
        ArrayInitialState.NEARLY_ASC -> {
            //90%概率按升序排列，10%概率在[0~size)范围内取随机Double值
            Array(size) { if (randomBoolean(0.9)) it.toDouble() else random(0.0, size.toDouble()) }
        }
        ArrayInitialState.NEARLY_DESC -> {
            Array(size) { if (randomBoolean(0.9)) it * -1.0 else random(size * -1.0, -0.0) }
        }
    }
}

/**
 * 对比不同排序方法的耗时
 */
fun sortMethodsCompare(sortFunctions: Array<Pair<String, (Array<Double>) -> Unit>>,
                       times: Int, size: Int, state: ArrayInitialState) {
    sortMethodsCompare(sortFunctions, times) { getDoubleArray(size, state) }
}

inline fun <T : Comparable<T>> sortMethodsCompare(sortFunctions: Array<Pair<String, (Array<T>) -> Unit>>,
                                                  times: Int, create: () -> Array<T>) {
    sortFunctions.forEach { sortFunPair ->
        var time = 0L
        repeat(times) {
            val array = create()
            time += spendTimeMillis {
                sortFunPair.second(array)
            }
        }
        println("${sortFunPair.first} average spend ${time / times} ms")
    }
}

fun main() {
    inputPrompt()
    val times = readInt("repeat times: ")
    val size = readInt("size: ")
    //设置初始数组是完全随机、完全升序、完全降序、接近升序、接近降序这五种状态
    val ordinal = readInt("array initial state(0~4): ")
    val state = enumValueOf<ArrayInitialState>(ordinal)
    println("Array initial state: ${state.name}")
    val sortMethods: Array<Pair<String, (Array<Double>) -> Unit>> = arrayOf(
            "Selection Sort" to ::selectionSort,
            "Bubble Sort" to ::bubbleSort,
            "Insertion Sort" to ::insertionSort,
            "Shell Sort" to ::shellSort,
            "Top Down Merge Sort" to ::topDownMergeSort,
            "Button Up Merge Sort" to ::buttonUpMergeSort
    )
    sortMethodsCompare(sortMethods, times, size, state)
}