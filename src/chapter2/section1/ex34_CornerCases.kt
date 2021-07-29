package chapter2.section1

import chapter2.ArrayInitialState
import chapter2.getDoubleArray
import extensions.randomBoolean

/**
 * 罕见情况
 * 编写一个测试用例，调用sort()方法对实际应用中可能出现的困难或极端情况的数组进行排序
 * 比如，数组可能是有序的，或是逆序的，数组的所有主键相同，数组的主键只有两种值，大小为0或为1的数组
 *
 * 解：使用check()方法检查排序后的数组是否有序，检查失败则抛出异常
 * 不能使用assert来进行断言判断，断言默认是关闭的，需要添加额外参数-ea才会生效
 * require常用来检测输入数据是否合法，不应当用来检测结果，check用于检查程序状态是否正确，应该用check
 */
fun cornerCases(sortMethod: (Array<Double>) -> Unit) {
    sortMethod(arrayOf())
    sortMethod(arrayOf(1.0))
    repeat(10) {
        val array = getDoubleArray(2)
        check(ex16_Certification(array, sortMethod))

        val size = 100

        val sameValueArray = Array(size) { 1.0 }
        check(ex16_Certification(sameValueArray, sortMethod))

        val twoValuesArray = Array(size) { if (randomBoolean()) 0.0 else 1.0 }
        check(ex16_Certification(twoValuesArray, sortMethod))

        val ascArray = getDoubleArray(size, ArrayInitialState.ASC)
        check(ex16_Certification(ascArray, sortMethod))

        val nearlyAscArray = getDoubleArray(size, ArrayInitialState.NEARLY_ASC)
        check(ex16_Certification(nearlyAscArray, sortMethod))

        val descArray = getDoubleArray(size, ArrayInitialState.DESC)
        check(ex16_Certification(descArray, sortMethod))

        val nearlyDescArray = getDoubleArray(size, ArrayInitialState.NEARLY_DESC)
        check(ex16_Certification(nearlyDescArray, sortMethod))

        val repeatArray = getDoubleArray(size, ArrayInitialState.REPEAT)
        check(ex16_Certification(repeatArray, sortMethod))

        val randomValuesArray = getDoubleArray(size)
        check(ex16_Certification(randomValuesArray, sortMethod))
    }
    println("The sorting method is correct")
}

/**
 * 检查数组是否按正序排列
 */
fun <T : Comparable<T>> Array<T>.checkAscOrder(): Boolean {
    for (i in 0..this.size - 2) {
        if (this[i] > this[i + 1]) return false
    }
    return true
}

/**
 * 检查数组是否按逆序排列
 */
fun <T : Comparable<T>> Array<T>.checkDescOrder(): Boolean {
    for (i in 0..this.size - 2) {
        if (this[i] < this[i + 1]) return false
    }
    return true
}

fun main() {
    val sortMethodList = listOf<(Array<Double>) -> Unit>(::selectionSort, ::bubbleSort, ::insertionSort, ::shellSort)
    sortMethodList.forEach {
        cornerCases(it)
    }
    println("All sorting methods pass corner cases test")
}