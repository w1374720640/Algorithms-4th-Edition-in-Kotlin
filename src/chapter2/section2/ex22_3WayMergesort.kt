package chapter2.section2

import chapter2.ArrayInitialState
import chapter2.getDoubleArray
import chapter2.section1.checkAscOrder
import chapter2.section1.doublingTest
import chapter2.sortMethodsCompare
import extensions.spendTimeMillis
import kotlin.math.log2

/**
 * 三向归并
 * 假设每次我们是把数组分成三个部分而不是两个部分并将它们分别排序，然后进行三向归并
 * 这种算法的运行时间的增长数量级是多少？
 *
 * 解：三向归并的最大层数为log₃N=log₂N/log₂3  ~lgN
 * 每层需要从原始数组向额外数组拷贝N次，每次拷贝进行两次数组访问，2N
 * 每层N个元素，每个元素需要对比两次才能判断最小值，每次对比需要访问数组两次，4N
 * 从额外数组向原始数组拷贝数据，需要访问数组两次，2N
 * 总增长数量级为8NlgN/log₂3 ≈5NlgN   ~NlgN
 */
fun <T : Comparable<T>> ex22_3WayMergesort(originalArray: Array<T>) {
    val extraArray = originalArray.copyOf()
    ex22_3WayMergesort(originalArray, extraArray, 0, originalArray.size - 1)
}

fun <T : Comparable<T>> ex22_3WayMergesort(originalArray: Array<T>, extraArray: Array<T>, start: Int, end: Int) {
    if (start >= end) return
    val length = end - start + 1
    //取值范围长度为2时需要特殊处理
    if (length == 2) {
        if (originalArray[start] > originalArray[end]) {
            val temp = originalArray[start]
            originalArray[start] = originalArray[end]
            originalArray[end] = temp
        }
        return
    }
    val secondStart = length / 3 + start
    val thirdStart = length / 3 * 2 + start
    ex22_3WayMergesort(originalArray, extraArray, start, secondStart - 1)
    ex22_3WayMergesort(originalArray, extraArray, secondStart, thirdStart - 1)
    ex22_3WayMergesort(originalArray, extraArray, thirdStart, end)
    ex22Merge(originalArray, extraArray, start, secondStart, thirdStart, end)
}

fun <T : Comparable<T>> ex22Merge(originalArray: Array<T>,
                                  extraArray: Array<T>,
                                  start: Int,
                                  secondStart: Int,
                                  thirdStart: Int,
                                  end: Int) {
    for (i in start..end) {
        extraArray[i] = originalArray[i]
    }
    var first = start
    var second = secondStart
    var third = thirdStart
    var k = start
    while (k <= end) {
        when {
            //有两部分超范围，直接取第三部分的值
            first >= secondStart && second >= thirdStart -> originalArray[k++] = extraArray[third++]
            first >= secondStart && third > end -> originalArray[k++] = extraArray[second++]
            second >= thirdStart && third > end -> originalArray[k++] = extraArray[first++]

            //有一部分超范围，比较剩余两部分的值，取较小的值
            first >= secondStart -> {
                if (extraArray[second] <= extraArray[third]) {
                    originalArray[k++] = extraArray[second++]
                } else {
                    originalArray[k++] = extraArray[third++]
                }
            }
            second >= thirdStart -> {
                if (extraArray[first] <= extraArray[third]) {
                    originalArray[k++] = extraArray[first++]
                } else {
                    originalArray[k++] = extraArray[third++]
                }
            }
            third > end -> {
                if (extraArray[first] <= extraArray[second]) {
                    originalArray[k++] = extraArray[first++]
                } else {
                    originalArray[k++] = extraArray[second++]
                }
            }

            //三部分都在有效范围内，找出最小值
            else -> {
                if (extraArray[first] <= extraArray[second]) {
                    if (extraArray[first] <= extraArray[third]) {
                        originalArray[k++] = extraArray[first++]
                    } else {
                        originalArray[k++] = extraArray[third++]
                    }
                } else {
                    if (extraArray[second] <= extraArray[third]) {
                        originalArray[k++] = extraArray[second++]
                    } else {
                        originalArray[k++] = extraArray[third++]
                    }
                }
            }
        }
    }
}

fun main() {
    println("checkAscOrder: ")
    val size = 100_0000
    val array = getDoubleArray(size)
    val time = spendTimeMillis {
        ex22_3WayMergesort(array)
    }
    val isAscOrder = array.checkAscOrder()
    println("isAscOrder=$isAscOrder time=$time ms")

    println("doubleGrowthTest:")
    doublingTest(size *10, ::ex22_3WayMergesort) { N -> N * log2(N.toDouble()) }

    println("sortMethodsCompare: ")
    val sortMethodList = arrayOf<Pair<String, (Array<Double>)-> Unit>>(
            "Top Down Merge Sort" to ::topDownMergeSort,
            "ex22" to ::ex22_3WayMergesort
    )
    sortMethodsCompare(sortMethodList, 10, size, ArrayInitialState.RANDOM)
}