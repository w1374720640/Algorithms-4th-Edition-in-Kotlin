package chapter2.section2

import chapter2.ArrayInitialState
import chapter2.getDoubleArray
import chapter2.section1.checkAscOrder
import chapter2.section1.doubleGrowthTest
import chapter2.section1.insertionSort
import chapter2.section1.selectionSort
import chapter2.sortMethodsCompare

/**
 * 次线性的额外空间
 * 用大小M将数组分为N/M块（简单起见，设M是N的约数）
 * 实现一个归并方法，使所需的额外空间减少到max(M, N/M)
 * (1) 可以先将一个块看作一个元素，将块的第一个元素作为块的主键，用选择排序将块排序
 * (2) 遍历数组，将第一块和第二块归并，完成后将第二块和第三块归并，等等
 *
 * 解：先将每个块内用选择排序单独排序，时间复杂度为N/M*M²=N*M
 * 开始归并：
 * 将第一块和第二块归并，完成后再将第二块和第三块归并、将第三块和第四块归并...
 * 原理类似于冒泡排序，当第一次循环结束后，最后一块内的数据已经符合最终的顺序，下一次循环只需要归并到倒数第二块数据
 * 每个循环需要进行 N/M-1 N/M-2 ... 3 2 1次归并，约等于(N/M)²/2
 * 每次归并需要额外空间M，时间复杂度为2M，所以归并过程需要的时间复杂度为(N/M)²/2*2M = N²/M
 * 所以算法总的时间复杂度为N*M+N²/M ~N² 额外空间为M
 */
fun <T : Comparable<T>> ex12(array: Array<T>, M: Int) {
    require(array.size % M == 0) { "For simplicity, assume that array.size is a multiple of M" }
    ex12SelectionSort(array, M)
    val extraArray = array.copyOfRange(0, M)
    for (i in array.size - M downTo M step M) {
        for (start in 0 until i step M) {
            ex12Merge(array, extraArray, M, start)
        }
    }
}

fun <T : Comparable<T>> ex12SelectionSort(array: Array<T>, M: Int) {
    for (i in array.indices step M) {
        selectionSort(array, i, i + M - 1)
    }
}

fun <T : Comparable<T>> ex12Merge(array: Array<T>, extraArray: Array<T>, M: Int, start: Int) {
    for (i in start until start + M) {
        extraArray[i - start] = array[i]
    }
    var i = start
    var j = start + M
    var k = start
    while (i < start + M) {
        when {
            j >= start + M * 2 || extraArray[i - start] <= array[j] -> {
                array[k++] = extraArray[i - start]
                i++
            }
            else -> array[k++] = array[j++]
        }
    }
}

fun main() {
    val array = getDoubleArray(1000)
        ex12(array, 10)
    val isAscOrder = array.checkAscOrder()
    println("isAscOrder=$isAscOrder")

    println("sortMethodsCompare:")
    //因为ex12函数的参数数量不符合sortMethodsCompare方法的要求，在外面包裹一层
    val ex12: (Array<Double>) -> Unit = { ex12(it, 10) }
    val sortMethodList = arrayOf<Pair<String, (Array<Double>) -> Unit>>(
            "Selection Sort" to ::selectionSort,
            "Insertion Sort" to ::insertionSort,
            "ex12" to ex12,
            "Top Down Merge Sort" to ::topDownMergeSort
    )
    sortMethodsCompare(sortMethodList, 10, 1_0000, ArrayInitialState.RANDOM)

    println("doubleGrowthTest:")
    doubleGrowthTest(20_0000, ex12) { N -> N.toDouble() * N }
}

